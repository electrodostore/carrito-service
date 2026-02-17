package com.electrodostore.carrito_service.integration.producto.client;

import com.electrodostore.carrito_service.integration.producto.dto.ProductoIntegrationDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

//Clase descriptiva de cada uno de los métodos para hacer peticiones al servicio Producto
@FeignClient(name = "producto-service") //Mismo nombre con el que se registró en eureka-server
public interface ProductoFeignClient {

    //Descripción del método para consultar una lista de productos por sus ids
    @PostMapping("/productos/traer-productos-por-ids")
    List<ProductoIntegrationDto> findProductos(@RequestBody List<Long> productosIds);

    //Descripción del método para consultar un producto por su id
    @GetMapping("/productos/{productoId}")
    ProductoIntegrationDto findProducto(@PathVariable Long productoId);


}
