package com.electrodostore.carrito_service.service;

import com.electrodostore.carrito_service.dto.CarritoResponseDto;
import com.electrodostore.carrito_service.dto.ProductoAgregarDto;
import com.electrodostore.carrito_service.dto.ProductoCambiarCantidadDto;
import com.electrodostore.carrito_service.integration.venta.dto.VentaIntegrationResponseDto;
import com.electrodostore.carrito_service.model.Carrito;

import java.util.List;

//Interfaz donde se van a declarar todas las operaciones de este dominio (carrito)
public interface ICarritoService {

    //Método para consultar todos los registros que tenemos de los carritos
    List<CarritoResponseDto> findAllCarritos();

    //Método para consultar un carrito por su id
    CarritoResponseDto findCarritoResponse(Long carritoId);

    //Expone el carrito pendiente del cliente autenticado
    CarritoResponseDto findMyCarritoPending();

    //Crear carrito asignado al cliente autenticado
    Carrito crearCarrito();

    /**
     * Método para agregar uno o varios productos al
     * carrito con estado pendiente del cliente autenticado
     */
    CarritoResponseDto agregarProductos(List<ProductoAgregarDto> productosAgregar);

    /**
     * Método para eliminar un producto del
     * carrito con estado pendiente del cliente autenticado
     */
    CarritoResponseDto deleteProductos(Long productoEliminarId);

    /**
     * Método que se encarga de cambiar la cantidad que se había
     * establecido comprar de un producto por una nueva en
     * el carrito pendiente del cliente autenticado
     */
    CarritoResponseDto cambiarCantidadProducto(ProductoCambiarCantidadDto productoNuevaCantidad);

    /**
     * Compra carrito pendiente del cliente autenticado
     * y registra la venta en venta-service
    */
    VentaIntegrationResponseDto comprarCarrito();
}
