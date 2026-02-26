package com.electrodostore.carrito_service.integration.venta.client;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//Clase de configuración que se asocia con el FeignClient que se crea para hacer peticiones a venta-service
@Configuration
public class VentaFeignConfig {

    /*Registramos el Bean del errorDecoder de venta-service en el contenedor de Spring para Feign lo encuentre allí y lo
    asocie con el FeignClient que hace peticiones a venta-service cuando lo cree¨*/
    @Bean
    public VentaErrorDecoder errorDecoder(){return new VentaErrorDecoder();}
}
