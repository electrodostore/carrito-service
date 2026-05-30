package com.electrodostore.carrito_service.integration.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter  @Setter
@AllArgsConstructor
@NoArgsConstructor
/*Permite deserializar el cuerpo de una
 * respuesta de error aunque contenga
 * propiedades desconocidas en este dominio.*/
@JsonIgnoreProperties(ignoreUnknown = true)
public class ErrorBodyResponseDto {

    private String errorCode;
    private String mensaje;
}
