package com.electrodostore.carrito_service.model;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.math.BigDecimal;

/**
 * Snapshot de un producto proveniente de producto-service.
 *
 * Conserva los datos relevantes del producto al momento de
 * agregarlo al carrito para evitar depender de cambios
 * posteriores en producto-service.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Embeddable
public class ProductoSnapshot {

    /**
     * Identificador del producto original.
     * Se utiliza en equals() y hashCode() para evitar
     * productos duplicados dentro del carrito.
     */
    @EqualsAndHashCode.Include
    private Long productId;

    private String productName;
    private BigDecimal productPrice;
    private Integer purchasedQuantity;
    private BigDecimal subTotal;
    private String productDescription;
}
