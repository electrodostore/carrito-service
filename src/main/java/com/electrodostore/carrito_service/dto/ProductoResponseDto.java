package com.electrodostore.carrito_service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter  @Setter
@AllArgsConstructor
@NoArgsConstructor
//Clase DTO para exponer los datos de cada producto perteneciente a un carrito
public class ProductoResponseDto {

    private Long productId;
    private String productName;
    private BigDecimal productPrice;
    //Cantidad comprada del producto
    private Integer purchasedQuantity;
    //Subtotal = precio * productQuantity
    private BigDecimal subTotal;
    private String productDescription;
}
