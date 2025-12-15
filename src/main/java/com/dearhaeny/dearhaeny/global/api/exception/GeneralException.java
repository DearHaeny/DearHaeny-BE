package com.dearhaeny.dearhaeny.global.api.exception;

import com.dearhaeny.dearhaeny.global.api.code.BaseErrorCode;
import com.dearhaeny.dearhaeny.global.api.dto.ErrorReasonDto;
import lombok.Getter;

@Getter
public class GeneralException extends RuntimeException {

    private final BaseErrorCode errorCode;
    private final String errorMessage;

    // 코드의 기본 메시지를 RuntimeException에 올려서 Null 방지
    public GeneralException(BaseErrorCode errorCode) {
        super(errorCode.getReason().getMessage());
        this.errorCode = errorCode;
        this.errorMessage = errorCode.getReason().getMessage();
    }

    public GeneralException(BaseErrorCode errorCode, String errorMessage) {
        super(buildMessage(errorCode, errorMessage));
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public GeneralException(BaseErrorCode errorCode, String errorMessage, Throwable cause) {
        super(buildMessage(errorCode, errorMessage), cause);
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    private static String buildMessage(BaseErrorCode errorCode, String errorMessage) {
        String base = errorCode.getReason().getMessage();
        return (errorMessage == null || errorMessage.isBlank()) ? base : base + ": " + errorMessage;
    }

    public ErrorReasonDto getErrorReason() {
        return this.errorCode.getReason();
    }

    public ErrorReasonDto getErrorReasonHttpStatus() {
        return this.errorCode.getReasonHttpStatus();
    }
}
