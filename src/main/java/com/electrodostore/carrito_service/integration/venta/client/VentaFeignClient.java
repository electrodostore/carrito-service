package com.electrodostore.carrito_service.integration.venta.client;

import com.electrodostore.carrito_service.integration.venta.dto.VentaIntegrationRequestDto;
import com.electrodostore.carrito_service.integration.venta.dto.VentaIntegrationResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

//Clase cliente Feign de venta-service y por medio de la cual se van a hacer las peticiones a ese servicio
@FeignClient(name = "venta-service",  //mismo nombre registrado en Eureka-Server
        configuration = VentaFeignConfig.class //Definimos la configuración que este FeignClient va a tener
)
public interface VentaFeignClient {

    //Descripción del método que guarda una venta a partir de los datos del carrito que se compró
    @PostMapping("/ventas")
    VentaIntegrationResponseDto createVenta(@RequestBody VentaIntegrationRequestDto nuevaVenta);

}
