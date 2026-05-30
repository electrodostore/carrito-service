package com.electrodostore.carrito_service.integration.venta.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter  @Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductoIntegrationRequestDto {

    private Long id;
    private Integer quantity;
}
