package com.artlighter.glucosecontrolservice.general;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.ObjectSchema;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.parameters.HeaderParameter;
import io.swagger.v3.oas.models.parameters.RequestBody;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.web.filter.CommonsRequestLoggingFilter;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;
import java.util.Locale;

@Configuration
@EnableSpringDataWebSupport(pageSerializationMode = EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO)
public class GeneralConfig {

    @Bean
    public LocaleResolver localeResolver() {
        AcceptHeaderLocaleResolver slr = new AcceptHeaderLocaleResolver();
        slr.setDefaultLocale(Locale.of("ru", "RU"));
        return slr;
    }

    @Bean
    public DecimalFormat decimalFormat() {
        return new DecimalFormat("#.##", new DecimalFormatSymbols(Locale.US));
    }

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
        server.setDescription("Main back-end API");

        Info info = new Info()
                .title("Системное API для сервиса")
                .version("1.0")
                .description("API предоставляет методы для получения и модификации пользователей, настроек системы, " +
                        "профилей пользователей (в т.ч. пациентов, профили инсулина), записей дневника самоконтроля, " +
                        "а также совершения вычислений инсулина.");

        return new OpenAPI()
                .info(info)
                .servers(List.of(server))
                .components(new Components()
                        .addSecuritySchemes("sessionAuth", new SecurityScheme()
                                .type(SecurityScheme.Type.APIKEY)
                                .in(SecurityScheme.In.COOKIE)
                                .name("JSESSIONID"))
                        .addSecuritySchemes("csrf", new SecurityScheme()
                                .type(SecurityScheme.Type.APIKEY)
                                .in(SecurityScheme.In.HEADER)
                                .name("X-XSRF-TOKEN")));
    }

    @Bean
    public OpenApiCustomizer springSecurityLoginCustomizer() {
        //Swagger-описание логин эндпоинта, что генерируется Spring Security. Автоматическое распознавание
        // неправильный формат входных данных определяет
        return openApi -> {
            if (openApi.getPaths() == null) {
                openApi.setPaths(new io.swagger.v3.oas.models.Paths());
            }

            if (!openApi.getPaths().containsKey("/api/v1/auth/process-login")) {
                ObjectSchema formSchema = new ObjectSchema();
                formSchema.addProperty("username", new StringSchema().description("Имя пользователя"));
                formSchema.addProperty("password", new StringSchema().description("Пароль")
                        .type("string").format("password"));

                MediaType mediaType = new MediaType().schema(formSchema);
                Content content = new Content().addMediaType("application/x-www-form-urlencoded", mediaType);
                RequestBody requestBody = new RequestBody().description("Данные для проведения логина")
                        .required(true)
                        .content(content);

                ApiResponses responses = new ApiResponses()
                        .addApiResponse("200", new ApiResponse().description("В случае успеха"))
                        .addApiResponse("400", new ApiResponse().description("Нет пользователя / неверный пароль"));

                Operation postOperation = new Operation()
                        .summary("Произвести логин в систему")
                        .addTagsItem("auth")
                        .requestBody(requestBody)
                        .responses(responses)
                        .addSecurityItem(new SecurityRequirement().addList("csrf"));

                PathItem loginPathItem = new PathItem().post(postOperation);
                openApi.getPaths().addPathItem("/api/v1/auth/process-login", loginPathItem);

            }
        };
    }
}
