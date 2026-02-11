package com.electrodostore.carrito_service.dto;

import com.electrodostore.carrito_service.model.CarritoStatus;
import com.electrodostore.carrito_service.model.ClienteSnapshot;
import com.electrodostore.carrito_service.model.ProductoSnapshot;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

//Clase DTO para exponer los datos del carrito de un determinado cliente, asi como los productos que hay en él
public class CarritoResponseDto {

    private Long id;
    private List<ProductoResponseDto> listProductos = new ArrayList<>();
    private ClienteResponseDto cliente;
    private CarritoStatus status;
}
