package com.electrodostore.carrito_service.integration.venta.client;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class VentaFeignConfig {

    /**
     * Configura decodificador de errores para traducir excepciones conocidas
     */
    @Bean
    public VentaErrorDecoder errorDecoder(){return new VentaErrorDecoder();}
}
