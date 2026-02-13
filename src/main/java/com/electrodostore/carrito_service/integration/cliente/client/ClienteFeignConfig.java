package com.electrodostore.carrito_service.integration.cliente.client;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//Clase de configuración para el FeignClient que se encarga de hacer peticiones a cliente-service
@Configuration
public class ClienteFeignConfig {

    /*Definimos el Bean que Spring registra para que Spring-Cloud-OpenFeign utilice como el errorDecoder del FeignClient
    * que crea para hacer peticiones a cliente-service*/
    /*Cuandó Feign reciba una response con statusCode diferente de 2xx, este revisará el errorDecoder que se le configuró
    para tratar de interpretar y transformar la response para retornarla a Feign ya sea como excepción de dominio o FeignException
    basada en la Response*/
    @Bean
    public ClienteErrorDecoder feignErrorDecoder(){return new ClienteErrorDecoder();}
}
