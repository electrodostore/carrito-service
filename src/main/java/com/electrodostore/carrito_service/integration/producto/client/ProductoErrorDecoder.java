package com.electrodostore.carrito_service.integration.producto.client;

import com.electrodostore.carrito_service.exception.CarritoErrorCode;
import com.electrodostore.carrito_service.exception.ProductoNotFoundException;
import com.electrodostore.carrito_service.exception.ProductoStockInsuficienteException;
import com.electrodostore.carrito_service.integration.common.ErrorBodyResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.FeignException;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;

/**
 * Decodifica errores provenientes de producto-service
 * e intenta traducirlos en excepciones de dominio.
 * */
@Slf4j
public class ProductoErrorDecoder implements ErrorDecoder {

    private final ObjectMapper objMapper = new ObjectMapper();

    /**
     * Intenta traducir la respuesta de error en excepción de dominio,
     * si no es posible retorna excepción Feign predeterminada.
     * */
    @Override
    public Exception decode(String methodKey, Response response) {
        try{

            if (response.body() == null){return FeignException.errorStatus(methodKey, response);}

            //Lee el cuerpo de la respuesta y lo mapea a DTO de error
            InputStream bodyIn = response.body().asInputStream();
            ErrorBodyResponseDto error = objMapper.readValue(bodyIn, ErrorBodyResponseDto.class);

            //Solo se conocen errores HTTP 404
            if(response.status() == 404){

                // Intenta mapear la respuesta de error a excepción conocida del dominio
                switch(CarritoErrorCode.valueOf(error.getErrorCode())){

                    case PRODUCT_NOT_FOUND:
                        return new ProductoNotFoundException(error.getMensaje());

                    case PRODUCT_STOCK_INSUFICIENTE:
                        return new ProductoStockInsuficienteException(error.getMensaje());
                }
            }

            return FeignException.errorStatus(methodKey, response);

            //Si hay un problema leyendo el cuerpo de la respuesta, se retorna excepción Feign predeterminada.
        }catch(IOException ex){
            log.error("Error leyendo el body de la response", ex);
            return FeignException.errorStatus(methodKey, response);

        }
    }
}
