package com.electrodostore.carrito_service.controller;

import com.electrodostore.carrito_service.dto.CarritoResponseDto;
import com.electrodostore.carrito_service.dto.ProductoAgregarDto;
import com.electrodostore.carrito_service.dto.ProductoCambiarCantidadDto;
import com.electrodostore.carrito_service.integration.venta.dto.VentaIntegrationResponseDto;
import com.electrodostore.carrito_service.service.ICarritoService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/carritos")
public class CarritoController {

    private final ICarritoService carritoService;
    public CarritoController(ICarritoService carritoService){this.carritoService = carritoService;}

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<CarritoResponseDto>> findAllCarritos(){
        return ResponseEntity.ok(carritoService.findAllCarritos());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{carritoId}")
    public ResponseEntity<CarritoResponseDto> findCarrito(@PathVariable Long carritoId){
        return ResponseEntity.ok(carritoService.findCarritoResponse(carritoId));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/me")
    public ResponseEntity<CarritoResponseDto> findMyCarrito(){
        return ResponseEntity.ok(carritoService.findMyCarritoPending());
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/me/productos")
    public ResponseEntity<CarritoResponseDto> agregarProductos(@RequestBody @NotEmpty List<@NotNull @Valid ProductoAgregarDto> listProductos){
        return ResponseEntity.ok(carritoService.agregarProductos(listProductos));
    }

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/me/productos/{productoEliminarId}")
    public ResponseEntity<CarritoResponseDto> eliminarProductos(@PathVariable Long productoEliminarId){
        return ResponseEntity.ok(carritoService.deleteProductos(productoEliminarId));
    }

    @PreAuthorize("isAuthenticated()")
    @PatchMapping("/me/productos")
    public ResponseEntity<CarritoResponseDto> cambiarCantidadProducto(@RequestBody @Valid ProductoCambiarCantidadDto productoNuevaCantidad){
        return ResponseEntity.ok(carritoService.cambiarCantidadProducto(productoNuevaCantidad));
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/me/comprar")
    public ResponseEntity<VentaIntegrationResponseDto> comprarCarrito(){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(carritoService.comprarCarrito());
    }

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/me/productos")
    public ResponseEntity<Void> vaciarCarrito(){
        carritoService.vaciarMiCarrito();
        return ResponseEntity.noContent().build();
    }
}
