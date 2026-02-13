package com.electrodostore.carrito_service.integration.cliente;

import com.electrodostore.carrito_service.exception.ServiceUnavailable;
import com.electrodostore.carrito_service.integration.cliente.client.ClienteFeignClient;
import com.electrodostore.carrito_service.integration.cliente.dto.ClienteIntegrationDto;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j  //@Slf4j contiene un logger para agregar errores o warnings informativos al log del proyecto
@Service
//Clase donde se van a definir cada uno de los métodos protegidos que hacen peticiones a cliente-service por medio de ClienteFeignClient
//Este service actúa como intermediario entre la lógica de negocio de carrito-service y el cliente Feign de cliente-service
public class ClienteIntegrationService {

    //Inyección de dependencia por constructor para el cliente Feign de cliente-service
    private final ClienteFeignClient clienteFeignClient;
    public ClienteIntegrationService(ClienteFeignClient clienteFeignClient){
        this.clienteFeignClient = clienteFeignClient;
    }

    /*Método protegido con Circuit-Breaker el cuál actúa como un circuito que después de una serie de errores seguidos de
     infraestructura en la comunicación con cliente-service se abre y me redirige las peticiones al método fallback durante
      un tiempo determinado antes de volver a intentar comunicarse*/
    @CircuitBreaker(name = "cliente-service", fallbackMethod = "findClienteFallback")
    public ClienteIntegrationDto findCliente(Long clienteId){
        return clienteFeignClient.findCliente(clienteId);
    }

    /*Método fallback para el método findCliente, este fallback actúa como un plan-B en caso de que ocurra un error de
     infraestructura en la petición a cliente-service. La función de este es informar sobre el error de comunicación al cliente*/
    public ClienteIntegrationDto findClienteFallback(Long clienteId, Throwable ex){

        //Informamos la activación del fallback
        log.warn("fallback activado para el método findCliente. clienteId={}", clienteId, ex);

        //Lanzamos excepción indicando el problema
        throw new ServiceUnavailable("No fue posible establecer la comunicación con cliente-service. Intente de nuevo más tarde");
    }
}
