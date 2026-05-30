package com.electrodostore.carrito_service.exception;

import com.electrodostore.carrito_service.model.Carrito;
import lombok.Getter;

/**
 * Excepción de seguridad para denegar el acceso
 * de usuarios no autorizados a recursos protegidos
 */
@Getter
public class UnauthorizedOperationException extends BusinessException {
    private final CarritoErrorCode errorCode;

    public UnauthorizedOperationException(String message) {
        super(message);
        this.errorCode = CarritoErrorCode.UNAUTHORIZED_OPERATION;
    }
}
