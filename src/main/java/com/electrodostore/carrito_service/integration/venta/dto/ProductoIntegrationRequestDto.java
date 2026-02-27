package com.electrodostore.carrito_service.integration.venta.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter  @Setter
@AllArgsConstructor
@NoArgsConstructor
//DTO que se usará para almacenar los datos que se van a transferir a los productos que se registrán en la venta una vez se decida comprar el carrito
public class ProductoIntegrationRequestDto {

    private Long id;
    private Integer quantity;
}
