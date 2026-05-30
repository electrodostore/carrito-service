package com.electrodostore.carrito_service.integration.producto.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Transporta datos en la integración con producto-service
 * cuando se desea hacer una operación con el stock de un producto.
 * */
@Getter  @Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductoIntegrationStockDto {

    private Long productoId;  //Id del producto que tiene el stock sobre el que se le va a hacer la operación
    private Integer cantidadOperar;  //Cantidad con respecto a la cual se va a operar
}
