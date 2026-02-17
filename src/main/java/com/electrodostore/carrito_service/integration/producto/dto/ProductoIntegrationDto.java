package com.electrodostore.carrito_service.integration.producto.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter  @Setter
@AllArgsConstructor
@NoArgsConstructor
//DTO donde se van a almacenar los datos de los productos de un carrito una vez se haga la integración
public class ProductoIntegrationDto {

    private Long id;
    private String name;
    private Integer stock;
    private BigDecimal price;
    private String description;
}
