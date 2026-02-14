package com.electrodostore.carrito_service.exception;

/*SuperExcepción personalizada capaz de identificar a las excepciones de dominio por medio de una característica única,
 ya que todas estas heredarán de esta excepción*/
public class BusinessException extends RuntimeException {

    public BusinessException(String message) {
        super(message);
    }
}
