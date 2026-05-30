package com.electrodostore.carrito_service.exception;

import lombok.Getter;

@Getter
public class ProductoNotFoundException extends BusinessException {
    private final CarritoErrorCode errorCode;

    public ProductoNotFoundException(String message) {
        super(message);
        this.errorCode = CarritoErrorCode.PRODUCT_NOT_FOUND;
    }
}
