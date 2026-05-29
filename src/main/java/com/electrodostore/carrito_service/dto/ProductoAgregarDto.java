package com.electrodostore.carrito_service.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

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

