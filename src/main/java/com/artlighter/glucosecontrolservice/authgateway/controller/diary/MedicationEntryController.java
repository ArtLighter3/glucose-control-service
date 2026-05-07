package com.artlighter.glucosecontrolservice.authgateway.controller.diary;

import com.artlighter.glucosecontrolservice.diary.service.DiaryEntryService;
import com.artlighter.glucosecontrolservice.diary.dto.MedicationEntryDTO;
import com.artlighter.glucosecontrolservice.diary.entity.entry.MedicationEntry;
import com.artlighter.glucosecontrolservice.general.dto.CustomSlicedModel;
import com.artlighter.glucosecontrolservice.user.service.PatientProfileService;
import com.artlighter.glucosecontrolservice.diary.util.DiaryEntryType;
import com.artlighter.glucosecontrolservice.diary.util.mapper.MedicationEntryMapper;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.ZoneOffset;

@RestController
public class MedicationEntryController extends AbstractDiaryEntryController<MedicationEntry, MedicationEntryDTO> {
    public MedicationEntryController(DiaryEntryService diaryEntryService,
                                     PatientProfileService patientProfileService,
                                     MedicationEntryMapper entryMapper) {
        super(diaryEntryService, patientProfileService, entryMapper);
    }

    @Override
    @Operation(summary = "Получить записи с принятиями лекарств за временной период (постранично).",
            description = "Рекомендуется указать UTC-смещение пользователя, к которому будут преобразованы" +
                    "временные отметки записей. Иначе они будут по UTC+0. Если не указать нижнюю границу временного " +
                    "периода выборки, то выберутся записи в течение недели до верхней границы. " +
                    "Если не указать верхнюю границу, то верхней границей считается текущий момент времени.")
    @GetMapping("/medication")
    public CustomSlicedModel<MedicationEntryDTO> getDiaryEntries(int userId, Instant from, Instant to,
                                                                 ZoneOffset outputZoneOffset, Pageable pageable) {
        return super.getDiaryEntries(userId, from, to, outputZoneOffset, pageable);
    }

    @Override
    @Operation(summary = "Добавить запись с принятием лекарства.")
    @PostMapping("/medication")
    public MedicationEntryDTO postDiaryEntry(int userId, MedicationEntryDTO entryDTO, BindingResult bindingResult) {
        return super.postDiaryEntry(userId, entryDTO, bindingResult);
    }

    @Override
    @Operation(summary = "Обновить существующую запись с принятием лекарства.")
    @PutMapping("/medication")
    public MedicationEntryDTO putDiaryEntry(int userId, MedicationEntryDTO entryDTO, BindingResult bindingResult) {
        return super.putDiaryEntry(userId, entryDTO, bindingResult);
    }

    @Override
    @Operation(summary = "Удалить существующую запись с принятием лекарства.")
    @DeleteMapping("/medication")
    public void deleteDiaryEntry(int userId, Instant commitedAt) {
        super.deleteDiaryEntry(userId, commitedAt);
    }

    @Override
    protected DiaryEntryType getEntryType() {
        return DiaryEntryType.MEDICATION_ENTRY;
    }
}
