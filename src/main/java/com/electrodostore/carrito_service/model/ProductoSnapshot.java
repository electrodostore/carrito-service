package com.electrodostore.carrito_service.model;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.math.BigDecimal;

@Getter  @Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode //Métodos Equals y HashCode que Hibernate utiliza para evitar duplicidad de objetos Snapshot
/*Clase Embeddable usada para sacar objetos embebidos cuya identidad es copiada del registro original del producto en
 producto-service y el estado de cada producto será el que tenga al momento de agregarse al carrito*/
@Embeddable
public class ProductoSnapshot {

    /*Referencia (productId) a la identidad del producto original, y por la que se comparan los Snapshot
    embebidos para evitar duplicidad*/
    @EqualsAndHashCode.Include
    private Long productId;
    private String productName;
    private BigDecimal productPrice;
    //Cantidad comprada del producto
    private Integer purchasedQuantity;
    //Subtotal = precio * productQuantity
    private BigDecimal subTotal;
    private String productDescription;
}
