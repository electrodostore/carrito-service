package com.electrodostore.carrito_service.exception;

import lombok.Getter;

/**
 * Excepción usada cuando se quiere hacer
 * una operación sobre un carrito comprado.
 * */
@Getter
public class CarritoPurchasedException extends BusinessException {
    private final CarritoErrorCode errorCode;

    public CarritoPurchasedException(String message) {
        super(message);
        this.errorCode = CarritoErrorCode.STATUS_PURCHASED_CARRITO;
    }
}
