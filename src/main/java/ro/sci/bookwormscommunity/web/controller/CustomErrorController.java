package ro.sci.bookwormscommunity.web.controller;

import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.RequestDispatcher;

/**
 * Controller that handles the mapping for the error responses.
 * Implements the {@link ErrorController} interface's {@link ErrorController#getErrorPath()} method to set the path for the error views.
 *
 * @author Alex
 * @author Ionut
 * @author Radu
 * @author Sorin
 * @see ErrorController
 */
@Controller
public class CustomErrorController implements ErrorController {

    /**
     * Maps any the error to different views based on their Error Status Code.
     *
     * @param request {@link HttpServletRequest} object containing the attribute from which the error status code is extracted.
     * @param model   {@link Model} used to add attributes that requires to be returned to the View.
     * @return the name of the view according the the status code of the error.
     */
    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        if (status != null) {
            int statusCode = Integer.parseInt(status.toString());

            if (statusCode == HttpStatus.NOT_FOUND.value()) {
                return "error-404";
            } else if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
                return "error-500";
            }
        }
        return "error";
    }

    @Override
    public String getErrorPath() {
        return "/error";
    }

}
