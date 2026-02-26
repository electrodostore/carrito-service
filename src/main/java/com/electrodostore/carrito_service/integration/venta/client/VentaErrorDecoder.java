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

//Decodificador de errorCodes provenientes de venta-service en Responses de error (statusCode diferente de 2xx)
@Slf4j
public class VentaErrorDecoder implements ErrorDecoder {

    //ObjectMapper se encargará de deserializar las responses para sacar el errorCode
    private final ObjectMapper objectMapper = new ObjectMapper();

    /*Método que se encarga de hacer el proceso necesario para decifrar el errorCode que venga en la response y
     decidir así si este corresponde a una excepción de dominio o se trata de una excepción técnica que no podemos manejar*/
    //methodKey = método que hizo la petición y por consecuencia cometió el error
    //response -> Response completa con todos los parámetros (Header, Body, StatusCode, etc)
    @Override
    public Exception decode(String methodKey, Response response) {
        try{

            //El body de la Response viaja como Bytes, por lo que es necesario tener un lugar donde almacenarlos -> InputStream
            InputStream bodyIn = response.body().asInputStream();

            /*El método readValue de ObjectMapper se encarga de reconstruir el body de Bytes a formato JSON y así ponder
              deserealizarlo en la clase que se le indique como segundo parámetro*/
            ErrorBodyResponseDto error = objectMapper.readValue(bodyIn, ErrorBodyResponseDto.class);

            //En este caso solo podemos tratar las Response con statusCode = 404, ya que son a las únicas que les conocemos el errorCode
            if(response.status() == 404){

                //Analizamos los posibles casos de errorCode que se pueden dar y retornamos a Feign la excepción de dominio correspondiente
                switch(CarritoErrorCode.valueOf(error.getErrorCode())){

                    case CLIENT_NOT_FOUND:
                        return new ClienteNotFoundException(error.getMensaje());

                    case PRODUCT_NOT_FOUND:
                        return new ProductoNotFoundException(error.getMensaje());

                    case PRODUCT_STOCK_INSUFICIENTE:
                        return new ProductoStockInsuficienteException(error.getMensaje());
                }
            }

            /*En caso de que el statusCode de la Response no sea 404, no podemos transformar la excepción por lo que
              mostramos el FeignException original basada en la Response a Feign*/
            return FeignException.errorStatus(methodKey, response);

            /*Si ocurre una excepción de entrada o salida de datos, por ejemplo cuando accedemos al body de la Response
              manejamos la excepción como sigue:*/
        }catch(IOException e){
            //Mostramos el error en el log del proyecto
            log.error("Error intentando leer el body de la response", e);

            //Construimos el FeignException basado en la response para retornarlo a Feign y que este lo lance al método que hizó la petición
            return FeignException.errorStatus(methodKey, response);
        }

    }
}
