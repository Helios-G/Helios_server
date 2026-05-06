// exception/ModelNotFoundException.java
package com.helios.modelmgmt.exception;

import com.helios.common.exception.BusinessException;
import com.helios.common.exception.ErrorCode;

public class ModelNotFoundException extends BusinessException {
    public ModelNotFoundException(Long id) {
        super(ErrorCode.MODEL_NOT_FOUND, "해당 모델 정보를 찾을 수 없습니다. id=" + id);
    }
}