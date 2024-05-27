package org.jordijaspers.aniflix.api.user.validator;

import lombok.RequiredArgsConstructor;
import org.hawaiiframework.validation.ValidationException;
import org.hawaiiframework.validation.ValidationResult;
import org.hawaiiframework.validation.Validator;
import org.jordijaspers.aniflix.api.authentication.validator.CustomPasswordValidator;
import org.jordijaspers.aniflix.api.user.model.request.UpdatePasswordRequest;
import org.passay.RuleResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import static java.util.Objects.isNull;

/**
 * {@inheritDoc}.
 */
@Component
@RequiredArgsConstructor
@SuppressWarnings("MultipleStringLiterals")
public class ChangePasswordValidator implements Validator<UpdatePasswordRequest> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ChangePasswordValidator.class);

    private final CustomPasswordValidator passwordValidator;

    /**
     * {@inheritDoc}
     */
    @Override
    public void validate(final UpdatePasswordRequest request, final ValidationResult result) {
        if (isNull(request)) {
            result.reject("Request body is missing, please provide a request body with the correct configuration");
            throw new ValidationException(result);
        }

        result.rejectField("newPassword", request.getNewPassword())
                .whenNull("password must be specified")
                .orWhen(String::isEmpty, "password must not be empty");

        result.rejectField("confirmPassword", request.getConfirmPassword())
                .whenNull("password must be confirmed")
                .orWhen(String::isEmpty, "confirmation field must not be empty")
                .orWhen(confirmation -> !confirmation.equals(request.getNewPassword()), "Password does not match the confirmation");

        if (result.hasErrors()) {
            throw new ValidationException(result);
        }

        final RuleResult passwordValidationResult = passwordValidator.validatePassword(request.getNewPassword());
        result.rejectField("password", request.getNewPassword())
                .when(password -> !passwordValidationResult.isValid(), "Password is not strong enough");

        if (result.hasErrors()) {
            LOGGER.debug("Password validation failed for user with email '{}' with errors: {}",
                    request.getNewPassword(), passwordValidationResult.getDetails());
            throw new ValidationException(result);
        }
    }
}
