package com.electrodostore.carrito_service.exception;

/**
 * SuperExcepción personalizada para identificar a las excepciones
 * conocidas de dominio.
 */
public class BusinessException extends RuntimeException {

    public BusinessException(String message) {
        super(message);
    }
}
