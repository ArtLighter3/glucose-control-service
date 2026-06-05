package com.artlighter.glucosecontrolservice.user.util;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@Component
public class CodeGenerator {
    private static final String ALPHABET = "23456789ABCDEFGHJKMNPQRSTUVWXYZ";

    private SecureRandom random = new SecureRandom();

    /**
     * Генерирует код из букв и цифр с длиной codeLength.
     * @param codeLength длина кода; если передано 0 или меньше, то используется длина 8;
     * @return код в виде строки;
     */
    public String generateAlphaNumericCode(int codeLength) {
        if (codeLength <= 0) codeLength = 8;

        StringBuilder code = new StringBuilder(codeLength);
        for (int i = 0; i < codeLength; i++) {
            code.append(ALPHABET.charAt(random.nextInt(ALPHABET.length())));
        }

        return code.toString();
    }
}
