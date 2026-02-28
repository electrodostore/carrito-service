package com.electrodostore.carrito_service.exception;

import lombok.Getter;

@Getter  //Exponemos campo(s)
//Excepción de dominio para cuando se quiera realizar una operación sobre un carrito y el estado de este sea PURCHASED (comprado)
public class CarritoPurchasedException extends BusinessException {

    //ErrorCode identificativa de esta excepción fuera del dominio carrito
    private final CarritoErrorCode errorCode;

    //Subimos el mensaje de la excepción pasando por RuntimeException hasta llegar a Throwable el cual me expone el mensaje por medio de getMessage()
    public CarritoPurchasedException(String message) {
        super(message);

        //Le asignamos el correspondiente errorCode del conjunto de valores en el enum CarritoErrorCode
        this.errorCode = CarritoErrorCode.PURCHASED_CARRITO_STATUS;
    }
}
