package ru.social.network.controller;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import java.util.Map;
import java.util.stream.Collectors;

public class ControllerUtils {

    private static final String ERROR = "Error";

    static Map<String, String> getErrors(BindingResult bindingResult) {
        var collector = Collectors.toMap(fieldError ->
                        fieldError.getField() + ERROR, FieldError::getDefaultMessage);
        return bindingResult.getFieldErrors().stream().collect(collector);
    }
}
