package org.jordijaspers.aniflix.api.authentication.validator;

import lombok.RequiredArgsConstructor;
import org.hawaiiframework.validation.ValidationException;
import org.hawaiiframework.validation.ValidationResult;
import org.hawaiiframework.validation.Validator;
import org.jordijaspers.aniflix.api.authentication.model.request.LoginRequest;
import org.jordijaspers.aniflix.api.authentication.model.request.RegisterUserRequest;
import org.passay.RuleResult;
import org.springframework.stereotype.Component;

import static java.lang.String.format;
import static java.util.Objects.isNull;

/**
 * {@inheritDoc}.
 */
@Component
@RequiredArgsConstructor
@SuppressWarnings("MultipleStringLiterals")
public class AuthenticationValidator implements Validator<Object> {

    private static final String OWASP_EMAIL_REGEX = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,}$";

    private final CustomPasswordValidator passwordValidator;

    /**
     * {@inheritDoc}
     */
    @Override
    public void validate(final Object input, final ValidationResult result) {
        if (isNull(input)) {
            result.reject("Request body is missing, please provide a request body with the correct configuration");
            throw new ValidationException(result);
        }
    }

    public void validateUserRegistration(final RegisterUserRequest request) {
        final ValidationResult result = new ValidationResult();
        if (isNull(request)) {
            result.reject("Request body is missing, please provide a request body with the correct configuration");
            throw new ValidationException(result);
        }

        result.rejectField("email", request.getEmail())
                .whenNull("email must be specified")
                .orWhen(String::isEmpty, "email must not be empty")
                .orWhen(email -> !email.contains("@"), "email must contain an @")
                .orWhen(email -> !email.matches(OWASP_EMAIL_REGEX), "email must be a valid email address");

        result.rejectField("firstName", request.getFirstName())
                .whenNull("first name must be specified")
                .orWhen(String::isEmpty, "first name must not be empty");

        result.rejectField("lastName", request.getLastName())
                .whenNull("last name must be specified")
                .orWhen(String::isEmpty, "last name must not be empty");

        result.rejectField("password", request.getPassword())
                .whenNull("password must be specified")
                .orWhen(String::isEmpty, "password must not be empty");

        result.rejectField("passwordConfirmation", request.getPasswordConfirmation())
                .whenNull("password must be confirmed")
                .orWhen(String::isEmpty, "confirmation field must not be empty")
                .orWhen(confirmation -> !confirmation.equals(request.getPassword()), "Password does not match the confirmation");

        if (result.hasErrors()) {
            throw new ValidationException(result);
        }

        final RuleResult passwordValidationResult = passwordValidator.validatePassword(request.getPassword());
        result.rejectField("password", request.getPassword())
                .when(password -> !passwordValidationResult.isValid(),
                        format("Password is not strong enough: %s", passwordValidationResult.getDetails())
                );

        if (result.hasErrors()) {
            throw new ValidationException(result);
        }
    }

    public void validateLoginRequest(final LoginRequest request) {
        final ValidationResult result = new ValidationResult();
        if (isNull(request)) {
            result.reject("Request body is missing, please provide a request body with the correct configuration");
            throw new ValidationException(result);
        }

        result.rejectField("email", request.getEmail())
                .whenNull("email must be specified")
                .orWhen(String::isEmpty, "email must not be empty");

        result.rejectField("password", request.getPassword())
                .whenNull("password must be specified")
                .orWhen(String::isEmpty, "password must not be empty");

        if (result.hasErrors()) {
            throw new ValidationException(result);
        }
    }
}
