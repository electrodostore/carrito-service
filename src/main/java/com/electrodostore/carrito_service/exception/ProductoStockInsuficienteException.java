package com.electrodostore.carrito_service.exception;

import lombok.Getter;

/**
 * Excepción personalizada para indicar
 * que el stock de un determinado producto es insuficiente
 */
@Getter
public class ProductoStockInsuficienteException extends BusinessException{
    private final CarritoErrorCode errorCode;

    public ProductoStockInsuficienteException(String message) {
        super(message);
        this.errorCode = CarritoErrorCode.PRODUCT_STOCK_INSUFICIENTE;
    }
}
