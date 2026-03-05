package com.electrodostore.carrito_service.dto;

/*Interfaz que me identifica los DTO que comparten estructura de atributos (productoId y cantidad), pero se usan para diferentes fines
 (Agregar un producto a un determinado carrito, cambiar la cantidad de un producto, etc.)*/
//Todos estos DTO van a implementar esta interfaz para tener un punto de referencia por medio del cual identificarlos
public interface ProductoConCantidadDto {

    Long getProductoId();
    Integer getCantidad();
}
