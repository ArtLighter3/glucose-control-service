package com.artlighter.glucosecontrolservice.authgateway.controller.diary;

import com.artlighter.glucosecontrolservice.authgateway.util.exception.ExceptionDTO;
import com.artlighter.glucosecontrolservice.diary.dto.DiaryEntryWithTypeDTO;
import com.artlighter.glucosecontrolservice.diary.service.DiaryEntryService;
import com.artlighter.glucosecontrolservice.diary.dto.DiaryEntryDTO;
import com.artlighter.glucosecontrolservice.general.dto.CustomSliceMetadata;
import com.artlighter.glucosecontrolservice.general.dto.CustomSlicedModel;
import com.artlighter.glucosecontrolservice.user.entity.PatientProfile;
import com.artlighter.glucosecontrolservice.diary.entity.entry.DiaryEntry;
import com.artlighter.glucosecontrolservice.user.service.PatientProfileService;
import com.artlighter.glucosecontrolservice.diary.util.mapper.DiaryEntryCollectionMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.ZoneOffset;
import java.util.Collections;
import java.util.List;

@Tag(name = "diary", description = "методы для ведения дневника самоконтроля: " +
        "добавление, модификация записей разных типов")
@ApiResponses(value =
        {@ApiResponse(responseCode = "404", description = "Если больной не был найден.",
                content = @Content(schema = @Schema(implementation = ExceptionDTO.class))),
        @ApiResponse(responseCode = "500", description = "Ошибка сервера.",
                content = @Content(schema = @Schema(implementation = ExceptionDTO.class)))})
@RestController
@RequestMapping("api/v1/patients/{userId}/entries")
public class DiaryEntryCollectionController {
    private DiaryEntryService diaryEntryService;
    private PatientProfileService patientProfileService;
    private DiaryEntryCollectionMapper collectionMapper;

    public DiaryEntryCollectionController(DiaryEntryService diaryEntryService,
                                          PatientProfileService patientProfileService,
                                          DiaryEntryCollectionMapper collectionMapper) {
        this.diaryEntryService = diaryEntryService;
        this.patientProfileService = patientProfileService;
        this.collectionMapper = collectionMapper;
    }

    @Operation(summary = "Получить все записи дневника всех типов в заданном периоде времени (постранично).",
            description = "Рекомендуется указать UTC-смещение пользователя, к которому будут преобразованы " +
                    "временные отметки записей. Иначе они будут по UTC+0.")
    @GetMapping
    @PreAuthorize("@resourceAccessInspector.hasAccessToPatientResource(#userId, authentication, true, false)")
    public CustomSlicedModel<DiaryEntryWithTypeDTO> getAllEntries(@PathVariable int userId,
                                                                  @RequestParam(required = false) Instant from,
                                                                  @RequestParam(required = false) Instant to,
                                                                  @RequestParam(required = false) ZoneOffset outputZoneOffset,
                                                                  @PageableDefault(size = 20, page = 0,
                                                             sort = "commitedAt", direction = Sort.Direction.DESC)
                                                     @Parameter(description = "Данные о странице и сортировке. " +
                                                                 "По-умолчанию сортируется по дате совершения (убыв.)")
                                                     Pageable pageable) {
        PatientProfile patientProfile = patientProfileService.getByUserId(userId);

        Slice<DiaryEntry> entries =
                diaryEntryService.getAllDiaryEntries(patientProfile.getUserId(), from, to, pageable);

        return new CustomSlicedModel<DiaryEntryWithTypeDTO>(collectionMapper
                .mapToDTO(entries.getContent(), patientProfile, outputZoneOffset),
                new CustomSliceMetadata(entries.getSize(), entries.getNumber(), entries.hasNext()));
    }

    @Operation(summary = "Добавить весь список записей разных типов.",
            description = "Возвращает список тех записей, которые не удалось добавить из-за некорректности их " +
                    "значений. Если запись уже существует, то она будет обновлена.")
    @ApiResponses(value =
            {@ApiResponse(responseCode = "200", description = "В случае успеха. Даже если какие-то из записей" +
                    "некорректны."),
            @ApiResponse(responseCode = "400", description = "Если тело запроса (сам список) некорректное." +
                    "В случае некорректности одной из записей она будет возвращена в ответе с кодом 200.",
                    content = @Content(schema = @Schema(implementation = ExceptionDTO.class)))})
    @PostMapping
    @PreAuthorize("@resourceAccessInspector.hasAccessToPatientResource(#userId, authentication, false, false)")
    public List<DiaryEntryDTO> postAllEntries(@PathVariable int userId,
                                              @RequestBody @Valid List<DiaryEntryWithTypeDTO> entries, BindingResult bindingResult) {
        //TODO
        return Collections.emptyList();

//        PatientProfile patientProfile = patientProfileService.getByUserId(userId);
//
//        if (bindingResult.hasErrors()) {}
//
//        List<DiaryEntry> added
//                = diaryEntryService.addDiaryEntries(collectionMapper.mapToInternal(entries, patientProfile),
//                        patientProfile.getUserId(), true);
//
//        return collectionMapper.mapToDTO(added, patientProfile, ZoneOffset.UTC);
    }
}
