package com.demo.crud.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<Object> handleValidationExceptions(IllegalArgumentException ex) {
		StringBuilder errorMessage = new StringBuilder("Validation Error: ");
		errorMessage.append(ex.getMessage()).append("; ");
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage.toString());

	}

	@ExceptionHandler(DataIntegrityViolationException.class)
	public ResponseEntity<Object> handleIntegrityExceptions(DataIntegrityViolationException ex) {
		StringBuilder errorMessage = new StringBuilder("Data Intgerity Error: ");
		errorMessage.append(ex.getMessage()).append("; ");
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage.toString());

	}
}
