package com.electrodostore.carrito_service.integration.producto;

import com.electrodostore.carrito_service.exception.BusinessException;
import com.electrodostore.carrito_service.exception.ServiceUnavailable;
import com.electrodostore.carrito_service.integration.producto.client.ProductoFeignClient;
import com.electrodostore.carrito_service.integration.producto.dto.ProductoIntegrationDto;
import com.electrodostore.carrito_service.integration.producto.dto.ProductoIntegrationStockDto;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.ListResourceBundle;

/**
 * Define métodos protegidos por Circuit Breaker
 * para las integraciones con producto-service
 * realizadas mediante un cliente Feign
 */
@Slf4j
@Service
public class ProductoIntegrationService {

    private final ProductoFeignClient productoClient;

    public ProductoIntegrationService(ProductoFeignClient productoClient) {
        this.productoClient = productoClient;
    }

    /**
     * Consulta una lista de productos en producto-service
     * protegida con Circuit Breaker y Retry
     */
    @CircuitBreaker(name = "producto-service", fallbackMethod = "fallbackFindProductos")
    @Retry(name = "producto-service")
    public List<ProductoIntegrationDto> findProductos(List<Long> productosIds) {
        return productoClient.findProductos(new ArrayList<>(productosIds));
    }

    public List<ProductoIntegrationDto> fallbackFindProductos(List<Long> productosIds, Throwable ex){

      //Propaga excepciones de dominio sin modificaciones
        if(ex instanceof BusinessException be){
            throw be;
        }

        /*Indica el error cuando se tiene una excepción de infraestructura
          en la comunicación*/
        log.warn("fallback activado en la consulta de varios productos a producto-service", ex);
        throw new ServiceUnavailable("Error en la comunicación con producto-service. Por favor intente de nuevo más tarde");
    }

    /**
     * Protege integración al consultar los datos de un producto
     */
    @CircuitBreaker(name = "producto-service", fallbackMethod = "fallbackFindProducto")
    @Retry(name = "producto-service")
    public ProductoIntegrationDto findProducto(Long productoId){
        return productoClient.findProducto(productoId);
    }

    public ProductoIntegrationDto fallbackFindProducto(Long productoId, Throwable ex){

        //Propaga excepciones del negocio sin modificaciones
        if(ex instanceof BusinessException be){
            throw be;
        }

        //Indica el error de infraestructura en la comunicación
        log.warn("Fallback activado en la comunicación con producto-service. productoId={}", productoId, ex);
        throw new ServiceUnavailable("No fue posible establecer la comunicación con producto-service. Intente de nuevo más tarde");
    }

    /**
     * Protege integración cuando se verifica el stock de un conjunto de productos.
     */
    @CircuitBreaker(name = "producto-service", fallbackMethod = "fallbackVerificarProductoStock")
    public void verificarProductosStock(List<ProductoIntegrationStockDto> productosValidarStock){
        productoClient.verificarStockProductos(productosValidarStock);
    }

    public void fallbackVerificarProductoStock(List<ProductoIntegrationStockDto> productosValidarStock, Throwable ex){

        //Propaga excepciones de dominio sin modificaciones
        if(ex instanceof BusinessException be){
            throw be;
        }

        //Indica el error de infraestructura en la comunicación.
        log.warn("fallback activado en verificación de stock de productos", ex);
        throw new ServiceUnavailable("No se pudo establecer comunicación con producto-service. Intente de nuevo más tarde");
    }


}
