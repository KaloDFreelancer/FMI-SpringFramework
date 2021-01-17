package fmi.course.cookingwebapp.util;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.validation.Errors;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ErrorHandlingUtils {

    public static List<String> getViolationsAsStringList(Errors errors) {
        List<String> globalErrors = errors.getGlobalErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());
        List<String> fieldErrors = errors.getFieldErrors().stream()
                .map(err -> err.getField() + " = " + err.getRejectedValue() + ": " + err.getDefaultMessage())
                .collect(Collectors.toList());
        List<String> allErrors = new ArrayList<>(globalErrors);
        allErrors.addAll(fieldErrors);
        return allErrors;
    }
}