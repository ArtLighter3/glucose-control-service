package com.artlighter.glucosecontrolservice.auth.controller.diary;

import com.artlighter.glucosecontrolservice.diary.service.DiaryEntryService;
import com.artlighter.glucosecontrolservice.diary.dto.InsulinEntryDTO;
import com.artlighter.glucosecontrolservice.diary.entity.entry.InsulinEntry;
import com.artlighter.glucosecontrolservice.user.service.PatientProfileService;
import com.artlighter.glucosecontrolservice.diary.util.DiaryEntryType;
import com.artlighter.glucosecontrolservice.diary.util.mapper.InsulinEntryMapper;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.ZoneOffset;
import java.util.List;

@RestController
public class InsulinEntryController extends AbstractDiaryEntryController<InsulinEntry, InsulinEntryDTO> {
    public InsulinEntryController(DiaryEntryService diaryEntryService,
                                  PatientProfileService patientProfileService,
                                  InsulinEntryMapper entryMapper) {
        super(diaryEntryService, patientProfileService, entryMapper);
    }

    @Override
    @Operation(summary = "Получить записи с вводами инсулина за временной период.",
            description = "Рекомендуется указать UTC-смещение пользователя, к которому будут преобразованы" +
                    "временные отметки записей. Иначе они будут по UTC+0. Если не указать нижнюю границу временного " +
                    "периода выборки, то выберутся записи в течение недели до верхней границы. " +
                    "Если не указать верхнюю границу, то верхней границей считается текущий момент времени.")
    @GetMapping("/insulin")
    public List<InsulinEntryDTO> getDiaryEntries(int userId, Instant from, Instant to, ZoneOffset outputZoneOffset) {
        return super.getDiaryEntries(userId, from, to, outputZoneOffset);
    }

    @Override
    @Operation(summary = "Добавить запись с вводом инсулина.")
    @PostMapping("/insulin")
    public InsulinEntryDTO postDiaryEntry(int userId, InsulinEntryDTO entryDTO, BindingResult bindingResult) {
        return super.postDiaryEntry(userId, entryDTO, bindingResult);
    }

    @Override
    @Operation(summary = "Обновить существующую запись с вводом инсулина.")
    @PutMapping("/insulin")
    public InsulinEntryDTO putDiaryEntry(int userId, InsulinEntryDTO entryDTO, BindingResult bindingResult) {
        return super.putDiaryEntry(userId, entryDTO, bindingResult);
    }

    @Override
    @Operation(summary = "Удалить существующую запись с вводом инсулина.")
    @DeleteMapping("/insulin")
    public void deleteDiaryEntry(int userId, Instant commitedAt) {
        super.deleteDiaryEntry(userId, commitedAt);
    }

    @Override
    protected DiaryEntryType getEntryType() {
        return DiaryEntryType.INSULIN_ENTRY;
    }
}
