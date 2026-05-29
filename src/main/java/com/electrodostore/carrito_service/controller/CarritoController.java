package com.electrodostore.carrito_service.controller;

import com.electrodostore.carrito_service.dto.CarritoCreadoResponseDto;
import com.electrodostore.carrito_service.dto.CarritoResponseDto;
import com.electrodostore.carrito_service.dto.ProductoAgregarDto;
import com.electrodostore.carrito_service.dto.ProductoCambiarCantidadDto;
import com.electrodostore.carrito_service.integration.venta.dto.VentaIntegrationResponseDto;
import com.electrodostore.carrito_service.service.ICarritoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/carritos")
public class CarritoController {

    //Inyección de dependencia por constructor para el service de carrito
   private final ICarritoService carritoService;
   public CarritoController(ICarritoService carritoService){this.carritoService = carritoService;}

    @GetMapping
    public ResponseEntity<List<CarritoResponseDto>> findAllCarritos(){
       return ResponseEntity.ok(carritoService.findAllCarritos());
    }

    @GetMapping("/{carritoId}")
    public ResponseEntity<CarritoResponseDto> findCarrito(@PathVariable Long carritoId){
       return ResponseEntity.ok(carritoService.findCarritoResponse(carritoId));
    }

    @GetMapping("/me")
    public ResponseEntity<CarritoResponseDto> findMyCarrito(){
        return ResponseEntity.ok(carritoService.findMyCarritoPending());
    }

   //Es una operación POST porque estamos guardando o registrando algo (productos) dentro del carrito
   @PostMapping("/agregar-productos")
    public ResponseEntity<CarritoResponseDto> agregarProductos(@RequestBody List<ProductoAgregarDto> listProductos){
       return ResponseEntity.ok(carritoService.agregarProductos(listProductos));
   }

   //Es un método DELETE ya que se está eliminando un recurso (productos) dentro del carrito
   @DeleteMapping("/eliminar-producto/{productoEliminarId}")
    public ResponseEntity<CarritoResponseDto> eliminarProductos( @PathVariable Long productoEliminarId){
       return ResponseEntity.ok(carritoService.deleteProductos(productoEliminarId));
   }

   //Es un método patch ya que estamos actualizando parcialmente un recurso (producto) dentro del carrito
   @PatchMapping("/actualizar-cantidad-producto")
    public ResponseEntity<CarritoResponseDto> cambiarCantidadProducto(@RequestBody ProductoCambiarCantidadDto productoNuevaCantidad){
       return ResponseEntity.ok(carritoService.cambiarCantidadProducto(productoNuevaCantidad));
   }

   @PostMapping("/comprar-carrito")
    public ResponseEntity<VentaIntegrationResponseDto> comprarCarrito(){
       return ResponseEntity.status(HttpStatus.CREATED)
               .body(carritoService.comprarCarrito());
   }
}
