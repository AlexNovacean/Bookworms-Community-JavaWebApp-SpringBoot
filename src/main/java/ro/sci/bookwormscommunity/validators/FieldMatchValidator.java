package ro.sci.bookwormscommunity.validators;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Validates the fields annotated with the {@link FieldMatch} custom annotation, by implementing the {@link ConstraintValidator#isValid(Object, ConstraintValidatorContext)} method.
 *
 * @author Alex
 * @author Ionut
 * @author Radu
 * @author Sorin
 * @author Raul
 * @see ConstraintValidator
 */
public class FieldMatchValidator implements ConstraintValidator<FieldMatch, Object> {

    private Logger logger = LoggerFactory.getLogger(FieldMatchValidator.class);

    private String firstFieldName;
    private String secondFieldName;

    @Override
    public void initialize(final FieldMatch constraintAnnotation) {
        firstFieldName = constraintAnnotation.first();
        secondFieldName = constraintAnnotation.second();
    }

    @Override
    public boolean isValid(final Object value, final ConstraintValidatorContext context) {
        try {
            final Object firstObj = BeanUtils.getProperty(value, firstFieldName);
            final Object secondObj = BeanUtils.getProperty(value, secondFieldName);
            return firstObj == null && secondObj == null || firstObj != null && firstObj.equals(secondObj);
        } catch (Exception e) {
            logger.error("Field Match Validator Error: ", e);
        }
        return true;
    }
}
