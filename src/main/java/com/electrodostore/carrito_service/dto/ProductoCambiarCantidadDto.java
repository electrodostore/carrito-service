package com.electrodostore.carrito_service.dto;

//DTO que se encarga de almacenar el ID de un producto que se le va a modificar la cantidad comprada y la nueva cantidad
public record ProductoCambiarCantidadDto (
        Long productId,
        Integer newQuantity

) implements ProductoConCantidadDto{

    @Override
    public Long getProductoId() {
        return productId;
    }

    @Override
    public Integer getCantidad() {
        return newQuantity;
    }
}
