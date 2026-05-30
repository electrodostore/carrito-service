package com.electrodostore.carrito_service.dto;

import java.math.BigDecimal;

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
