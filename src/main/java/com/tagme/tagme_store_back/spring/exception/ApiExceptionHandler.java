package com.tagme.tagme_store_back.spring.exception;


import com.tagme.tagme_store_back.domain.exception.InvalidCredentialsException;
import com.tagme.tagme_store_back.domain.exception.ResourceNotFoundException;
import com.tagme.tagme_store_back.domain.exception.ValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionHandler {
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(ResourceNotFoundException.class)
    @RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ErrorMessage handleResourceNotFoundException(ResourceNotFoundException ex) {
        return new ErrorMessage(ex);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({ValidationException.class,IllegalArgumentException.class})
    @RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ErrorMessage handleValidationException(Exception ex) {
        return new ErrorMessage(ex);
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(InvalidCredentialsException.class)
    @RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ErrorMessage handleInvalidCredentialsException(InvalidCredentialsException ex) {
        return new ErrorMessage(ex);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    @RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ErrorMessage handleGeneralException(Exception exception) {
        return new ErrorMessage(exception);
    }
}
