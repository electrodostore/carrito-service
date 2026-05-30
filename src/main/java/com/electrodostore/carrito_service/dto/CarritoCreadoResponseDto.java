package com.electrodostore.carrito_service.dto;

import com.electrodostore.carrito_service.model.CarritoStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public record CarritoCreadoResponseDto(
        Long carritoId,
        CarritoStatus status
) {}
