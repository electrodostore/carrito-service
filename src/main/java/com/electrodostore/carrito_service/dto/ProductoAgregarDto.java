package com.electrodostore.carrito_service.dto;

//DTO que transporta los datos necesarios para agregar un producto al carrito
public record ProductoAgregarDto(
        Long id,
        Integer quantity
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
