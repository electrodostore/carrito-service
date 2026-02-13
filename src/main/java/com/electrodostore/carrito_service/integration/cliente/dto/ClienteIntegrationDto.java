package com.electrodostore.carrito_service.integration.cliente.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter  @Setter
@AllArgsConstructor
@NoArgsConstructor
//DTO de integración donde se va a deserealizar el cliente que venga en formato Json desde cliente-service
public class ClienteIntegrationDto {

    private Long id;
    private String name;
    private String cellphone;
    private String document;
    private String address;
}
