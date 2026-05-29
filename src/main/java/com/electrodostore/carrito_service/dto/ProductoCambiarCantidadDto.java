package com.electrodostore.carrito_service.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

//DTO que se encarga de almacenar el ID de un producto que se le va a modificar la cantidad comprada y la nueva cantidad
public record ProductoCambiarCantidadDto (
        @NotNull Long productId,
        @NotNull @PositiveOrZero Integer newQuantity

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

