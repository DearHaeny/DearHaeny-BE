package com.dearhaeny.dearhaeny.global.api.exception;

import com.dearhaeny.dearhaeny.global.api.code.ErrorStatus;
import com.dearhaeny.dearhaeny.global.api.dto.ApiResponse;
import com.dearhaeny.dearhaeny.global.api.dto.ErrorReasonDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    // GeneralException 처리
    @ExceptionHandler(GeneralException.class)
    public ResponseEntity<ApiResponse<Object>> handleCustomException(GeneralException e) {
        ErrorReasonDto reason = e.getErrorReasonHttpStatus();

        log.error("[GeneralException] code: {}, message: {}", reason.getCode(), reason.getMessage(), e);

        return new ResponseEntity<>(ApiResponse.onFailure(
                reason.getCode(), reason.getMessage(), null), reason.getHttpStatus());
    }

    // @Valid 유효성 검사 실패 처리
    @Override
    protected  ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException e, HttpHeaders headers, HttpStatusCode status, WebRequest request) {

        Map<String, String> errors = new HashMap<>();
        e.getBindingResult().getFieldErrors().forEach(err -> errors.put(err.getField(), err.getDefaultMessage()));

        log.error("[Validation Error] code: {}, errors: {}",
                ErrorStatus.VALIDATION_ERROR.getCode(), errors, e);

        return new ResponseEntity<>(ApiResponse.onFailure(ErrorStatus.VALIDATION_ERROR.getCode(), "Validation Error", errors),
                HttpStatus.BAD_REQUEST);
    }

    // DB UNIQUE 위반 등 무결성 예외 처리
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiResponse<Object>> handleDataIntegrityViolation(DataIntegrityViolationException e) {
        log.error("[IntegrityViolation] {}", e.getMessage(), e);

        // 중복 응답 전송
        if (e.getMessage() != null && e.getMessage().contains("Duplicate entry")) {
            var reason = ErrorStatus.DUPLICATE_NICKNAME.getReasonHttpStatus();
            return new ResponseEntity<>(ApiResponse.onFailure(reason.getCode(), reason.getMessage(), null), reason.getHttpStatus());
        }

        var reason = ErrorStatus.INTERNAL_SERVER_ERROR.getReasonHttpStatus();

        return new ResponseEntity<>(
                ApiResponse.onFailure(reason.getCode(), reason.getMessage(), null),
                reason.getHttpStatus()
        );
    }

    // 그 외 모든 예외 처리
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleException(Exception e) {

        // DataIntegrityViolationException 혹은 중복 메시지가 포함된 경우 강제 전환
        if ( e instanceof DataIntegrityViolationException || e.getMessage().contains("Duplicate entry")) {
            log.warn("[Handled through fallback] Duplicate entry detected in general Exception handler");
            var reason = ErrorStatus.DUPLICATE_NICKNAME.getReasonHttpStatus();
            return new ResponseEntity<>(ApiResponse.onFailure(reason.getCode(), reason.getMessage(), null), reason.getHttpStatus());
        }

        log.error("[Unhandled Exception] code: {}, message: {}",
                ErrorStatus.INTERNAL_SERVER_ERROR.getCode(), e.getMessage(), e);

        var reason = ErrorStatus.INTERNAL_SERVER_ERROR.getReasonHttpStatus();
        return new ResponseEntity<>(ApiResponse.onFailure(reason.getCode(), reason.getMessage(), null),
                reason.getHttpStatus());
    }
}
