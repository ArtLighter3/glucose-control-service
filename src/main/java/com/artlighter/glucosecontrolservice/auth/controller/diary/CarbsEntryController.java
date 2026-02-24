package com.artlighter.glucosecontrolservice.auth.controller.diary;

import com.artlighter.glucosecontrolservice.diary.service.DiaryEntryService;
import com.artlighter.glucosecontrolservice.diary.dto.DiaryEntryDeleteDTO;
import com.artlighter.glucosecontrolservice.diary.dto.CarbsEntryDTO;
import com.artlighter.glucosecontrolservice.diary.entity.entry.CarbsEntry;
import com.artlighter.glucosecontrolservice.user.service.PatientProfileService;
import com.artlighter.glucosecontrolservice.diary.util.DiaryEntryType;
import com.artlighter.glucosecontrolservice.diary.util.mapper.CarbsEntryMapper;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.ZoneOffset;
import java.util.List;

@RestController
public class CarbsEntryController extends AbstractDiaryEntryController<CarbsEntry, CarbsEntryDTO> {
    public CarbsEntryController(DiaryEntryService diaryEntryService,
                                PatientProfileService patientProfileService,
                                CarbsEntryMapper entryMapper) {
        super(diaryEntryService, patientProfileService, entryMapper);
    }

    @Override
    @Operation(summary = "Получить записи с принятыми углеводами за временной период.",
            description = "Рекомендуется указать UTC-смещение пользователя, к которому будут преобразованы" +
                    "временные отметки записей. Иначе они будут по UTC+0. Если не указать нижнюю границу временного " +
                    "периода выборки, то выберутся записи в течение недели до верхней границы. " +
                    "Если не указать верхнюю границу, то верхней границей считается текущий момент времени.")
    @GetMapping("/carbs")
    public List<CarbsEntryDTO> getDiaryEntries(int userId, Instant from, Instant to, ZoneOffset outputZoneOffset) {
        return super.getDiaryEntries(userId, from, to, outputZoneOffset);
    }

    @Override
    @Operation(summary = "Добавить запись с принятыми углеводами.")
    @PostMapping("/carbs")
    public CarbsEntryDTO postDiaryEntry(int userId, CarbsEntryDTO entryDTO, BindingResult bindingResult) {
        return super.postDiaryEntry(userId, entryDTO, bindingResult);
    }

    @Override
    @Operation(summary = "Обновить существующую запись с принятыми углеводами.")
    @PutMapping("/carbs")
    public CarbsEntryDTO putDiaryEntry(int userId, CarbsEntryDTO entryDTO, BindingResult bindingResult) {
        return super.putDiaryEntry(userId, entryDTO, bindingResult);
    }

    @Override
    @Operation(summary = "Удалить существующую запись с принятыми углеводами.")
    @DeleteMapping("/carbs")
    public DiaryEntryDeleteDTO deleteDiaryEntry(int userId, DiaryEntryDeleteDTO entryDTO, BindingResult bindingResult) {
        return super.deleteDiaryEntry(userId, entryDTO, bindingResult);
    }

    @Override
    protected DiaryEntryType getEntryType() {
        return DiaryEntryType.CARBS_ENTRY;
    }
}
