package com.kafafy.integrationFinal.exception;

import com.kafafy.integrationFinal.model.FinalOutputModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(SpecificException.class)
    public ResponseEntity<FinalOutputModel> handleSpecificException(SpecificException ex, HttpServletRequest request) {
        FinalOutputModel finalOutput = new FinalOutputModel();
        finalOutput.setMessage(ex.getMessage());
        finalOutput.setCode(ex.getCode());
        finalOutput.setStatusDesc("Failed");

        return new ResponseEntity<>(finalOutput, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<FinalOutputModel> handleGenericException(Exception ex, HttpServletRequest request) {
        FinalOutputModel finalOutput = new FinalOutputModel();
        finalOutput.setMessage("An unexpected error occurred: " + ex.getMessage());
        finalOutput.setCode("500");
        finalOutput.setStatusDesc("Failed");

        return new ResponseEntity<>(finalOutput, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
