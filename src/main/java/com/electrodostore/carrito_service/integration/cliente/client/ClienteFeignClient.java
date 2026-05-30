package com.electrodostore.carrito_service.integration.cliente.client;

import com.electrodostore.carrito_service.integration.cliente.dto.ClienteIntegrationDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Cliente Feign que hace peticiones a cliente-service
 */
@FeignClient(name = "cliente-service",
        configuration = ClienteFeignConfig.class)
public interface ClienteFeignClient {

    /**
     * Consulta los datos de un cliente y los trae si
     * el cliente está habilitado para realizar operaciones comerciales
     */
    @GetMapping("/clientes/{clienteId}/enabled")
    ClienteIntegrationDto findCliente(@PathVariable Long clienteId);
}
