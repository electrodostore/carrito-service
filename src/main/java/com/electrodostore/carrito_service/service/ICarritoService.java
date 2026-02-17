package com.electrodostore.carrito_service.service;

import com.electrodostore.carrito_service.dto.CarritoCreadoResponseDto;

//Interfaz donde se van a declarar todas las operaciones de este dominio (carrito)
public interface ICarritoService {

    //Crear un carrito asignado a un determinado cliente
    CarritoCreadoResponseDto crearCarrito(Long clienteId);

}
