package com.electrodostore.carrito_service.exception;

import lombok.Getter;

@Getter //Se exponen campo(s)
//Excepción personalizada del dominio para cuando no se encuentre un producto consultado en producto-service
public class ProductoNotFoundException extends BusinessException {

    //ErrorCode identificativa de esta excepción fuera del dominio
    private final CarritoErrorCode errorCode;

    //Subimos mensaje para tenerlo disponible en el método getMessage()
    public ProductoNotFoundException(String message) {
        super(message);

        //Se le da el respectivo valor al errorCode de la excepción
        this.errorCode = CarritoErrorCode.PRODUCT_NOT_FOUND;
    }
}
