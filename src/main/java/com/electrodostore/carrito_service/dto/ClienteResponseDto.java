package com.electrodostore.carrito_service.dto;

//Clase DTO para exponer los datos del cliente dueño de un determinado carrito al cliente del servicio
public record ClienteResponseDto(
        Long id,
        String name,
        String cellphone,
        String document,
        String address
) {}
