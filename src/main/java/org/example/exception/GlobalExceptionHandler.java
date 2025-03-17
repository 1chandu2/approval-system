package org.example.exception;

import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, String>> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", "Duplicate entry");

        String fieldName = extractFieldNameFromException(ex.getMessage());

        if (fieldName != null) {
            errorResponse.put("message", "The " + fieldName + " is already in use. Please use a different one.");
        } else {
            errorResponse.put("message", "A unique constraint violation occurred. Please check your input.");
        }

        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }

    private String extractFieldNameFromException(String exceptionMessage) {
        Pattern pattern = Pattern.compile("Key \\((.*?)\\)=");
        Matcher matcher = pattern.matcher(exceptionMessage);

        if (matcher.find()) {
            return matcher.group(1);
        }

        return null;
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, String>> handleRuntimeException(RuntimeException ex) {
        Map<String, String> response = new HashMap<>();

        if (ex.getMessage().contains("User not found")) {
            response.put("error", "User not found");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        } else if (ex.getMessage().contains("Invalid credentials")) {
            response.put("error", "Invalid credentials");
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        } else if (ex.getMessage().contains("Task not found")) {
            response.put("error", "Task not found");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        } else if (ex.getMessage().contains("User is not an assigned approver for this task")) {
            response.put("error", "User is not an assigned approver for this task");
            return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
        } else if (ex.getMessage().contains("User has already approved this task")) {
            response.put("error", "User has already approved this task");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } else {
            response.put("error", ex.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGeneralException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred: " + ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        Map<String, String > map = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> map.put(error.getField(), error.getDefaultMessage()));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(map);
    }


    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<String> handleMethodArgumentNotValidException(ConstraintViolationException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

}
