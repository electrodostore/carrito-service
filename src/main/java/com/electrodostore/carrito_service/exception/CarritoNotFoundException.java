package com.electrodostore.carrito_service.exception;

import lombok.Getter;

@Getter  //Se exponen campo(s)
//Excepción personalizada para cuando no se encuentre un determinado carrito buscado por el ID
public class CarritoNotFoundException extends BusinessException {

    //ErrorCode que identifica esta excepción fuera del dominio carrito
    private final CarritoErrorCode errorCode;

    //El mensaje de la excepción se sube a la clase padre RuntimeException para que esta los exponga con el método getMessage()
    public CarritoNotFoundException(String message) {
        super(message);

        //Se le da el respectivo valor del conjunto de valores en el enum CarritoErrorCode
        this.errorCode = CarritoErrorCode.CARRITO_NOT_FOUND;
    }
}
