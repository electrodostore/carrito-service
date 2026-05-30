package com.electrodostore.carrito_service.dto;

/**
 * Contrato común para DTOs de productos que contienen
 * una referencia al producto y una cantidad asociada,
 * permitiendo reutilizar lógica independiente del
 * contexto de cada DTO.
 * */
public interface ProductoConCantidadDto {

    Long getProductoId();
    Integer getCantidad();
}
