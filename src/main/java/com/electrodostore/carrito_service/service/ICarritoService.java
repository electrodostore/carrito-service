package com.electrodostore.carrito_service.service;

import com.electrodostore.carrito_service.dto.CarritoCreadoResponseDto;
import com.electrodostore.carrito_service.dto.CarritoResponseDto;
import com.electrodostore.carrito_service.dto.ProductoAgregarDto;
import com.electrodostore.carrito_service.dto.ProductoCambiarCantidadDto;
import com.electrodostore.carrito_service.integration.venta.dto.VentaIntegrationResponseDto;

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

    //Método que se encarga de cambiar la cantidad que se quería comprar de un producto por una nueva en un determinado carrito
    CarritoResponseDto cambiarCantidadProducto(Long carritoId, ProductoCambiarCantidadDto productoNuevaCantidad);

    //Método para finalmente, después de todo el proceso, se compré el carrito y se registré como venta en venta-service
    VentaIntegrationResponseDto comprarCarrito(Long carritoId);
}
