package com.piho.onoff.exceptions;

import com.piho.onoff.domain.HttpResponse;
import com.piho.onoff.exceptions.domain.BadRequestException;
import com.piho.onoff.exceptions.domain.ServerIsUnderMaintenanceException;
import com.piho.onoff.exceptions.domain.UniqueFieldExistException;
import com.piho.onoff.exceptions.domain.NotFoundException;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;

@RestControllerAdvice
public class ExceptionHandling implements ErrorController {

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<HttpResponse> badRequestException(BadRequestException exception) {
        return createHttpResponse(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<HttpResponse> validationException(ConstraintViolationException exception) {
        return createHttpResponse(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(ServerIsUnderMaintenanceException.class)
    public ResponseEntity<HttpResponse> serverIsUnderMaintenanceException(ServerIsUnderMaintenanceException exception) {
        return createHttpResponse(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(UniqueFieldExistException.class)
    public ResponseEntity<HttpResponse> uniqueFieldExistException(UniqueFieldExistException exception) {
        return createHttpResponse(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<HttpResponse> walletNotFoundException(NotFoundException exception) {
        return createHttpResponse(HttpStatus.NOT_FOUND, exception.getMessage());
    }

    private ResponseEntity<HttpResponse> createHttpResponse(HttpStatus httpStatus, String message) {
        return new ResponseEntity<>(
                new HttpResponse(httpStatus.value(), httpStatus, message), httpStatus);
    }

    @Override
    public String getErrorPath() {
        return null;
    }
}
