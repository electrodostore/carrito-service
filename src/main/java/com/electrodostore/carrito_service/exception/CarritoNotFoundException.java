package com.electrodostore.carrito_service.exception;

//Excepción personalizada para cuando no se encuentre un determinado carrito buscado por el ID
public class CarritoNotFoundException extends BusinessException {

    //El mensaje de la excepción se sube a la clase padre RuntimeException para que esta los exponga con el método getMessage()
    public CarritoNotFoundException(String message) {
        super(message);
    }
}
