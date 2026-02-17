package com.artlighter.glucosecontrolservice.nightscout.util;

import com.artlighter.glucosecontrolservice.user.service.PatientProfileService;
import org.springframework.stereotype.Component;

@Component
public class NightscoutAuthUtils {
    private PatientProfileService patientProfileService;

    public NightscoutAuthUtils(PatientProfileService patientProfileService) {
        this.patientProfileService = patientProfileService;
    }

    public boolean hasAccessToNightscoutApi(String username, String providedNightscoutApiSecret) {
        if (providedNightscoutApiSecret == null) return false;

        String internalApiSecret = patientProfileService.getNightscoutApiSecretIfEnabled(username);
        if (internalApiSecret == null) return false;

        return internalApiSecret.equals(providedNightscoutApiSecret);
    }
}
