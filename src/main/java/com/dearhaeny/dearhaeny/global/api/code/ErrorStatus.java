package com.dearhaeny.dearhaeny.global.api.code;

import com.dearhaeny.dearhaeny.global.api.dto.ErrorReasonDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorStatus implements BaseErrorCode{

    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "E-500-01", "서버 내부 오류가 발생했습니다."),
    _BAD_REQUEST(HttpStatus.BAD_REQUEST, "E-400-01", "잘못된 요청입니다."),
    VALIDATION_ERROR(HttpStatus.BAD_REQUEST, "E-400-02", "요청 값이 올바르지 않습니다."),
    REQUIRED_FIELD_MISSING(HttpStatus.BAD_REQUEST,"E-400-03", "필수 입력 항목이 누락되었습니다."),
    INVALID_WRITER(HttpStatus.FORBIDDEN, "E-403-01", "본인이 작성한 글에 대해서만 권한이 있습니다."),
    NOT_FOUND(HttpStatus.NOT_FOUND, "E-404", "대상을 찾을 수 없습니다."),
    POST_NOT_FOUND(HttpStatus.NOT_FOUND, "E-404-01", "해당 마음 게시글을 찾을 수 없습니다."),
    DUPLICATE_NICKNAME(HttpStatus.CONFLICT, "E-409-01", "이미 사용 중인 닉네임입니다."),
    REPLY_ALREADY_EXIST(HttpStatus.CONFLICT, "E-409-02", "이미 해당 마음 글에 대한 답장이 생성되어 있습니다."),
    REPLY_NOT_FOUND(HttpStatus.NOT_FOUND, "E-404-02", "해당 마음 글에 대한 답장이 존재하지 않습니다."),

    // GEMINI
    GEMINI_BLOCKED(HttpStatus.BAD_REQUEST, "E-400-03", "Gemini 응답이 정책에 인해 차단되었습니다."),
    GEMINI_API_KEY_MISSING(HttpStatus.INTERNAL_SERVER_ERROR, "E-500-02", "Gmini API 키가 설정되지 않았습니다."),
    GEMINI_HTTP_ERROR(HttpStatus.BAD_GATEWAY, "E-502-01", "Gemini HTTP 호출을 실패항였습니다."),
    GEMINI_INVALID_RESPONSE(HttpStatus.BAD_GATEWAY, "E-502-02", "Gemini 응답 포맷이 올바르지 않습니다."),
    GEMINI_ERROR_RETURNED(HttpStatus.BAD_GATEWAY, "E-502-03", "Gemini 응답 에러"),
    GEMINI_EMPTY_TEXT(HttpStatus.BAD_GATEWAY, "E-502-04", "Gemini가 빈 텍스트를 반환했습니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    @Override
    public ErrorReasonDto getReason() {
        return ErrorReasonDto.builder()
                .isSuccess(false)
                .code(code)
                .message(message)
                .build();
    }

    @Override
    public ErrorReasonDto getReasonHttpStatus(){
        return ErrorReasonDto.builder()
                .message(message)
                .code(code)
                .isSuccess(false)
                .httpStatus(httpStatus)
                .build();
    }
}