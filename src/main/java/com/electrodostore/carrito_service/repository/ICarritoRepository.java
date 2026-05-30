package com.electrodostore.carrito_service.repository;

import com.electrodostore.carrito_service.model.Carrito;
import com.electrodostore.carrito_service.model.CarritoStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositorio de datos para carrito-service
 */
@Repository
public interface ICarritoRepository extends JpaRepository<Carrito, Long> {

    //Busca el carrito pendiente de un cliente específico
    Optional<Carrito> findByCliente_clientIdAndStatus(Long clientId,
                                                      CarritoStatus status);
}
