package com.electrodostore.carrito_service.integration.venta.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter  @Setter
@AllArgsConstructor
@NoArgsConstructor
//DTO que se usará mara almacenar los datos de una venta que se van a transferir a venta-service para registrarla cuando se compre un carrito
public class VentaIntegrationRequestDto {

    private LocalDate date;
    //Lista de cada id y cantidad del producto en el carrito que se registrará en la venta
    private List<ProductoIntegrationRequestDto> productsList = new ArrayList<>();
    //Id del cliente dueño de la venta
    private Long clientId;
}
