package com.helios.user.exception;

import com.helios.common.exception.BusinessException;
import com.helios.common.exception.ErrorCode;

public class UserNotFoundException extends BusinessException {
    public UserNotFoundException() {
        super(ErrorCode.USER_NOT_FOUND);
    }
    public UserNotFoundException(String email) {
        super(ErrorCode.USER_NOT_FOUND, "해당 이메일의 사용자를 찾을 수 없습니다. email=" + email);
    }
}