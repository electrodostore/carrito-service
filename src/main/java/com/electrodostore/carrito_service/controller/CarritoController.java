package com.electrodostore.carrito_service.controller;

import com.electrodostore.carrito_service.dto.CarritoCreadoResponseDto;
import com.electrodostore.carrito_service.service.ICarritoService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/carritos")
public class CarritoController {

    //Inyección de dependencia por constructor para el service de carrito
   private final ICarritoService carritoService;
   public CarritoController(ICarritoService carritoService){this.carritoService = carritoService;}

    @PostMapping
   public ResponseEntity<CarritoCreadoResponseDto> crearCarrito(@RequestBody Long clienteId){
       return ResponseEntity.ok(carritoService.crearCarrito(clienteId));
   }
}
