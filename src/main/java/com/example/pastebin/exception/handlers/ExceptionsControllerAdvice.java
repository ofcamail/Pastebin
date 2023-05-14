package com.example.pastebin.exception.handlers;

import com.example.pastebin.exception.ForbiddenException;
import com.example.pastebin.exception.NotFoundException;
import com.example.pastebin.exception.UnauthorizedException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionsControllerAdvice {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<?> notFound() {
        return ResponseEntity.status(404).build();
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<?> forbidden() {
        return ResponseEntity.status(403).build();
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<?> unauthorized() {
        return ResponseEntity.status(401).build();
    }
}
