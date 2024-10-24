package com.ispydeer.WorkPlanner.controllers.exceptions;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * Global exception handler for the application.
 * Handles specific exceptions and provides custom responses.
 */
@ControllerAdvice
public class CustomResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * Handles UserNotFoundException and returns a 404 NOT FOUND response.
     *
     * @param ex      the exception
     * @param request the web request
     * @return error message and HTTP 404 NOT FOUND status
     */
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Object> handleUserNotFoundException(UserNotFoundException ex, WebRequest request) {
        String message = "User not found";
        return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
    }

    /**
     * Handles TeamNotFoundException and returns a 404 NOT FOUND response.
     *
     * @param ex      the exception
     * @param request the web request
     * @return error message and HTTP 404 NOT FOUND status
     */
    @ExceptionHandler(TeamNotFoundException.class)
    public ResponseEntity<Object> handleTeamNotFoundException(TeamNotFoundException ex, WebRequest request) {
        String message = "Team not found";
        return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
    }

    /**
     * Handles PlanEntryNotFoundException and returns a 404 NOT FOUND response.
     *
     * @param ex      the exception
     * @param request the web request
     * @return error message and HTTP 404 NOT FOUND status
     */
    @ExceptionHandler(PlanEntryNotFoundException.class)
    public ResponseEntity<Object> handlePlanEntryNotFoundException(PlanEntryNotFoundException ex, WebRequest request) {
        String message = "Plan entry not found";
        return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
    }

    /**
     * Handles UsernameAlreadyUsedException and returns a 409 CONFLICT response.
     *
     * @param ex      the exception
     * @param request the web request
     * @return error message and HTTP 409 CONFLICT status
     */
    @ExceptionHandler(UsernameAlreadyUsedException.class)
    public ResponseEntity<Object> handleUsernameAlreadyUsedException(UsernameAlreadyUsedException ex, WebRequest request) {
        String message = "Username is already used";
        return new ResponseEntity<>(message, HttpStatus.CONFLICT);
    }

    /**
     * Handles TeamNameAlreadyUsedException and returns a 409 CONFLICT response.
     *
     * @param ex      the exception
     * @param request the web request
     * @return error message and HTTP 409 CONFLICT status
     */
    @ExceptionHandler(TeamNameAlreadyUsedException.class)
    public ResponseEntity<Object> handleTeamNameAlreadyUsedException(TeamNameAlreadyUsedException ex, WebRequest request) {
        String message = "Team name is already used";
        return new ResponseEntity<>(message, HttpStatus.CONFLICT);
    }

    /**
     * Handles MethodArgumentNotValidException and returns a 400 BAD REQUEST response.
     * This method is overridden to customize the response with validation error messages.
     *
     * @param ex      the exception
     * @param headers HTTP headers
     * @param status  HTTP status code
     * @param request the web request
     * @return a list of validation error messages and HTTP 400 BAD REQUEST status
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request
    ) {
        List<String> errors = new ArrayList<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String errorMessage = error.getDefaultMessage();
            errors.add(errorMessage);
        });
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }
}
