package com.electrodostore.carrito_service.integration.venta.client;

import com.electrodostore.carrito_service.integration.venta.dto.ProductoIntegrationRequestDto;
import com.electrodostore.carrito_service.integration.venta.dto.VentaIntegrationResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * Cliente Feign que hace peticiones a venta-service
 */
@FeignClient(name = "venta-service",
        configuration = VentaFeignConfig.class
)
public interface VentaFeignClient {

    @PostMapping("/ventas")
    VentaIntegrationResponseDto createVenta(@RequestBody List<ProductoIntegrationRequestDto> listProductos);

}
