package com.dearhaeny.dearhaeny.global.api.exception;

import com.dearhaeny.dearhaeny.global.api.code.ErrorStatus;

public class NotFoundException extends GeneralException {

    public NotFoundException() {
        super(ErrorStatus.NOT_FOUND);
    }

    public NotFoundException(String message) {
        super(ErrorStatus.NOT_FOUND, message);
    }
}
