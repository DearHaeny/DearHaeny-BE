package com.dearhaeny.dearhaeny.global.api.code;

import com.dearhaeny.dearhaeny.global.api.dto.ErrorReasonDto;

public interface BaseErrorCode {

    ErrorReasonDto getReason();
    ErrorReasonDto getReasonHttpStatus();

}