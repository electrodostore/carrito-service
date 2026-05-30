package com.electrodostore.carrito_service.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

/**
 * Transporta los datos de un producto cuando se
 * desea cambiar la cantidad agregada al carrito.
 */
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

