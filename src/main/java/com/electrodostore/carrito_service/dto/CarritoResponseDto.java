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

@Getter  @Setter
@AllArgsConstructor
@NoArgsConstructor
//Clase DTO para exponer los datos del carrito de un determinado cliente, asi como los productos que hay en él
public class CarritoResponseDto {

    private Long id;
    private BigDecimal total;
    private List<ProductoResponseDto> listProductos = new ArrayList<>();
    private ClienteResponseDto cliente;
    private CarritoStatus status;
}
