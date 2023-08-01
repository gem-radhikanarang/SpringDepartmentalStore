package com.admin.SpringBootDepartmentalStore.exception;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.NoSuchElementException;

@ControllerAdvice
public class ExceptionHandling {
    @ExceptionHandler(IllegalArgumentException.class)
    public final ResponseEntity<String> handleInvalidEmail(final IllegalArgumentException e) {
        return ResponseEntity.ok(e.getMessage());
    }

    @ExceptionHandler(NoSuchElementException.class)
    public final ResponseEntity<String> handleNoSuchElementException() {
        return new ResponseEntity<>("Order with the given id not found", HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(IllegalStateException.class)
    public final ResponseEntity<String> handleIllegalStateException(final IllegalStateException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
