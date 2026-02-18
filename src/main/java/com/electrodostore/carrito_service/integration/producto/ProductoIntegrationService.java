package com.electrodostore.carrito_service.integration.producto;

import com.electrodostore.carrito_service.exception.BusinessException;
import com.electrodostore.carrito_service.exception.ServiceUnavailable;
import com.electrodostore.carrito_service.integration.producto.client.ProductoFeignClient;
import com.electrodostore.carrito_service.integration.producto.dto.ProductoIntegrationDto;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j //@Slf4j contiene un logger para lanzar errores o warnings informativos al log del proyecto
//Servicio donde se definen los métodos protegidos por Circuit-Breaker que harán diferentes peticiones a producto-service por medio del cliente Feign
@Service
public class ProductoIntegrationService {

    //Inyección de dependencia por constructor para la clase FeignClient de producto-service
    private final ProductoFeignClient productoClient;
    public ProductoIntegrationService(ProductoFeignClient productoClient){this.productoClient = productoClient;}

    /*Método protegido por Circuit-Breaker encargado de usar al FeignClient de producto-service para consultar los datos
     de un grupo de productos por sus ids*/
    /*En caso de que ocurra un error en la comunicación, se activará el fallback asociado a este método y ahí se manejará ya
     sea como excepción de dominio o excepción de infraestructura*/
    @CircuitBreaker(name = "producto-service", fallbackMethod = "fallbackFindProductos")
    @Retry(name = "producto-service")
    public List<ProductoIntegrationDto> findProductos(List<Long> productosIds){
        //En caso de tener ids duplicados en la lista, es mejor borrarlos
        Set<Long> productosIdsUnicos = new HashSet<>(productosIds);

        return productoClient.findProductos(new ArrayList<>(productosIdsUnicos));
    }

    //Fallback del método findProductos
    /*El fallback debe tener la misma firma del método al que pertenece, y el último parámetro de este debe ser un objeto
    * de la superclase de todas las excepciones: Throwable, este objeto es la excepción que activó el fallback*/
    public List<ProductoIntegrationDto> fallbackFindProductos(List<Long> productosIds, Throwable ex){

        //Filtramos las excepciones de dominio para evitar que se lance un service-unavailable y que oculte el problema real
        if(ex instanceof BusinessException be){
            /*be es la excepción original, pero ya no es de tipo estático Throwable sino BusinessException y el tipo dinámico
            de esta es la respectiva excepción de dominio que se esté lanzando, al final será esta la que verá el cliente*/
            throw be;
        }

        //Agregamos warn al log del proyecto indicando la activación del fallback
        log.warn("fallback activado en la consulta de varios productos a producto-service", ex);

        //Si la excepción que activó el fallback no es excepción de dominio -> Excepción de infraestructura (error técnico en la comunicación)
        throw new ServiceUnavailable("Error en la comunicación con producto-service. Por favor intente de nuevo más tarde");
    }

    //Método protegido por Circuit-Breaker para consultar un producto por su ID
    @CircuitBreaker(name = "producto-service", fallbackMethod = "fallbackFindProducto")
    @Retry(name = "producto-service")
    public ProductoIntegrationDto findProducto(Long productoId){
        //Consultamos por Feign el producto correspondiente
        return productoClient.findProducto(productoId);
    }

    //Fallback para el método de consultar un producto por su id
    //Firma del método + Throwable (Excepción que activó el fallback con tipo estático Throwable)
    public ProductoIntegrationDto fallbackFindProducto(Long productoId, Throwable ex){

        //Si la excepción que activó el fallback es excepción de dominio, no tiene sentido lanzar un ServiceUnavailable
        if(ex instanceof BusinessException be){
            /*be es la excepción que activó el fallback pero parseada de tipo estático Throwable a BusinessException.
            * De igual forma el tipo dinámico de esta sigue siendo la excepción de dominio correspondiente*/
            throw be;
        }

        //Si ex no es excepción de dominio, entonces la manejamos con excepción de infraestructura

        //Indicamos la activación del fallback
        log.warn("Fallback activado en la comunicación con producto-service. productoId={}", productoId, ex);

        //Lanzamos excepción indicando el problema de comunicación
        throw new ServiceUnavailable("No fue posible establecer la comunicación con producto-service. Intente de nuevo más tarde");
    }
}
