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

    //Definimos el estado en que se encuentra el carrito
    /*La annotation @Enumerated define la forma en que se guarda este enum en la base de datos.
    * El parámetro EnumType.STRING define que el valor del enum será guardado como String.
    * NOTA: Por defecto hibernate usa EnumType.ORDINAL, el cual guarda el enum como número. Por ejemplo, el primer valor
    * definido en el enum = 0, el segundo = 1 y así´sucesivamente. Esto es frágil y se considera una mala práctica por lo que
    * siempre es importante definir EnumType.STRING*/
    @Enumerated(EnumType.STRING)
    private CarritoStatus status;
}
