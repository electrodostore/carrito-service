package com.electrodostore.carrito_service.exception;

import lombok.Getter;

@Getter
public class CarritoNotFoundException extends BusinessException {
    private final CarritoErrorCode errorCode;

    public CarritoNotFoundException(String message) {
        super(message);
        this.errorCode = CarritoErrorCode.CARRITO_NOT_FOUND;
    }
}
