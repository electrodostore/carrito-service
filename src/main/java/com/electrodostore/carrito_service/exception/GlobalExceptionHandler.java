package com.electrodostore.carrito_service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

/*Clase marcada con @RestControllerAdvice la convierte en un manejador global de excepciones. Cuando ocurra una excepción Spring
  revisa esta clase antes de lanzar la excepción para ver si no hay un handler que la maneje*/
@RestControllerAdvice
public class GlobalExceptionHandler {

    //Método propio para construir el body de la response cuándo ocurra algún error de dominio
    private Map<String, Object> buildExceptionBody(HttpStatus status, String mensaje, String errorCode){

        //Se saca un objeto de la interfaz map<> que me crea pares: clave-valor similar un diccionario
        Map<String, Object> response = new LinkedHashMap<>();

        //Se crea la response de la excepción con los siguientes parámetros informativos
        response.put("timestamp", LocalDateTime.now());
        response.put("status", status.value());
        response.put("error", status.getReasonPhrase());
        response.put("errorCode", errorCode);
        response.put("mensaje", mensaje);

        return response;
    }

    //Manejador de la excepción carritoNotFound
    @ExceptionHandler(CarritoNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handlerCarritoNotFound(CarritoNotFoundException ex){
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(buildExceptionBody(HttpStatus.NOT_FOUND, ex.getMessage(), ex.getErrorCode().name()));
    }

    //Manejador de la excepción ProductoNotFound
    @ExceptionHandler(ProductoNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handlerProductoNotFound(ProductoNotFoundException ex){
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(buildExceptionBody(HttpStatus.NOT_FOUND, ex.getMessage(), ex.getErrorCode().name()));
    }

    //Manejador de la excepción ClienteNotFound
    @ExceptionHandler(ClienteNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handlerClienteNotFound(ClienteNotFoundException ex){
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(buildExceptionBody(HttpStatus.NOT_FOUND, ex.getMessage(), ex.getErrorCode().name()));
    }

    //Manejador de la excepción de dominio ServiceUnavailable
    @ExceptionHandler(ServiceUnavailable.class)
    public ResponseEntity<Map<String,Object>> handlerServiceUnavailable(ServiceUnavailable ex){
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(buildExceptionBody(HttpStatus.SERVICE_UNAVAILABLE, ex.getMessage(), ex.getErrorCode().name()));
    }

    //Handler de la excepción de dominio ProductoStockInsuficiente
    @ExceptionHandler(ProductoStockInsuficienteException.class)
    public  ResponseEntity<Map<String, Object>> handlerProductoStockInsuficiente(ProductoStockInsuficienteException ex){
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(buildExceptionBody(HttpStatus.NOT_FOUND, ex.getMessage(), ex.getErrorCode().name()));
    }

    @ExceptionHandler(CarritoPurchasedException.class)
    public ResponseEntity<Map<String, Object>> handlerCarritoPurchased(CarritoPurchasedException ex){
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(buildExceptionBody(HttpStatus.CONFLICT, ex.getMessage(), ex.getErrorCode().name()));
    }
}
