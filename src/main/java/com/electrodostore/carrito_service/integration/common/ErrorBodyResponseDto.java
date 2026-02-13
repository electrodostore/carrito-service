package com.electrodostore.carrito_service.integration.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter  @Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
//DTO encargado de almacenar los datos (no necesariamente todos) del body que venga en la Response con un statusCode diferente a 2xx
public class ErrorBodyResponseDto {

    /*Del body solo vamos a extraer el errorCode y el mensaje, con esto será suficiente para interpretar esa Response y saber
    de qué tipo de excepción estamos hablando*/
    private String errorCode;
    private String mensaje;
}
