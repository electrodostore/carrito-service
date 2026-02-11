package com.electrodostore.carrito_service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter  @Setter
@AllArgsConstructor
@NoArgsConstructor
//Clase DTO para transferir los datos de los diferentes productos agregados a un determinado carrito
public class ProductoDeCarritoDto {

    //Id del producto a agregar
    private Long id;
    //Cantidad que se va a comprar
    private Long quantity;
}
