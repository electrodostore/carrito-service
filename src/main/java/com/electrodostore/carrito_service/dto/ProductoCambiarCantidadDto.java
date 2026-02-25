package com.electrodostore.carrito_service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter  @Setter
@AllArgsConstructor
@NoArgsConstructor
//DTO que se encarga de almacenar el ID de un producto que se le va a modificar la cantidad comprada y la nueva cantidad
public class ProductoCambiarCantidadDto {

    private Long productId;
    private Integer newQuantity;

}
