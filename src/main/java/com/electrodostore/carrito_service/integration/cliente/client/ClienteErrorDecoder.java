package com.electrodostore.carrito_service.integration.cliente.client;

import com.electrodostore.carrito_service.exception.CarritoErrorCode;
import com.electrodostore.carrito_service.exception.ClienteNotFoundException;
import com.electrodostore.carrito_service.integration.common.ErrorBodyResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.FeignException;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;

@Slf4j
/*Clase errorDecoder cuya función es interpretar por medio de los errorCodes las response del servicio Cliente que vengan con
 statusCode diferente de 2xx y revisar, antes de que Feign construya la excepción, si se puede transformar a una excepción de dominio*/
public class ClienteErrorDecoder implements ErrorDecoder {

    //Creamos objeto capaz de deserializar el body de una Response para sacar de él su errorCode
    private final ObjectMapper objMapper = new ObjectMapper();

    /*Método encargado de retornarle a Feign la excepción final que este lanzará al método que hizo la petición. Esta
       excepción puede ser de dominio (si la logramos transformar) o la excepción original que causo el problema*/
    //MethodKey -> Nombre del método que hizó la petición por FeignClient
    //response -> Response completa con todos los parámetros (Header, body, statusCode, etc)
    @Override
    public Exception decode(String methodKey, Response response) {
        try{

            /*El body de la Response viaja como un conjunto de bytes, por lo que es necesario almacenar esos bytes en
            algún lado para que objMapper los pueda reconstruir como Json, ese "algún lado" es InputStream*/
            InputStream bodyIn = response.body().asInputStream();

            //Finalmente, ObjMapper se encarga de reconstruir el body y deserializarlo en la clase que se le envié como segundo parámetro
            ErrorBodyResponseDto error = objMapper.readValue(bodyIn, ErrorBodyResponseDto.class);

            //Solo comparamos responses con statusCode == 404, ya que las excepciones que necesitamos interpretar, en este caso, son las de NOT_FOUND
            if(response.status() == 404){

                //Una vez sepamos que algún recurso no se encontró, procedemos a averiguar cuál es exactamente ese recurso
                switch (CarritoErrorCode.valueOf(error.getErrorCode())){

                    //Si el error es que no se encontró el cliente consultado, se indica por medio de la excepción de dominio
                    case CLIENT_NOT_FOUND:
                        return new ClienteNotFoundException(error.getMensaje());

                    /*NOTA: Como el servicio Cliente solo lanza errores cuando no se encuentra un cliente, no será necesario
                      crear más casos de enum dentro del switch. Ahora, esto puede cambiar en cualquier momento si el servicio
                      cliente llegase a crecer*/
                }
            }

            //Si el statusCode no es 404, no tenemos como interpretar asi que le retornamos a Feign la excepción que causó el problema
            return FeignException.errorStatus(methodKey, response);

            /*Si la excepción es de entrada o salida de datos (Problemas al encontrar o leer el body de la Response)
            * manejamos la excepción agregando el mensaje del error al log del proyecto y retornando a Feign la excepción
            * correspondiente a la Response que llegó*/
        }catch(IOException ex){
            log.error("Problema leyendo el body de la response");
            return FeignException.errorStatus(methodKey, response);
        }

    }
}
