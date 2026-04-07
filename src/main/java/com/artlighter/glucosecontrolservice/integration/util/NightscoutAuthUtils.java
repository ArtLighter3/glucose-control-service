package com.artlighter.glucosecontrolservice.integration.util;

import com.artlighter.glucosecontrolservice.general.exception.ResourceNotFoundException;
import com.artlighter.glucosecontrolservice.integration.entity.IntegrationProfile;
import com.artlighter.glucosecontrolservice.integration.service.IntegrationProfileService;
import com.artlighter.glucosecontrolservice.user.service.PatientProfileService;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Component
public class NightscoutAuthUtils {
    private IntegrationProfileService integrationProfileService;

    public NightscoutAuthUtils(IntegrationProfileService integrationProfileService) {
        this.integrationProfileService = integrationProfileService;
    }

    public boolean hasAccessToNightscoutApi(int patientId, String providedNightscoutApiSecret) {
        if (providedNightscoutApiSecret == null) return false;

        IntegrationProfile integrationProfile = null;
        try {
            integrationProfile = integrationProfileService.getByPatientProfileId(patientId);
        } catch (ResourceNotFoundException ex) {
            return false;
        }

        if (!integrationProfile.isNightscoutEnabled()) return false;

        String internalApiSecret = sha1Hash(integrationProfile.getNightscoutApiSecret());
        if (internalApiSecret == null) return false;

        return internalApiSecret.equals(providedNightscoutApiSecret);
    }

    private String sha1Hash(String input) {
        if (input == null) return null;

        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            byte[] digestBytes = md.digest(input.getBytes(StandardCharsets.UTF_8));

            StringBuilder sb = new StringBuilder();
            for (byte b : digestBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();

        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }
}
