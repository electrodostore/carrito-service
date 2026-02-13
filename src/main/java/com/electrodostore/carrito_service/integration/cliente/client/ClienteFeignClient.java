package com.electrodostore.carrito_service.integration.cliente.client;

import com.electrodostore.carrito_service.integration.cliente.dto.ClienteIntegrationDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

//Interface FeignClient donde se va a describir y declarar cada end-point perteneciente a un determinado método en cliente-service y los parámetros requeridos por este
@FeignClient(name = "cliente-service") //Mismo nombre con el que se registró el servicio en eureka-server
public interface ClienteFeignClient {

    //Descripción del método que busca y trae un cliente desde el servicio cliente
    @GetMapping("/clientes/{clienteId}")
    ClienteIntegrationDto findCliente(@PathVariable Long clienteId);
}
