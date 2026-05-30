package com.electrodostore.carrito_service.integration.cliente;

import com.electrodostore.carrito_service.exception.BusinessException;
import com.electrodostore.carrito_service.exception.ServiceUnavailable;
import com.electrodostore.carrito_service.integration.cliente.client.ClienteFeignClient;
import com.electrodostore.carrito_service.integration.cliente.dto.ClienteIntegrationDto;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Define métodos protegidos con Circuit Breaker
 * para hacer integraciones con cliente-service.
 */
@Slf4j
@Service
public class ClienteIntegrationService {

    private final ClienteFeignClient clienteFeignClient;
    public ClienteIntegrationService(ClienteFeignClient clienteFeignClient){
        this.clienteFeignClient = clienteFeignClient;
    }

    /**
     * Consulta de un cliente en cliente-service
     * protegida por Circuit Breaker y Retry
     */
    @CircuitBreaker(name = "cliente-service", fallbackMethod = "findClienteFallback")
    @Retry(name = "cliente-service")
    public ClienteIntegrationDto findCliente(Long clienteId){
        return clienteFeignClient.findCliente(clienteId);
    }

    /**
     * Fallback ejecutado al ocurrir un error
     * en la integración con cliente-service cuando
     * se consulta un cliente
     */
    public ClienteIntegrationDto findClienteFallback(Long clienteId, Throwable ex){

        //Propaga excepciones de dominio sin modificarlas
        if(ex instanceof BusinessException be){
            throw be;
        }

        //Informa sobre el error de infraestructura en la integración
        log.warn("fallback activado para el método findCliente. clienteId={}", clienteId, ex);
        throw new ServiceUnavailable("No fue posible establecer la comunicación con cliente-service. Intente de nuevo más tarde");
    }
}
