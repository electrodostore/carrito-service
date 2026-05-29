package com.electrodostore.carrito_service.dto;

import com.electrodostore.carrito_service.model.CarritoStatus;
import com.electrodostore.carrito_service.model.ClienteSnapshot;
import com.electrodostore.carrito_service.model.ProductoSnapshot;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

//Clase DTO para exponer los datos del carrito de un determinado cliente, asi como los productos que hay en él
public record CarritoResponseDto(
        Long id,
        BigDecimal total,
        List<ProductoResponseDto> listProductos,
        ClienteResponseDto cliente,
        CarritoStatus status
) {}
