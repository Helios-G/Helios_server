package com.helios.session.exception;

import com.helios.common.exception.BusinessException;
import com.helios.common.exception.ErrorCode;

public class SessionNotFoundException extends BusinessException {
    public SessionNotFoundException(Long sessionId) {
        super(ErrorCode.SESSION_NOT_FOUND, "세션을 찾을 수 없습니다. id=" + sessionId);
    }
}