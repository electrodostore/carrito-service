package com.electrodostore.carrito_service.integration.venta.client;

import com.electrodostore.carrito_service.exception.CarritoErrorCode;
import com.electrodostore.carrito_service.exception.ClienteNotFoundException;
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
 * Decodifica errores provenientes de venta-service
 * para intentar traducirlos a excepciones
 * de dominio conocidas.
 */
@Slf4j
public class VentaErrorDecoder implements ErrorDecoder {

    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Intenta convertir la respuesta de error Feign en excepción de dominio
     * o retorna FeignException si no es posible.
     * */
    @Override
    public Exception decode(String methodKey, Response response) {
        try{

            if(response.body() == null){
                return FeignException.errorStatus(methodKey, response);
            }

            //Leemos body de la respuesta y lo mapeamos a DTO de error
            InputStream bodyIn = response.body().asInputStream();
            ErrorBodyResponseDto error = objectMapper.readValue(bodyIn, ErrorBodyResponseDto.class);

            //En este caso, solo podemos interpretar respuestas HTTP 404
            if(response.status() == 404){

                // Intenta mapear el error remoto a una excepción conocida del dominio.
                switch(CarritoErrorCode.valueOf(error.getErrorCode())){

                    case CLIENT_NOT_FOUND:
                        return new ClienteNotFoundException(error.getMensaje());

                    case PRODUCT_NOT_FOUND:
                        return new ProductoNotFoundException(error.getMensaje());

                    case PRODUCT_STOCK_INSUFICIENTE:
                        return new ProductoStockInsuficienteException(error.getMensaje());
                }
            }


            return FeignException.errorStatus(methodKey, response);

            /*Si ocurre un error leyendo el cuerpo de la respuesta,
               retornamos excepción Feign predeterminada*/
        }catch(IOException e){
            log.error("Error intentando leer el body de la response", e);
            return FeignException.errorStatus(methodKey, response);
        }

    }
}
