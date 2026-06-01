package com.electrodostore.carrito_service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Centraliza el manejo de excepciones de la API
 * para errores conocidos.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    //Construye el cuerpo de la respuesta de error
    private Map<String, Object> buildExceptionBody(HttpStatus status, String mensaje, String errorCode){
        Map<String, Object> response = new LinkedHashMap<>();

        response.put("timestamp", LocalDateTime.now());
        response.put("status", status.value());
        response.put("error", status.getReasonPhrase());
        response.put("errorCode", errorCode);
        response.put("mensaje", mensaje);

        return response;
    }

    @ExceptionHandler(CarritoNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handlerCarritoNotFound(CarritoNotFoundException ex){
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(buildExceptionBody(HttpStatus.NOT_FOUND, ex.getMessage(), ex.getErrorCode().name()));
    }

    @ExceptionHandler(ProductoNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handlerProductoNotFound(ProductoNotFoundException ex){
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(buildExceptionBody(HttpStatus.NOT_FOUND, ex.getMessage(), ex.getErrorCode().name()));
    }

    @ExceptionHandler(ClienteNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handlerClienteNotFound(ClienteNotFoundException ex){
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(buildExceptionBody(HttpStatus.NOT_FOUND, ex.getMessage(), ex.getErrorCode().name()));
    }

    @ExceptionHandler(ServiceUnavailable.class)
    public ResponseEntity<Map<String,Object>> handlerServiceUnavailable(ServiceUnavailable ex){
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(buildExceptionBody(HttpStatus.SERVICE_UNAVAILABLE, ex.getMessage(), ex.getErrorCode().name()));
    }

    @ExceptionHandler(ProductoStockInsuficienteException.class)
    public  ResponseEntity<Map<String, Object>> handlerProductoStockInsuficiente(ProductoStockInsuficienteException ex){
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(buildExceptionBody(HttpStatus.CONFLICT, ex.getMessage(), ex.getErrorCode().name()));
    }

    @ExceptionHandler(CarritoPurchasedException.class)
    public ResponseEntity<Map<String, Object>> handlerCarritoPurchased(CarritoPurchasedException ex){
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(buildExceptionBody(HttpStatus.CONFLICT, ex.getMessage(), ex.getErrorCode().name()));
    }

    @ExceptionHandler(UnauthorizedOperationException.class)
    public ResponseEntity<Map<String, Object>> handlerUnauthorizedOperation(UnauthorizedOperationException ex){
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(buildExceptionBody(HttpStatus.FORBIDDEN, ex.getMessage(), ex.getErrorCode().name()));
    }

}
