package com.electrodostore.carrito_service.integration.producto.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter  @Setter
@AllArgsConstructor
@NoArgsConstructor
/*DTO que almacena los datos que viajarán en la petición a producto-service para hacer una operación (validar, reponer,
 descontar) sobre el stock de un producto */
public class ProductoIntegrationStockDto {

    private Long productoId;  //Id del producto que tiene el stock sobre el que se le va a hacer la operación
    private Integer cantidadOperar;  //Cantidad con respecto a la cual se va a operar
}
