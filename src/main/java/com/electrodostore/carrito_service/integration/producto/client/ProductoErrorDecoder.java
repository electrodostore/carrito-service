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

@Slf4j
//Clase de-codificadora de los errorCodes que vienen en el body de las Responses con statusCode diferente de 2xx (response indicando errores)
//Gracias a estos errorCodes es que podemos interpretar las responses para lanzar las respectivas excepciones de dominio (siempre y cuando el error sea un error de dominio)
public class ProductoErrorDecoder implements ErrorDecoder {

    //Instancia de la clase que se encarga de deserealizar el body de una response de Json a objeto DTO (ErrorBodyResponseDto)
    private final ObjectMapper objMapper = new ObjectMapper();

    /*Dentro de este método se va a hacer todo el proceso necesario para interpretar la response de error y retornar a
      Feign una excepción de dominio si es el caso. Si no es un problema de dominio se retornará la excepción técnica correspondiente*/
    //MethodKey = Método Feign que hizó la petición y causó el error
    //Respons = Response completa con todos los parámetros (header, body, statusCode, etc)
    @Override
    public Exception decode(String methodKey, Response response) {
        try{

            //El body de la Response viaja como un conjunto de Bytes, por lo que es necesario almacenar esos bytes en algún sitio. Dicho sitio es InputStream
            InputStream bodyIn = response.body().asInputStream();

            //Con ObjMapper reconstruimos el body de bytes a Json para después deserealizarlo en un objeto de la clase "ErrorResponseDto"
            ErrorBodyResponseDto error = objMapper.readValue(bodyIn, ErrorBodyResponseDto.class);

            //En este caso solo se interpretarán excepciones NOT_FOUND, si la response tiene algún otro statusCode no tenemos como interpretarla
            if(response.status() == 404){

                //Dependiendo del errorCode que venga, retornamos la respectiva excepción
                switch(CarritoErrorCode.valueOf(error.getErrorCode())){

                    //Si el errorCode es indicando que el producto no se encontró -> Excepción de dominio que lo indica
                    case PRODUCT_NOT_FOUND:
                        return new ProductoNotFoundException(error.getMensaje());

                    //Si el errorCode es indicando que un producto no tiene suficiente stock -> Excepción de dominio que lo indica
                    case PRODUCT_STOCK_INSUFICIENTE:
                        return new ProductoStockInsuficienteException(error.getMensaje());
                }
            }

            //Si el statusCode no es 404, entonces retornamos a Feign la excepción original a partir de la Response que llegó
            return FeignException.errorStatus(methodKey, response);

            //Si ocurre algún error en la entrada o salida de datos cuando se intente acceder al body, retornamos la excepción original basada en la Response
        }catch(IOException ex){

            //Notificamos el error al log del proyecto
            log.error("Error leyendo el body de la response", ex);

            //Retornamos excepción original a Feign para que la lance al método que hizo la petición
            return FeignException.errorStatus(methodKey, response);

        }
    }
}
