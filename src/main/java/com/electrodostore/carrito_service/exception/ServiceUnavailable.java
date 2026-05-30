package com.electrodostore.carrito_service.exception;

import lombok.Getter;

/**
 * Excepción que comunica un error de infraestructura en
 * la integración con los microservicios.
 */
@Getter
public class ServiceUnavailable extends RuntimeException {
    private final CarritoErrorCode errorCode;

    public ServiceUnavailable(String message) {
        super(message);
        this.errorCode = CarritoErrorCode.SERVICE_UNAVAILABLE;

    }
}
