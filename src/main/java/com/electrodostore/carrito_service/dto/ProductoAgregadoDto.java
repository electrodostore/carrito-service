package com.electrodostore.carrito_service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter  @Setter
@AllArgsConstructor
@NoArgsConstructor
//DTO que almacena el ID del producto y la cantidad que se quiere comprar de este cuando se agregue un producto al carrito
public class ProductoAgregadoDto {

    private Long id; //id del producto a agregar
    private int quantity; //Cantidad que se quiere comprar

}
