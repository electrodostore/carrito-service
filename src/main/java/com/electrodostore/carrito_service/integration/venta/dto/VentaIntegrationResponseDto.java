package com.electrodostore.carrito_service.integration.venta.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter  @Setter
@AllArgsConstructor
@NoArgsConstructor
//DTO encargado de almacenar la respuesta de venta-service cuando se registra una venta (ID de la venta que se registró)
public class VentaIntegrationResponseDto {

    private Long ventaId;
}
