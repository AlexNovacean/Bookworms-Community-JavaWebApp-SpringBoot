package ro.sci.bookwormscommunity.validators;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Custom validation annotation, used to check if two fields of type {@link String} are equal, using the {@link FieldMatchValidator}.
 *
 * @author Alex
 * @author Ionut
 * @author Radu
 * @author Sorin
 */
@Target({
        TYPE,
        ANNOTATION_TYPE
})
@Retention(RUNTIME)
@Constraint(validatedBy = FieldMatchValidator.class)
@Documented
public @interface FieldMatch {
    String message() default "{constraints.field-match}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String first();

    String second();

    @Target({
            TYPE,
            ANNOTATION_TYPE
    })
    @Retention(RUNTIME)
    @Documented
    @interface List {
        FieldMatch[] value();
    }
}
