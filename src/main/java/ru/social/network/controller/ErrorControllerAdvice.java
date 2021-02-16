package ru.social.network.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.social.network.mapings.ViewPages;

import javax.validation.ValidationException;

@ControllerAdvice
public class ErrorControllerAdvice {

    private static final Logger LOGGER = LoggerFactory.getLogger(ErrorControllerAdvice.class);

    @ExceptionHandler({ValidationException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleValidationException(Exception e) {
        LOGGER.error(e.getMessage());
        return ViewPages.ERROR_404;
    }

    @ExceptionHandler({RuntimeException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleException(Exception e) {
        LOGGER.error(e.getMessage());
        return ViewPages.ERROR_500;
    }
}
