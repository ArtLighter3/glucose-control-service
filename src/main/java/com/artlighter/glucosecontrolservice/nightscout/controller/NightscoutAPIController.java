package com.artlighter.glucosecontrolservice.nightscout.controller;

import com.artlighter.glucosecontrolservice.diary.service.DiaryEntryService;
import com.artlighter.glucosecontrolservice.general.exception.ResourceNotFoundException;
import com.artlighter.glucosecontrolservice.nightscout.dto.NightscoutEntryDTO;
import com.artlighter.glucosecontrolservice.nightscout.dto.NightscoutTreatmentDTO;
import com.artlighter.glucosecontrolservice.nightscout.service.NightscoutService;
import com.artlighter.glucosecontrolservice.user.entity.PatientProfile;
import com.artlighter.glucosecontrolservice.user.service.PatientProfileService;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Valid;
import jakarta.validation.Validator;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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
        PatientProfile patientProfile = getPatientProfileOrThrowException(username);

        List<NightscoutEntryDTO> rejected = new ArrayList<>();
        List<NightscoutEntryDTO> toAdd = getValidated(entries, rejected);

        rejected.addAll(nightscoutService.addGlucoseEntries(toAdd, patientProfile, true));
        return rejected;
    }

    @PostMapping("/treatments")
    @PreAuthorize("@nightscoutAuthUtils.hasAccessToNightscoutApi(#username, #apiSecret)")
    public List<NightscoutTreatmentDTO> postTreatment(@PathVariable String username,
                                                      @RequestHeader("api-secret") String apiSecret,
                                                      @RequestBody List<NightscoutTreatmentDTO> treatments) {
        //TODO профиль уже подгружается при проверке авторизации в @PreAuthorize. Может сделать проверку здесь
        //чтобы два раза не загружать одно и то же?
        PatientProfile patientProfile = getPatientProfileOrThrowException(username);

        List<NightscoutTreatmentDTO> rejected = new ArrayList<>();
        List<NightscoutTreatmentDTO> toAdd = getValidated(treatments, rejected);

        rejected.addAll(nightscoutService.addTreatments(toAdd, patientProfile, true));
        return rejected;
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

    private PatientProfile getPatientProfileOrThrowException(String username) {
        PatientProfile patientProfile = patientProfileService.getByUsername(username);
        if (patientProfile == null) throw new ResourceNotFoundException("patient not found");

        return patientProfile;
    }
}
