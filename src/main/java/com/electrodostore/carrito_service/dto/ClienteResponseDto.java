package com.electrodostore.carrito_service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter  @Setter
@AllArgsConstructor
@NoArgsConstructor
//Clase DTO para exponer los datos del cliente dueño de un determinado carrito al cliente del servicio
public class ClienteResponseDto {

    //Datos que se van a exponer del cliente
    private Long id;
    private String name;
    private String cellphone;
    private String document;
    private String address;
}
