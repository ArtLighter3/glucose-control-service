package com.artlighter.glucosecontrolservice.authgateway.controller.diary;

import com.artlighter.glucosecontrolservice.authgateway.util.validation.ConvertableValueRangeValidator;
import com.artlighter.glucosecontrolservice.authgateway.util.exception.ConvertableValueValidationException;
import com.artlighter.glucosecontrolservice.authgateway.util.exception.ValidationIsFailedException;
import com.artlighter.glucosecontrolservice.diary.service.DiaryEntryService;
import com.artlighter.glucosecontrolservice.diary.dto.GlucoseEntryDTO;
import com.artlighter.glucosecontrolservice.diary.entity.entry.GlucoseEntry;
import com.artlighter.glucosecontrolservice.user.entity.PatientProfile;
import com.artlighter.glucosecontrolservice.user.service.PatientProfileService;
import com.artlighter.glucosecontrolservice.diary.util.DiaryEntryType;
import com.artlighter.glucosecontrolservice.diary.util.mapper.GlucoseEntryMapper;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.ZoneOffset;
import java.util.List;

@RestController
public class GlucoseEntryController extends AbstractDiaryEntryController<GlucoseEntry, GlucoseEntryDTO> {
    private ConvertableValueRangeValidator convertableValueRangeValidator;

    public GlucoseEntryController(DiaryEntryService diaryEntryService,
                                  PatientProfileService patientProfileService,
                                  GlucoseEntryMapper entryMapper,
                                  ConvertableValueRangeValidator convertableValueRangeValidator) {
        super(diaryEntryService, patientProfileService, entryMapper);
        this.convertableValueRangeValidator = convertableValueRangeValidator;
    }

    @Override
    @Operation(summary = "Получить записи с измерениями глюкозы за временной период.",
            description = "Значение глюкозы в возвращенном объекте будет в тех единицах измерения, которые" +
                    "выставлены в профиле соответствующего больного. " +
                    "Рекомендуется указать UTC-смещение пользователя, к которому будут преобразованы" +
                    "временные отметки записей. Иначе они будут по UTC+0. Если не указать нижнюю границу временного " +
                    "периода выборки, то выберутся записи в течение недели до верхней границы. " +
                    "Если не указать верхнюю границу, то верхней границей считается текущий момент времени.")
    @GetMapping("/glucose")
    public List<GlucoseEntryDTO> getDiaryEntries(int userId, Instant from, Instant to, ZoneOffset outputZoneOffset) {
        return super.getDiaryEntries(userId, from, to, outputZoneOffset);
    }

    @Override
    @Operation(summary = "Добавить запись с измерением глюкозы.")
    @PostMapping("/glucose")
    public GlucoseEntryDTO postDiaryEntry(int userId, GlucoseEntryDTO entryDTO, BindingResult bindingResult) {
        return super.postDiaryEntry(userId, entryDTO, bindingResult);
    }

    @Override
    @Operation(summary = "Обновить существующую запись с измерением глюкозы.")
    @PutMapping("/glucose")
    public GlucoseEntryDTO putDiaryEntry(int userId, GlucoseEntryDTO entryDTO, BindingResult bindingResult) {
        return super.putDiaryEntry(userId, entryDTO, bindingResult);
    }

    @Override
    @Operation(summary = "Удалить существующую запись с измерением глюкозы.")
    @DeleteMapping("/glucose")
    public void deleteDiaryEntry(int userId, Instant commitedAt) {
        super.deleteDiaryEntry(userId, commitedAt);
    }

    @Override
    protected DiaryEntryType getEntryType() {
        return DiaryEntryType.GLUCOSE_ENTRY;
    }

    @Override
    protected void doAdditionalValidation(BindingResult bindingResult,
                                          GlucoseEntryDTO entryDTO,
                                          PatientProfile patientProfile) {
        //GlucoseEntry содержит значение глюкозы, которые передается в разных единицах измерения
        // в зависимости от настроек больного. Необходима валидация диапазона в зависимости от единицы измерения;
        super.doAdditionalValidation(bindingResult, entryDTO, patientProfile);
        try {
            convertableValueRangeValidator.isGlucoseValid(entryDTO.value(), patientProfile.getGlucoseUnit());
        } catch (ConvertableValueValidationException ex) {
            bindingResult.rejectValue("value", "not_in_range", ex.getMessage());
            throw new ValidationIsFailedException(bindingResult);
        }
    }
}
