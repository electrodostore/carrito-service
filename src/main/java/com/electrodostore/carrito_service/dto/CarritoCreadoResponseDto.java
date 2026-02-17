package com.electrodostore.carrito_service.dto;

import com.electrodostore.carrito_service.model.CarritoStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter  @Setter
@AllArgsConstructor
@NoArgsConstructor
//DTO encargado de notificarle al cliente el ID y estado del carrito cuando este crea uno
public class CarritoCreadoResponseDto {

    //Datos que se van a exponer del carrito una vez se cree
    private Long carritoId;
    private CarritoStatus status;
}
