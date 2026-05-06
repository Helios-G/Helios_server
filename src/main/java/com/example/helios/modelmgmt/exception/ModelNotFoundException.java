// exception/ModelNotFoundException.java
package com.example.helios.modelmgmt.exception;

public class ModelNotFoundException extends RuntimeException {
    public ModelNotFoundException(Long id) {
        super("해당 모델 정보를 찾을 수 없습니다. id=" + id);
    }
}