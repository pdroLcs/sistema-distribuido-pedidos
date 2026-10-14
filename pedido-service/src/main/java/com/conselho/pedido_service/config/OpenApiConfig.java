package com.conselho.pedido_service.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Pedidos API")
                        .description(
                                """
                                API para gerenciamento de pedidos, incluindo:
                                
                                - Listar todos os pedidos
                                - Buscar pedido por ID
                                - Criar um novo pedido
                                """
                        )
                );
    }

}
