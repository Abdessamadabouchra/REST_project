package com.project1.project1.exception.handler;

import com.project1.project1.exception.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler    {

@Override
protected ResponseEntity<Object> handleNoHandlerFoundException(
        NoHandlerFoundException ex,
        HttpHeaders headers,
        HttpStatusCode status,
        WebRequest request){

        return new ResponseEntity<>(
                new ErrorResponse("PATH_NOT_FOUND", "Invalid path: " + ex.getRequestURL()),
                HttpStatus.NOT_FOUND
        );
    }


 @ExceptionHandler(InvalidOperationException.class)
    public ResponseEntity<String> handleInvalidOp(InvalidOperationException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }


    @ExceptionHandler(ParamsNotValidException.class)
    public ResponseEntity<ErrorResponse> handleParams(ParamsNotValidException e) {
        return new ResponseEntity<>(new ErrorResponse("PARAMS_NOT_VALID", e.getMessage()),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BodyNotValidException.class)
    public ResponseEntity<ErrorResponse> handleBody(BodyNotValidException e) {
        return new ResponseEntity<>(new ErrorResponse("BODY_NOT_VALID", e.getMessage()),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResource(ResourceNotFoundException e) {
        return new ResponseEntity<>(new ErrorResponse("RESOURCE_NOT_FOUND", e.getMessage()),
                HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(PathNotFoundException.class)
    public ResponseEntity<ErrorResponse> handlePath(PathNotFoundException e) {
        return new ResponseEntity<>(new ErrorResponse("PATH_NOT_FOUND", e.getMessage()),
                HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ServerErrorException.class)
    public ResponseEntity<ErrorResponse> handleServer(ServerErrorException e) {
        return new ResponseEntity<>(new ErrorResponse("SERVER_ERROR", e.getMessage()),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // fallback: si une erreur non prévue arrive
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleFallback(Exception e) {
        return new ResponseEntity<>(
                new ErrorResponse("SERVER_ERROR", "Unexpected error occurred."),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }
}

