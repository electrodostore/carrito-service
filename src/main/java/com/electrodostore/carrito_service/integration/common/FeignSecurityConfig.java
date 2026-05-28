package com.electrodostore.carrito_service.integration.common;

import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

/**
 * Configuración global de seguridad para requests
 * salientes realizadas mediante FeignClient.
 */
@Configuration
public class FeignSecurityConfig {

    /**
     * Intercepta requests Feign salientes y propaga
     * el token JWT del usuario autenticado.
     */
    @Bean
    public RequestInterceptor jwtPropagationInterceptor() {

        //Permite modificar la request HTTP construida por Feign
        return requestTemplate -> {

            //Obtiene la autenticación asociada a la request actual
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            //Si la autenticación proviene de JWT, se propaga el token
            if (authentication instanceof JwtAuthenticationToken jwtAuth) {
                String token = jwtAuth.getToken().getTokenValue();
                requestTemplate.header(
                        "Authorization",
                        "Bearer " + token);
            }
        };
    }
}
