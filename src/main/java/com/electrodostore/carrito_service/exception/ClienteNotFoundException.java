package com.electrodostore.carrito_service.exception;

//Excepción personalizada para este dominio para cuando no se logre encontrar un Cliente consultado al servicio Ciente
public class ClienteNotFoundException extends RuntimeException {

    //Subimos mensaje para tenerlo disponible con el método getMessage()
    public ClienteNotFoundException(String message) {
        super(message);
    }
}
