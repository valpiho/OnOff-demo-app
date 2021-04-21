package com.piho.onoff.services;

import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Service
public class ValidationErrorService {
    public ArrayList<String> validationErrorService(BindingResult result) {
        if (result.hasErrors()) {
            ArrayList<String> errorList = new ArrayList<>();
            for (FieldError error: result.getFieldErrors()) {
                errorList.add(error.getDefaultMessage());
            }
            return errorList;
        }
        return null;
    }
}
