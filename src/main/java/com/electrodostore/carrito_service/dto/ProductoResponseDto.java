package com.electrodostore.carrito_service.dto;

import java.math.BigDecimal;

//Clase DTO para exponer los datos de cada producto perteneciente a un carrito
public record ProductoResponseDto(
        Long productId,
        String productName,
        BigDecimal productPrice,
        //Cantidad comprada del producto
        Integer purchasedQuantity,
        //Subtotal = precio * productQuantity
        BigDecimal subTotal,
        String productDescription
) {}
