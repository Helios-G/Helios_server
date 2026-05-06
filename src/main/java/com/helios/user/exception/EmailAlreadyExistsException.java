package com.helios.user.exception;

import com.helios.common.exception.BusinessException;
import com.helios.common.exception.ErrorCode;

public class EmailAlreadyExistsException extends BusinessException {
    public EmailAlreadyExistsException() {
        super(ErrorCode.EMAIL_ALREADY_EXISTS);
    }
}