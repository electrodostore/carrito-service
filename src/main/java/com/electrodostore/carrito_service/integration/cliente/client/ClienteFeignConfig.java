package com.electrodostore.carrito_service.integration.cliente.client;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ClienteFeignConfig {

    /**
     * Configura decodificador de errores para traducir
     * respuestas de error conocidos a excepciones de dominio.
     * */
    @Bean
    public ClienteErrorDecoder feignErrorDecoder(){return new ClienteErrorDecoder();}
}
