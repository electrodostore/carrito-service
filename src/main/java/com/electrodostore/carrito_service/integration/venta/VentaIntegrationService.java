package com.electrodostore.carrito_service.integration.venta;

import com.electrodostore.carrito_service.exception.BusinessException;
import com.electrodostore.carrito_service.exception.ServiceUnavailable;
import com.electrodostore.carrito_service.integration.venta.client.VentaFeignClient;
import com.electrodostore.carrito_service.integration.venta.dto.ProductoIntegrationRequestDto;
import com.electrodostore.carrito_service.integration.venta.dto.VentaIntegrationResponseDto;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Define los métodos protegidos por Circuit Breaker
 * que realizan integraciones con venta-service
 * mediante un cliente Feign.
 */
@Slf4j
@Service
public class VentaIntegrationService {

    private final VentaFeignClient ventaClient;

    public VentaIntegrationService(VentaFeignClient ventaClient){this.ventaClient = ventaClient;}


    /**
     * Circuit Breaker para registrar una venta en venta-service
     * */
    @CircuitBreaker(name = "venta-service", fallbackMethod = "fallbackCreateVenta")
    @Retry(name = "venta-service")
    public VentaIntegrationResponseDto createVenta(List<ProductoIntegrationRequestDto> listProductos){
        return ventaClient.createVenta(listProductos);
    }

    /**
     * Fallback ejecutado cuando no es posible completar
     * la integración con venta-service.
     */
    public VentaIntegrationResponseDto fallbackCreateVenta(List<ProductoIntegrationRequestDto> listProductos, Throwable ex){

        //Propaga excepciones de dominio
        if(ex instanceof BusinessException be){
            throw be;
        }

        //Indica el error de infraestructura en la comunicación
        log.warn("fallback activado al intentar registrar una venta", ex);
        throw new ServiceUnavailable("No se pudo completar la compra, intente de nuevo más tarde");
    }
}
