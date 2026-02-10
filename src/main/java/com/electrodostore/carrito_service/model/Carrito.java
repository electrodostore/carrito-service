package com.electrodostore.carrito_service.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

//Entidad carrito donde se va a registrar cada carrito creado por un determinado cliente
@Entity
public class Carrito {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    /*@ElementCollection crea una tabla secundaria donde se relaciona la colección de los diferentes objetos embebidos de
    ProductoSnapshot con el carrito que los contiene*/
    @ElementCollection
    @CollectionTable(
            //Le definimos nombre a la tabla secundaria
            name = "carrito_productos",
            //Definimos la referencia al servicio carrito
            joinColumns = @JoinColumn(name = "carrito_id")
    )
    private Set<ProductoSnapshot> listProductos = new HashSet<>();

    //Objeto embebido que hace referencia al cliente dueño del carrito
    @Embedded
    private ClienteSnapshot cliente;
}
