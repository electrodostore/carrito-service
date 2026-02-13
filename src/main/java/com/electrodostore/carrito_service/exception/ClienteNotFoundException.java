package com.electrodostore.carrito_service.exception;

import lombok.Getter;

@Getter //Exponemos campo(s)
//Excepción personalizada para este dominio para cuando no se logre encontrar un Cliente consultado al servicio Ciente
public class ClienteNotFoundException extends RuntimeException {

    //ErrorCode identificativo para esta excepción fuera del dominio
    private final CarritoErrorCode errorCode;

    //Subimos mensaje para tenerlo disponible con el método getMessage()
    public ClienteNotFoundException(String message) {
        super(message);

        //Se le asigna el valor correspondiente del conjunto de valores guardados en CarritoErrorCode
        this.errorCode = CarritoErrorCode.CLIENT_NOT_FOUND;
    }
}
