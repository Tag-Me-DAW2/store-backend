package com.tagme.tagme_store_back.spring.exception;


import com.tagme.tagme_store_back.domain.exception.BusinessException;
import com.tagme.tagme_store_back.domain.exception.InvalidCredentialsException;
import com.tagme.tagme_store_back.domain.exception.ResourceNotFoundException;
import com.tagme.tagme_store_back.domain.exception.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(ApiExceptionHandler.class);

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(ResourceNotFoundException.class)
    @RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ErrorMessage handleResourceNotFoundException(ResourceNotFoundException ex) {
        logger.error("ResourceNotFoundException: {}", ex.getMessage());
        return new ErrorMessage(ex);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({ValidationException.class, IllegalArgumentException.class, BusinessException.class})
    @RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ErrorMessage handleValidationException(Exception ex) {
        logger.error("ValidationException: {}", ex.getMessage());
        return new ErrorMessage(ex);
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(InvalidCredentialsException.class)
    @RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ErrorMessage handleInvalidCredentialsException(InvalidCredentialsException ex) {
        logger.error("InvalidCredentialsException: {}", ex.getMessage());
        return new ErrorMessage(ex);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(RuntimeException.class)
    @RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ErrorMessage handleRuntimeException(RuntimeException exception) {
        logger.error("RuntimeException: {}", exception.getMessage(), exception);
        return new ErrorMessage(exception);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    @RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ErrorMessage handleGeneralException(Exception exception) {
        logger.error("Exception: {}", exception.getMessage(), exception);
        return new ErrorMessage(exception);
    }
}
