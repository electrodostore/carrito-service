package com.electrodostore.carrito_service.exception;

import lombok.Getter;

@Getter
public class ClienteNotFoundException extends BusinessException {
    private final CarritoErrorCode errorCode;

    public ClienteNotFoundException(String message) {
        super(message);
        this.errorCode = CarritoErrorCode.CLIENT_NOT_FOUND;
    }
}
