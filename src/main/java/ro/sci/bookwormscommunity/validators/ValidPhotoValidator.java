package ro.sci.bookwormscommunity.validators;

import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class ValidPhotoValidator implements ConstraintValidator<ValidPhoto, MultipartFile> {

    private static final double MAX_PHOTO_SIZE = 1D * 1024 * 1024;
    private List<String> photoTypes = Arrays.asList("image/png", "image/jpg", "image/jpeg");

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
        } catch (IOException ignore) {
            //exception handled in the UserRegistrationController
        }

        return false;
    }
}
