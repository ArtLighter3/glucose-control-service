package com.artlighter.glucosecontrolservice.nightscout.controller;

import com.artlighter.glucosecontrolservice.general.exception.ResourceNotFoundException;
import com.artlighter.glucosecontrolservice.nightscout.dto.NightscoutEntryDTO;
import com.artlighter.glucosecontrolservice.nightscout.dto.NightscoutTreatmentDTO;
import com.artlighter.glucosecontrolservice.nightscout.service.NightscoutService;
import com.artlighter.glucosecontrolservice.user.entity.PatientProfile;
import com.artlighter.glucosecontrolservice.user.service.PatientProfileService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Tag(name = "nightscout", description = "API для загрузчиков Nightscout")
@RestController
@RequestMapping("/nightscout/{username}/api/v1")
public class NightscoutAPIController {
    private PatientProfileService patientProfileService;
    private NightscoutService nightscoutService;
    //private DiaryEntryService diaryEntryService;
    private Validator validator;

    public NightscoutAPIController(PatientProfileService patientProfileService,
                                   NightscoutService nightscoutService,
                                 //  DiaryEntryService diaryEntryService,
                                   Validator validator) {
        this.patientProfileService = patientProfileService;
        this.nightscoutService = nightscoutService;
       // this.diaryEntryService = diaryEntryService;
        this.validator = validator;
    }

    @PostMapping("/entries")
    @PreAuthorize("@nightscoutAuthUtils.hasAccessToNightscoutApi(#username, #apiSecret)")
    public List<NightscoutEntryDTO> postEntry(@PathVariable String username,
                                              @RequestHeader("api-secret") String apiSecret,
                                              @RequestBody List<NightscoutEntryDTO> entries) {
        PatientProfile patientProfile = patientProfileService.getByUsername(username);

        List<NightscoutEntryDTO> rejected = new ArrayList<>();
        List<NightscoutEntryDTO> toAdd = getValidated(entries, rejected);

        rejected.addAll(nightscoutService.addGlucoseEntries(toAdd, patientProfile.getUserId(), true));
        return rejected;
    }

    @PostMapping("/treatments")
    @PreAuthorize("@nightscoutAuthUtils.hasAccessToNightscoutApi(#username, #apiSecret)")
    public List<NightscoutTreatmentDTO> postTreatment(@PathVariable String username,
                                                      @RequestHeader("api-secret") String apiSecret,
                                                      @RequestBody List<NightscoutTreatmentDTO> treatments) {
        //TODO профиль уже подгружается при проверке авторизации в @PreAuthorize. Может сделать проверку здесь
        //чтобы два раза не загружать одно и то же?
        PatientProfile patientProfile = patientProfileService.getByUsername(username);

        List<NightscoutTreatmentDTO> rejected = new ArrayList<>();
        List<NightscoutTreatmentDTO> toAdd = getValidated(treatments, rejected);

        rejected.addAll(nightscoutService.addTreatments(toAdd, patientProfile.getUserId(), true));
        return rejected;
    }

    @PutMapping("/treatments")
    @PreAuthorize("@nightscoutAuthUtils.hasAccessToNightscoutApi(#username, #apiSecret)")
    public List<NightscoutTreatmentDTO> putTreatment(@PathVariable String username,
                                                      @RequestHeader("api-secret") String apiSecret,
                                                      @RequestBody NightscoutTreatmentDTO treatment) {
        return postTreatment(username, apiSecret, List.of(treatment));
    }

    private <T> List<T> getValidated(List<T> entries, List<T> containerForRejected) {
        List<T> toAdd = new ArrayList<>();

        for (T t : entries) {
            Set<ConstraintViolation<T>> violations = validator.validate(t);
            if (violations.isEmpty()) toAdd.add(t);
            else if (containerForRejected != null) containerForRejected.add(t);
        }

        return toAdd;
    }
}
