package com.electrodostore.carrito_service.model;

//Enum que define el conjunto de valores que puede tener el estado de un carrito una vez creado
public enum CarritoStatus {

    //Por ahora el carrito solo podrá estar en estado pendiente (PENDING) o comprado (PURCHASED)
    PENDING,
    PURCHASED
}
