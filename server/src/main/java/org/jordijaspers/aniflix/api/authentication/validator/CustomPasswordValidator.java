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
                // length between 8 and 16 characters
                new LengthRule(8, 16),
                // at least one upper-case character
                new CharacterRule(EnglishCharacterData.UpperCase, 1),
                // at least one lower-case character
                new CharacterRule(EnglishCharacterData.LowerCase, 1),
                // at least one digit character
                new CharacterRule(EnglishCharacterData.Digit, 1),
                // at least one symbol (special character)
                new CharacterRule(EnglishCharacterData.Special, 1),
                // no whitespace
                new WhitespaceRule(),
                // rejects passwords that contain a sequence of >= 5 characters alphabetical  (e.g. abcdef)
                new IllegalSequenceRule(EnglishSequenceData.Alphabetical, 5, false),
                // rejects passwords that contain a sequence of >= 5 characters numerical   (e.g. 12345)
                new IllegalSequenceRule(EnglishSequenceData.Numerical, 5, false)
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
