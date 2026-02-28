package com.electrodostore.carrito_service.exception;

//Enum encargado de encapsular y guardar el conjunto de valores de errorCodes que identifican a cada excepción de negocio fuera de este dominio
/*También podemos usar estos valores de errorCodes para compararlos con un errorCode que venga en una response de error y
 así interpretar y transformar esa response a la excepción de dominio correspondiente*/
public enum CarritoErrorCode {

    CARRITO_NOT_FOUND,  //ErrorCode de CarritoNotFoundException
    CLIENT_NOT_FOUND,   //ErrorCode de ClienteNotFoundException
    PRODUCT_NOT_FOUND,  //ErrorCode de ProductoNotFound
    PRODUCT_STOCK_INSUFICIENTE,  //ErrorCode de ProductoStockInsuficienteException
    STATUS_PURCHASED_CARRITO,   //ErrorCode de CarritoPurchasedException
    SERVICE_UNAVAILABLE  //ErrorCode de ServiceUnavailable
}
