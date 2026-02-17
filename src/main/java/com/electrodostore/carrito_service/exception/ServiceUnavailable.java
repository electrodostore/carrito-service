package com.electrodostore.carrito_service.exception;

import lombok.Getter;

@Getter  //Se exponen campo(s)
//Excepción personalizada para indicar problemas de comunicación entre servicios (errores de infraestructura)
public class ServiceUnavailable extends RuntimeException {

    //ErrorCode que identifica a esta excepción fuera del dominio
    private final CarritoErrorCode errorCode;

    /*Llamamos al constructor de la clase padre RuntimeException y esta va pasando por las clases de las que hereda hasta llegar
    a Throwable que me expone el mensaje por medio del método getMessage()*/
    public ServiceUnavailable(String message) {
        super(message);

        //Se le da el respectivo valor al errorCode de la excepción
        this.errorCode = CarritoErrorCode.SERVICE_UNAVAILABLE;

    }
}
