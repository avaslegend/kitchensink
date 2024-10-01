package com.app.kitchensink.infraestructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI(){
        

        return new OpenAPI().info(new Info().title("Kitchen Skin REST API")
                                            .description("Our app provides a concise managing of people information")
                                            .version("1.0.0")
                                            .contact(new Contact().name("Smith Vasquez")
                                                                  .email("pro.sava.25@gmail.com"))
                            ).servers(getServers());
                                           
    }

    private List<Server> getServers() {
        List<Server> servers = new ArrayList<>();
        Server server1 = new Server();
        server1.setDescription("Develop Server");
        server1.setUrl("http://localhost:8081");
        servers.add(server1);

        Server server2 = new Server();
        server2.setDescription("QA Server");
        server2.setUrl("http://localhost:8082");
        servers.add(server2);

        Server server3 = new Server();
        server3.setDescription("Production Server");
        server3.setUrl("http://localhost:8083");
        servers.add(server3);

        return servers;
    }
 }
