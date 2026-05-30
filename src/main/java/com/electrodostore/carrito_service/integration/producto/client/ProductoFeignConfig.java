package com.electrodostore.carrito_service.integration.producto.client;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class
ProductoFeignConfig {

    /*Configura el decodificador de errores que intenta traducir respuestas de error
      en excepciones de dominio*/
    @Bean
    public ProductoErrorDecoder productoErrorDecoder(){return new ProductoErrorDecoder();}
}
