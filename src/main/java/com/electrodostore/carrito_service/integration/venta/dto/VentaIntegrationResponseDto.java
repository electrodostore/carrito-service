package com.electrodostore.carrito_service.integration.venta.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Almacena el identificador de la venta creada en venta-service
 */
@Getter  @Setter
@AllArgsConstructor
@NoArgsConstructor
public class VentaIntegrationResponseDto {

    private Long ventaId;
}
