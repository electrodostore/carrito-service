package com.electrodostore.carrito_service.exception;

//Excepción personalizada para indicar problemas de comunicación entre servicios (errores de infraestructura)
public class ServiceUnavailable extends RuntimeException {

    /*Llamamos al constructor de la clase padre RuntimeException y esta va pasando por las clases de las que hereda hasta llegar
    a Throwable que me expone el mensaje por medio del método getMessage()*/
    public ServiceUnavailable(String message) {
        super(message);
    }
}
