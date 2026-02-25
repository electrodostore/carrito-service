package com.electrodostore.carrito_service.service;

import com.electrodostore.carrito_service.dto.CarritoCreadoResponseDto;
import com.electrodostore.carrito_service.dto.CarritoResponseDto;
import com.electrodostore.carrito_service.dto.ProductoAgregarDto;

import java.util.List;

//Interfaz donde se van a declarar todas las operaciones de este dominio (carrito)
public interface ICarritoService {

    //Método para consultar todos los registros que tenemos de los carritos
    List<CarritoResponseDto> findAllCarritos();

    //Método para consultar un carrito por su id
    CarritoResponseDto findCarritoResponse(Long carritoId);

    //Crear un carrito asignado a un determinado cliente
    CarritoCreadoResponseDto crearCarrito(Long clienteId);

    //Método para agregar uno o varios productos a un determinado carrito
    CarritoResponseDto agregarProductos(Long carritoId, List<ProductoAgregarDto> productosAgregar);

    //Método para eliminar uno o varios productos de un determinado carrito
    CarritoResponseDto deleteProductos(Long carritoId, Long productoEliminarId);
}
