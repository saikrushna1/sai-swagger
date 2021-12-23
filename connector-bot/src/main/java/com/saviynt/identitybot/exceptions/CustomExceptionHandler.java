package com.saviynt.identitybot.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler
{

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<Object> handleAllExceptions(Exception ex, WebRequest request) {
        return new ResponseEntity(ErrorResponse.builder()
                .error("Internal Server Error")
                .message(ex.getLocalizedMessage())
                .status( HttpStatus.INTERNAL_SERVER_ERROR.value()).build(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(RecordNotFoundException.class)
    public final ResponseEntity<Object> handleRecordNotFoundException(RecordNotFoundException ex, WebRequest request) {
        return new ResponseEntity(ErrorResponse.builder().error("Record Not Found")
                .message(ex.getLocalizedMessage())
                .status( HttpStatus.NOT_FOUND.value()).build(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AuthenticationException.class)
    public final ResponseEntity<Object> handleAuthenticationException(AuthenticationException ex, WebRequest request) {
        return new ResponseEntity(ErrorResponse.builder()
                .error("Unauthorized")
                .message("Username/password is incorrect")
                .status( HttpStatus.UNAUTHORIZED.value()).build(),HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public final ResponseEntity<Object> handleUserAlreadyExistsException(AuthenticationException ex, WebRequest request) {
        return new ResponseEntity(ErrorResponse.builder()
                .error("Bad Request")
                .message(ex.getLocalizedMessage())
                .status( HttpStatus.UNAUTHORIZED.value()).build(),HttpStatus.UNAUTHORIZED);
    }


}
