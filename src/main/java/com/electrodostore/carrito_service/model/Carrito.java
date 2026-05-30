package com.electrodostore.carrito_service.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Getter  @Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Carrito {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;
    private BigDecimal total = BigDecimal.ZERO;
    /*Colección de Snapshot de productos almacenados en una
    * tabla secundaria asociada al carrito.
    *
    * El Snapshot contiene el estado actual del producto al
    * momento de agregarse al carrito.*/
    @ElementCollection
    @CollectionTable(
            name = "carrito_productos",
            joinColumns = @JoinColumn(name = "carrito_id")
    )
    private Set<ProductoSnapshot> listProductos = new HashSet<>();


    //Snapshot del cliente propietario del carrito.
    @Embedded
    private ClienteSnapshot cliente;

    @Enumerated(EnumType.STRING)
    private CarritoStatus status;
}
