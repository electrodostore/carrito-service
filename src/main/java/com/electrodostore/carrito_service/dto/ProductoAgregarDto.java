package com.electrodostore.carrito_service.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

/**
 * Transporta los datos de los productos cuando
 * se desean agregar a un carrito
 * */
public record ProductoAgregarDto(
        @NotNull Long id,
        @NotNull @PositiveOrZero Integer quantity
) implements ProductoConCantidadDto {

    @Override
    public Long getProductoId() {
        return id;
    }

    @Override
    public Integer getCantidad() {
        return quantity;
    }
}

