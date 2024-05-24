package org.jordijaspers.aniflix.api.user.validator;

import lombok.RequiredArgsConstructor;
import org.hawaiiframework.validation.ValidationResult;
import org.hawaiiframework.validation.Validator;
import org.jordijaspers.aniflix.api.user.model.request.UpdateEmailRequest;
import org.springframework.stereotype.Component;

/**
 * {@inheritDoc}.
 */
@Component
@RequiredArgsConstructor
public class EmailValidator implements Validator<UpdateEmailRequest> {

    private static final String OWASP_EMAIL_REGEX = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,}$";

    /**
     * {@inheritDoc}
     */
    @Override
    public void validate(final UpdateEmailRequest request, final ValidationResult result) {
        result.rejectField("email", request.getEmail())
                .whenNull("email must be specified")
                .orWhen(String::isEmpty, "email must not be empty")
                .orWhen(email -> !email.contains("@"), "email must contain an @")
                .orWhen(email -> !email.matches(OWASP_EMAIL_REGEX), "email must be a valid email address");
    }
}
