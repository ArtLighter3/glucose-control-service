package com.artlighter.glucosecontrolservice.integration.dto;

import com.artlighter.glucosecontrolservice.general.TypeGroup;
import com.artlighter.glucosecontrolservice.integration.util.validation.CorrectApiSettings;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.GroupSequence;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(name = "IntegrationProfile", description = "профиль пользователя-больного с настройками интеграции с " +
        "другими сервисами")
@GroupSequence({IntegrationProfileDTO.class, TypeGroup.class})
@CorrectApiSettings(groups = {TypeGroup.class})
public record IntegrationProfileDTO(
        @Schema(description = "Включено ли API для интеграции с загрузчиками Nightscout. Если true, то " +
        "обязательно должен быть передан nightscoutApiSecret.")
        @NotNull
        Boolean isNightscoutEnabled,
        @Schema(description = "API-ключ для загрузчиков Nightscout")
        @Size(min = 12, max = 255)
        String nightscoutApiSecret
) {
}
