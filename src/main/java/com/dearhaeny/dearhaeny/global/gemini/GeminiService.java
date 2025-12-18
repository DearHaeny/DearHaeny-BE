package com.dearhaeny.dearhaeny.global.gemini;

import com.dearhaeny.dearhaeny.global.api.code.ErrorStatus;
import com.dearhaeny.dearhaeny.global.api.exception.GeneralException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.springframework.util.StringUtils.hasText;

@Service
@Slf4j
@RequiredArgsConstructor
public class GeminiService {

    @Value("${gemini.api.key}")
    private String apiKey;

    private static final String GEMINI_ENDPOINT =
            "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent";

    public String callGemini(String prompt) {
        try {
            // Gemini API Key 존재 여부 검증
            if (!hasText(apiKey)) {
                throw new GeneralException(ErrorStatus.GEMINI_API_KEY_MISSING);
            }

            ObjectMapper om = new ObjectMapper();

            String body = "{"
                    + "\"contents\":[{"
                    + "  \"parts\":[{"
                    + "    \"text\":" + om.writeValueAsString(prompt)
                    + "  }]"
                    + "}]"
                    + "}";

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(GEMINI_ENDPOINT + "?key=" + apiKey))
                    .header("Content-Type", "application/json; charset=UTF-8")
                    .POST(HttpRequest.BodyPublishers.ofString(body))
                    .build();

            HttpClient client = HttpClient.newHttpClient();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // HTTP 레벨 에러
            if (response.statusCode() < 200 || response.statusCode() >= 300) {
                throw new GeneralException(ErrorStatus.GEMINI_HTTP_ERROR,
                        "status=" + response.statusCode() + ", body=" + response.body());
            }


            // Gemini 응답 JSON 파싱
            JsonNode root = om.readTree(response.body());

            // 안정성 차단 여부 확 (Gemini가 정책 위반으로 응답을 차단한 경우)
            JsonNode feedback = root.path("promptFeedback");
            if (!feedback.isMissingNode()) {
                String blockReason = feedback.path("blockReason").asText("");
                if (hasText(blockReason)) {
                    throw new GeneralException(ErrorStatus.GEMINI_BLOCKED,
                            blockReason + " / " + response.body());
                }
            }

            // candidates 배열 존재 여부 검증: Gemini 응답 구조가 변경되었거나 비정상인 경우
            JsonNode candidates = root.path("candidates");
            if (!candidates.isArray() || candidates.isEmpty()) {
                throw new GeneralException(ErrorStatus.GEMINI_INVALID_RESPONSE,
                        "AI 응답에 candidates 누락: " + response.body());
            }

            JsonNode parts = candidates.get(0).path("content").path("parts");
            if (!parts.isArray() || parts.isEmpty()) {
                throw new RuntimeException("AI 응답에 parts 누락: " + response.body());
            }

            // 실제 AI 텍스트 응답 추출
            String text = parts.get(0).path("text").asText();
            if (!hasText(text)) {
                throw new GeneralException(ErrorStatus.GEMINI_EMPTY_TEXT, "body = " + response.body());
            }

            // 정상 응답 반환
            return text.trim();
        } catch (Exception e) {
            // 그 외 에러는 500 서버 에러 발생
            throw new GeneralException(ErrorStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
