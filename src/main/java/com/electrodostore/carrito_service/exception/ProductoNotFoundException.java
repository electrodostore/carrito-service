package com.electrodostore.carrito_service.exception;

//Excepción personalizada del dominio para cuando no se encuentre un producto consultado en producto-service
public class ProductoNotFoundException extends RuntimeException {

    //Subimos mensaje para tenerlo disponible en el método getMessage()
    public ProductoNotFoundException(String message) {
        super(message);
    }
}
