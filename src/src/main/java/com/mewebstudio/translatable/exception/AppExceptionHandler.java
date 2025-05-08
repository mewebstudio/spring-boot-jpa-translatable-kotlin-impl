package com.mewebstudio.translatable.exception;

import com.mewebstudio.translatable.dto.response.DetailedErrorResponse;
import com.mewebstudio.translatable.dto.response.ErrorResponse;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.HashMap;
import java.util.Map;

@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
public class AppExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(AppExceptionHandler.class);

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException e) {
        String message = String.format(
            "Method %s is not supported for this request. Supported methods are: %s",
            e.getMethod(),
            e.getSupportedHttpMethods()
        );
        log.error(e.toString(), e);
        return build(HttpStatus.METHOD_NOT_ALLOWED, message);
    }

    @ExceptionHandler({
        BadRequestException.class,
        MultipartException.class,
        MissingServletRequestPartException.class,
        HttpMediaTypeNotSupportedException.class,
        MethodArgumentTypeMismatchException.class,
        IllegalArgumentException.class,
        InvalidDataAccessApiUsageException.class,
        ConstraintViolationException.class,
        MissingRequestHeaderException.class,
        HttpMessageNotReadableException.class
    })
    public ResponseEntity<ErrorResponse> handleBadRequestException(Exception e) {
        String message = e.getCause() != null ? e.getCause().getMessage() : e.getMessage();
        log.error(e.toString(), e);
        return build(HttpStatus.BAD_REQUEST, message);
    }

    @ExceptionHandler({NotFoundException.class, NoResourceFoundException.class})
    public ResponseEntity<ErrorResponse> handleNotFoundException(Exception e) {
        String message = e.getCause() != null ? e.getCause().getMessage() : e.getMessage();
        log.error(e.toString(), e);
        return build(HttpStatus.NOT_FOUND, message);
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<ErrorResponse> handleBindException(BindException e) {
        log.error(e.toString(), e);
        Map<String, String> errors = new HashMap<>();
        for (ObjectError error : e.getBindingResult().getAllErrors()) {
            if (error instanceof FieldError fieldError) {
                errors.put(fieldError.getField(), fieldError.getDefaultMessage());
            }
        }
        return build(HttpStatus.UNPROCESSABLE_ENTITY, "Validation error!", errors);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleAllExceptions(Exception e) {
        log.error("Exception: {}", e.getMessage(), e);
        return build(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error");
    }

    private ResponseEntity<ErrorResponse> build(HttpStatus httpStatus, String message, Map<String, String> errors) {
        ErrorResponse response = errors != null && !errors.isEmpty()
            ? new DetailedErrorResponse(message, errors)
            : new ErrorResponse(message);
        return ResponseEntity.status(httpStatus).body(response);
    }

    private ResponseEntity<ErrorResponse> build(HttpStatus httpStatus, String message) {
        return build(httpStatus, message, new HashMap<>());
    }
}
