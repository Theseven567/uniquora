package kz.codingwolves.controllers;

import kz.codingwolves.enums.Messages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by sagynysh on 12/20/16.
 */
@ControllerAdvice
@RestController
public class ErrorHandler implements ErrorController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public String getErrorPath() {
        return "/error";
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseBody
    public String methodNotSupported(HttpServletResponse response) {
        response.setStatus(404);
        return Messages.notfound.toString();
    }

    @ExceptionHandler(Throwable.class)
    @ResponseBody
    public String conflict(Throwable e, HttpServletResponse response) {
        logger.info("An error occured with message {" + e.getMessage() + ", " + e.getStackTrace()[0].toString() + "}");
        response.setStatus(500);
        return Messages.internalerror.toString();
    }

    @RequestMapping(value = "/error")
    public String error(HttpServletRequest request, HttpServletResponse response) {
        Integer code = (java.lang.Integer) request.getAttribute("javax.servlet.error.status_code");
        if (code != null && code == 404) {
            response.setStatus(404);
            return Messages.notfound.toString();
        }
        response.setStatus(500);
        return Messages.internalerror.toString();
    }
}
