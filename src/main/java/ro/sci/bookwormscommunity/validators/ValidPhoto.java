package ro.sci.bookwormscommunity.validators;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ValidPhotoValidator.class)
@Documented
public @interface ValidPhoto {

    String message() default "Invalid Photo.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
