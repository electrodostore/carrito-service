package com.electrodostore.carrito_service.exception;

import lombok.Getter;

@Getter  //Se exponen campo(s)
//Excepción personalizada para cuando el stock de un determinado producto consultado es insuficiente
public class ProductoStockInsuficienteException extends BusinessException{

    //ErrorCode que identifica esta excepción fuera del dominio carrito
    private final CarritoErrorCode errorCode;

    public ProductoStockInsuficienteException(String message) {
        super(message);

        //Le damos el respectivo valor de errorCode a la excepción
        this.errorCode = CarritoErrorCode.PRODUCT_STOCK_INSUFICIENTE;
    }
}
