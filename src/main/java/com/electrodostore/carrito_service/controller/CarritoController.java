package com.electrodostore.carrito_service.controller;

import com.electrodostore.carrito_service.dto.CarritoCreadoResponseDto;
import com.electrodostore.carrito_service.dto.CarritoResponseDto;
import com.electrodostore.carrito_service.dto.ProductoAgregarDto;
import com.electrodostore.carrito_service.service.ICarritoService;
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

    @PostMapping
   public ResponseEntity<CarritoCreadoResponseDto> crearCarrito(@RequestBody Long clienteId){
       return ResponseEntity.ok(carritoService.crearCarrito(clienteId));
   }

   @PatchMapping("/agregar-productos/{carritoId}")
    public ResponseEntity<CarritoResponseDto> agregarProductos(@PathVariable Long carritoId, @RequestBody List<ProductoAgregarDto> listProductos){
       return ResponseEntity.ok(carritoService.agregarProductos(carritoId, listProductos));
   }
}
