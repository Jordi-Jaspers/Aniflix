package org.jordijaspers.aniflix.api.authentication.validator;

import org.passay.*;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * A custom password strength validator.
 */
@Component
public class CustomPasswordValidator {

    private final PasswordValidator passwordValidator;

    public CustomPasswordValidator() {
        this.passwordValidator = new PasswordValidator(List.of(
                // length at least 8 characters
                new LengthRule(8, 100),
                // at least one upper-case character
                new CharacterRule(EnglishCharacterData.UpperCase, 1),
                // at least one lower-case character
                new CharacterRule(EnglishCharacterData.LowerCase, 1),
                // at least one digit character
                new CharacterRule(EnglishCharacterData.Digit, 1),
                // at least one symbol (special character)
                new CharacterRule(EnglishCharacterData.Special, 1),
                // no whitespace
                new WhitespaceRule()
        ));
    }

    public boolean isWeakPassword(final String password) {
        final RuleResult result = passwordValidator.validate(new PasswordData(password));
        return !result.isValid();
    }

    public RuleResult validatePassword(final String password) {
        return passwordValidator.validate(new PasswordData(password));
    }
}
