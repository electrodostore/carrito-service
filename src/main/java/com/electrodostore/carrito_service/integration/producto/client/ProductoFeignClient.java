package com.electrodostore.carrito_service.integration.producto.client;

import com.electrodostore.carrito_service.integration.producto.dto.ProductoIntegrationDto;
import com.electrodostore.carrito_service.integration.producto.dto.ProductoIntegrationStockDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

//Cliente Feign que hace integraciones con producto-service
@FeignClient(name = "producto-service",
        configuration = ProductoFeignConfig.class)
public interface ProductoFeignClient {

    //Busca un conjunto de productos por sus ids
    @PostMapping("/productos/search")
    List<ProductoIntegrationDto> findProductos(@RequestBody List<Long> productosIds);

    @GetMapping("/productos/{productoId}")
    ProductoIntegrationDto findProducto(@PathVariable Long productoId);

    @PostMapping("/productos/stock/verificar")
    void verificarStockProductos(@RequestBody List<ProductoIntegrationStockDto> productosValidarStock);

}
