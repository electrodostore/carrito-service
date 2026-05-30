package com.electrodostore.carrito_service.dto;

public record ClienteResponseDto(
        Long id,
        String name,
        String cellphone,
        String document,
        String address
) {}
