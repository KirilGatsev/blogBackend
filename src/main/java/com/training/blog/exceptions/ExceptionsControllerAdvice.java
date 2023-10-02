package com.training.blog.exceptions;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.*;

@RestControllerAdvice
public class ExceptionsControllerAdvice {
    //when not using valid to validate aka checking if username is unique
    //this is done AFTER we have taken the request
    @ExceptionHandler(ConstraintViolationException.class)
    ResponseEntity<ApiError> invalidInput(ConstraintViolationException ex){
        ApiError error = new ApiError();
        List<String> errors = new ArrayList<>();
        for(ConstraintViolation<?> constraint: ex.getConstraintViolations()){
            errors.add(constraint.getMessage());
        }
        error.setMessage("INVALID_VALUES");
        error.setStatus(HttpStatus.BAD_REQUEST);
        error.setErrors(errors);
        error.setTime(LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    //when @Valid fails
    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<ApiError> invalidInput(Exception ex){
        ApiError error = new ApiError(HttpStatus.BAD_REQUEST, "INVALID_VALUES", ex.getMessage());
        error.setTime(LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }
    //da naprava error checkings da nqma ednakvi usernamove i postove s ednakvi titles
    @ExceptionHandler({
            UserNotFoundException.class,
            PostNotFoundException.class
    })
    ResponseEntity<ApiError> userNotFoundExceptionAdvise(RuntimeException ex){
        ApiError error = new ApiError();
        error.setMessage("OBJECT_NOT_FOUND");
        error.setStatus(HttpStatus.BAD_REQUEST);
        error.setErrors(Collections.singletonList(ex.getMessage()));
        error.setTime(LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(NotUniqueException.class)
    ResponseEntity<ApiError> uniqueFieldNotUnique(NotUniqueException ex){
        ApiError error = new ApiError();
        error.setMessage("OBJECT_NOT_UNIQUE");
        error.setStatus(HttpStatus.CONFLICT);
        error.setErrors(Collections.singletonList(ex.getMessage()));
        error.setTime(LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);

    }


}
