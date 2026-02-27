package com.electrodostore.carrito_service.integration.venta;

import com.electrodostore.carrito_service.exception.BusinessException;
import com.electrodostore.carrito_service.exception.ServiceUnavailable;
import com.electrodostore.carrito_service.integration.venta.client.VentaFeignClient;
import com.electrodostore.carrito_service.integration.venta.dto.VentaIntegrationRequestDto;
import com.electrodostore.carrito_service.integration.venta.dto.VentaIntegrationResponseDto;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

//Clase donde se van a definir los métodos protegidos por Circuit-Breaker que harán peticiones a venta-service con ayuda del FeignClient que se creó
@Slf4j  //@Slf4j tiene un logger con el que agregaremos warnings o errores al log del proyecto
@Service
public class VentaIntegrationService {

    //Inyección de dependencia por constructor para el FeignClient que hace peticiones a venta-service
    private final VentaFeignClient ventaClient;
    public VentaIntegrationService(VentaFeignClient ventaClient){this.ventaClient = ventaClient;}

    //Método protegido por Circuit-Breaker encargado de registrar una venta en venta-service
    //Se le asocia un método fallback para cuando ocurra algún fallo tener un plan-B de como actuar
    @CircuitBreaker(name = "venta-service", fallbackMethod = "fallbackCreateVenta")
    @Retry(name = "venta-service") //Definimos que reintente la petición en caso de fallos
    public VentaIntegrationResponseDto createVenta(VentaIntegrationRequestDto ventaNueva){
        return ventaClient.createVenta(ventaNueva);
    }

    //Método fallback que se activa cuando hay un fallo en la petición que registra una venta en venta-service
    public VentaIntegrationResponseDto fallbackCreateVenta(VentaIntegrationRequestDto ventaNueva, Throwable ex){

        //Filtramos las excepciones de dominio para evitar ocultarlas con una excepción SERVICE_UNAVAILABLE
        /*Por sintaxis de Resilinece4j el método fallback debe tener la misma firma del método protegido y un último
        * parámetro Throwable que es la excepción que activó el fallback. No se puede lanzar directamente una excepción
        * que sea Throwable ya que Java piensa que se puede tratar de una excepción Checked que necesitan un manejo
        * explicito, pero como nuestras excepciones de dominio son Unchecked (Heredan de RuntimeException) dicho manejo no
        * es necesario, asi que cambiamos el tipo estático de la excepción de Throwable a BusinessException para poder
        * lanzarla*/
        if(ex instanceof BusinessException be){
            throw be;
        }

        //Indicamos la activación del fallback en el log del proyecto
        log.warn("fallback activado al intentar registrar una venta", ex);

        /*Si la excepción que activó el fallback no es excepción de dominio, entonces lanzamos el SERVICE_UNAVAILABLE indicando
           el error de infraestructura*/
        throw new ServiceUnavailable("No se pudo completar la compra, intente de nuevo más tarde");
    }
}
