package com.artlighter.glucosecontrolservice.calculations.dto;

import com.artlighter.glucosecontrolservice.calculations.util.validation.CorrectCalculationRequest;
import com.artlighter.glucosecontrolservice.general.TypeGroup;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.GroupSequence;
import jakarta.validation.constraints.*;

import java.time.LocalTime;

@Schema(name = "InsulinCalculationRequest",
        description = "Запрос на расчет инсулина с нужными параметрами и настройками расчета")
@GroupSequence({InsulinCalculationRequestDTO.class, TypeGroup.class})
@CorrectCalculationRequest(groups = {TypeGroup.class})
public record InsulinCalculationRequestDTO(
        @Schema(description = "Количество углеводов в граммах для расчета компенсации", example = "60.5")
        @NotNull
        @DecimalMin("0")
        @DecimalMax("300")
        Float carbs,
        @Schema(description = "Уровень глюкозы для расчета корректировки до целевого значения." +
                "Должно быть указано обязательно, если correctGlucoseLevel = true", example = "9.7")
        @DecimalMin("0.5")
        @DecimalMax("40")
        Float glucose,
        @Schema(description = "Время дня пользователя. Нужно для определения параметров, изменяемых по времени суток" +
                "(ISF, ICR...). Точное местное время дня пользователя без преобразований по временным зонам!",
                example = "16:30")
        @NotNull
        LocalTime localTimeOfDay,
        @Schema(description = "Учитывать ли активный инсулин. Активный инсулин будет рассчитываться " +
                "исходя из недавних записей ввода инсулина.")
        @NotNull
        Boolean considerActiveInsulin,
        @Schema(description = "Рассчитывать ли дополнительную дозу для корректировки " +
                "уровня глюкозы до целевого значения. Целевое значение определяется как уровень высокой глюкозы по " +
                "настройкам в профиле больного.")
        @NotNull
        Boolean correctGlucoseLevel,
        @Schema(description = "Значение коррекции")
        Float correction
) {
}
