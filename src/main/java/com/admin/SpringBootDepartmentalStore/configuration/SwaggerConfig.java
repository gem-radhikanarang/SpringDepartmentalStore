package com.admin.SpringBootDepartmentalStore.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Departmental Store API",
                version = "1.0",
                description = "API documentation for Departmental Store CRUD application",
                contact = @io.swagger.v3.oas.annotations.info.Contact(
                name = "Radhika Narang",
                email = "radhika.narang@geminisolutions.com"
        )
        ),
        servers = {
                @Server(url = "http://localhost:9111", description = "Local Server")
        }
)
public class SwaggerConfig implements WebMvcConfigurer {


}
