package com.electrodostore.carrito_service.repository;

import com.electrodostore.carrito_service.model.Carrito;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

//Definimos el centro de las operaciones para el repositorio de carrito-service
@Repository
public interface ICarritoRepository extends JpaRepository<Carrito, Long> {
}
