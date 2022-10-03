package com.hospyboard.api.app.user.services;

import com.hospyboard.api.app.user.dto.UserDTO;
import fr.funixgaming.api.core.exceptions.ApiBadRequestException;
import org.springframework.stereotype.Component;

@Component
public class UserUtils {

    public void checkUserPassword(final UserDTO request) throws ApiBadRequestException {
        if (request.getPassword() == null) return;

        if (request.getPassword().length() < 8) {
            throw new ApiBadRequestException("Votre mot de passe doit contenir au minimum 8 caractères.");
        } else if (!isPasswordContainsUpperCases(request.getPassword())) {
            throw new ApiBadRequestException("Votre mot de passe doit contenir au moins deux majuscules.");
        } else if (!isPasswordContainsNumbers(request.getPassword())) {
            throw new ApiBadRequestException("Votre mot de passe doit contenir au moins deux chiffres.");
        }
    }

    private boolean isPasswordContainsNumbers(final String password) {
        int numbersCount = 0;

        for (char c : password.toCharArray()) {
            if (c >= '0' && c <= '9') {
                ++numbersCount;
            }
        }
        return numbersCount >= 2;
    }

    private boolean isPasswordContainsUpperCases(final String password) {
        int upperCaseCount = 0;

        for (char c : password.toCharArray()) {
            if (c >= 'A' && c <= 'Z') {
                ++upperCaseCount;
            }
        }
        return upperCaseCount >= 2;
    }

}
