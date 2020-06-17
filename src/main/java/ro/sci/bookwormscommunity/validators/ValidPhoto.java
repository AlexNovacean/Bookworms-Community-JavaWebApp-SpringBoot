package ro.sci.bookwormscommunity.validators;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * Custom validation annotation, that validates a file to be an accepted image.
 * The validation is done by the {@link ValidPhotoValidator}.
 *
 * @author Alex
 * @author Ionut
 * @author Radu
 * @author Sorin
 * @author Raul
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ValidPhotoValidator.class)
@Documented
public @interface ValidPhoto {

    String message() default "Invalid Photo.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
