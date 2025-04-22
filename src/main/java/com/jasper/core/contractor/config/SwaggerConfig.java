package com.jasper.core.contractor.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI springShopOpenAPI() {
        OpenAPI openAPI=new OpenAPI();
        setOpenApi(openAPI,"v1");
        return openAPI;
    }

    private void setOpenApi(OpenAPI openApi, String version) {

        Server remoteServer = new Server();
        remoteServer.setUrl("https://be-jasper-contractor-test.up.railway.app"  );
        remoteServer.setDescription("Remote Server");
        Server localServer = new Server();
        localServer.setUrl("http://127.0.0.1:8080"  );
        localServer.setDescription("Local Server");

        Info info = new Info();
        info.setVersion(version);
        Contact contact = new Contact();
        info.setContact(contact);
        info.setSummary("Contractors Web Docs");
        info.setTitle("Contractors Open API");
        info.setDescription("Explore our guides and examples to integrate Contractors.");
        openApi.info(info);

        openApi.setServers(List.of(remoteServer,localServer));
    }

}

