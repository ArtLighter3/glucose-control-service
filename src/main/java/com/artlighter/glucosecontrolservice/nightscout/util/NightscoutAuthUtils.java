package com.artlighter.glucosecontrolservice.nightscout.util;

import com.artlighter.glucosecontrolservice.user.service.PatientProfileService;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Component
public class NightscoutAuthUtils {
    private PatientProfileService patientProfileService;

    public NightscoutAuthUtils(PatientProfileService patientProfileService) {
        this.patientProfileService = patientProfileService;
    }

    public boolean hasAccessToNightscoutApi(String username, String providedNightscoutApiSecret) {
        if (providedNightscoutApiSecret == null) return false;

        String internalApiSecret = sha1Hash(patientProfileService.getNightscoutApiSecretIfEnabled(username));
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
