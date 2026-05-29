package com.electrodostore.carrito_service.dto;

import com.electrodostore.carrito_service.model.CarritoStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//DTO encargado de notificarle al cliente el ID y estado del carrito cuando este crea uno
public record CarritoCreadoResponseDto(
        Long carritoId,
        CarritoStatus status
) {}
