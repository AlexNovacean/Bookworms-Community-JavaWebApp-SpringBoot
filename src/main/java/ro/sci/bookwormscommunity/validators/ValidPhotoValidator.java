package ro.sci.bookwormscommunity.validators;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Validates the {@link MultipartFile} fields annotated with {@link ValidPhoto} custom annotation, by implementing the {@link ConstraintValidator#isValid(Object, ConstraintValidatorContext)} method.
 *
 * @author Alex
 * @author Ionut
 * @author Radu
 * @author Sorin
 * @author Raul
 * @see ConstraintValidator
 */
public class ValidPhotoValidator implements ConstraintValidator<ValidPhoto, MultipartFile> {

    private static final double MAX_PHOTO_SIZE = 1D * 1024 * 1024;
    private final List<String> photoTypes = Arrays.asList("image/png", "image/jpg", "image/jpeg");

    private Logger logger = LoggerFactory.getLogger(ValidPhotoValidator.class);

    @Override
    public boolean isValid(MultipartFile file, ConstraintValidatorContext constraintValidatorContext) {
        if (file.isEmpty()) {
            return true;
        }
        byte[] fileContent;
        String fileType = file.getContentType();
        try {
            fileContent = file.getBytes();
            if (fileContent.length < MAX_PHOTO_SIZE && photoTypes.contains(fileType)) {
                return true;
            }
        } catch (IOException e) {
            logger.error("An error occurred while validating the photo: ", e);
        }
        return false;
    }
}
