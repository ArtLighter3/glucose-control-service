package com.artlighter.glucosecontrolservice.user.dto;

public record AttachedPatientDTO(String lastName,
                                 String firstName,
                                 String middleName,
                                 Integer patientId,
                                 Integer patientProfileId,
                                 String email) {
}
