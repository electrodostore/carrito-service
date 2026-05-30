package com.electrodostore.carrito_service.service;

import com.electrodostore.carrito_service.dto.CarritoResponseDto;
import com.electrodostore.carrito_service.dto.ProductoAgregarDto;
import com.electrodostore.carrito_service.dto.ProductoCambiarCantidadDto;
import com.electrodostore.carrito_service.integration.venta.dto.VentaIntegrationResponseDto;
import com.electrodostore.carrito_service.model.Carrito;

import java.util.List;

public interface ICarritoService {

    List<CarritoResponseDto> findAllCarritos();

    CarritoResponseDto findCarritoResponse(Long carritoId);

    /**
     * Expone el carrito pendiente del cliente autenticado
     */
    CarritoResponseDto findMyCarritoPending();

    /**
     * Crea carrito asignado al cliente autenticado
     */
    Carrito crearCarrito();

    /**
     * Agrega uno o varios productos al
     * carrito con estado pendiente del cliente autenticado
     */
    CarritoResponseDto agregarProductos(List<ProductoAgregarDto> productosAgregar);

    /**
     * Elimina un producto del carrito pendiente
     * del cliente autenticado
     */
    CarritoResponseDto deleteProductos(Long productoEliminarId);

    /**
     * Actualiza la cantidad de un producto
     * en el carrito pendiente del cliente autenticado.
     */
    CarritoResponseDto cambiarCantidadProducto(ProductoCambiarCantidadDto productoNuevaCantidad);

    /**
     * Compra carrito pendiente del cliente autenticado
     * y registra la venta en venta-service
    */
    VentaIntegrationResponseDto comprarCarrito();

    /**
     * Elimina todos los productos del carrito
     * pendiente del cliente autenticado.
     */
    void vaciarMiCarrito();
}
