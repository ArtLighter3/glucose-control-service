package com.artlighter.glucosecontrolservice.general;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.CommonsRequestLoggingFilter;

import java.util.List;

@Configuration
public class GeneralConfig {

    @Bean
    public CommonsRequestLoggingFilter logFilter() {
        CommonsRequestLoggingFilter filter
                = new CommonsRequestLoggingFilter();
        filter.setIncludeQueryString(true);
        filter.setIncludePayload(true);
        filter.setMaxPayloadLength(10000);
        filter.setIncludeHeaders(false);
        filter.setAfterMessagePrefix("REQUEST DATA: ");
        return filter;
    }

    @Bean
    public OpenAPI defineOpenAPI () {
        Server server = new Server();
        server.setUrl("/api/v1");
        server.setDescription("Main back-end API");

        Info info = new Info()
                .title("Системное API для сервиса")
                .version("1.0")
                .description("API предоставляет методы для получения и модификации пользователей, настроек системы," +
                        "профилей пользователей (в т.ч. пациентов, профили инсулина), записей дневника самоконтроля," +
                        "а также совершения вычислений инсулина.");
        return new OpenAPI().info(info).servers(List.of(server));
    }
}
