package com.microcompany.accountsservice.config;

import com.microcompany.accountsservice.exception.AccountNotfoundException;
import com.microcompany.accountsservice.exception.GlobalException;
import com.microcompany.accountsservice.payload.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.validation.ConstraintViolationException;


@RestControllerAdvice
public class GlobalExceptionController {

    @ExceptionHandler(value = GlobalException.class)
    public ResponseEntity handleGlobalException(GlobalException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST.value()).body(new ApiResponse(ex.getMessage(), false));
    }

    @ExceptionHandler(value = AccountNotfoundException.class)
    public ResponseEntity handleAccountException(AccountNotfoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(ex.getMessage(), false));
    }

    @ExceptionHandler(value = MethodArgumentTypeMismatchException.class)
    public ResponseEntity handleMismatchException(MethodArgumentTypeMismatchException ex) {
        return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED.value()).body(new ApiResponse(ex.getMessage(), false));
    }

    @ExceptionHandler(value = ConstraintViolationException.class)
    public ResponseEntity handleConstraintViolationException(ConstraintViolationException ex) {
        return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED.value()).body(new ApiResponse(ex.getMessage(), false));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity handleValidationException(MethodArgumentNotValidException ex) {
        StringBuilder errors = new StringBuilder();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.append(fieldName).append(": ").append(errorMessage).append(". ");
        });
        errors.setLength(errors.length()-1);
        return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED.value()).body(new ApiResponse(errors.toString(), false));
    }
}
