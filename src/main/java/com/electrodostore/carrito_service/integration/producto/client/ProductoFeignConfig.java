package com.electrodostore.carrito_service.integration.producto.client;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//Clase de configuración para el Feign que hace peticiones a producto-service
@Configuration
public class ProductoFeignConfig {

    /*Registramos el Bean del errorDecoder en el contendor de Spring para que Feign lo encuentre y lo asocie con el FeignClient
      correspondiente que hace peticiones a producto-service*/
    /*Con esto logramos que cuando Feign reciba una response con statusCode diferente de 2xx, revise el errorDecoder asociado
    * y allí se pueda o interpretar la response como excepción de dominio y retornarla o retornar la excepción técnica asociada a esa response*/
    @Bean
    public ProductoErrorDecoder productoErrorDecoder(){return new ProductoErrorDecoder();}
}
