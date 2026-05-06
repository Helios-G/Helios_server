package com.helios.user.exception;

import com.helios.common.exception.BusinessException;
import com.helios.common.exception.ErrorCode;

public class InvalidPasswordException extends BusinessException {
    public InvalidPasswordException() {
        super(ErrorCode.INVALID_PASSWORD);
    }
}