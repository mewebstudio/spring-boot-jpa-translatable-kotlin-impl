package com.mewebstudio.translatable.exception

import com.mewebstudio.translatable.dto.response.DetailedErrorResponse
import com.mewebstudio.translatable.dto.response.ErrorResponse
import com.mewebstudio.translatable.util.logger
import jakarta.validation.ConstraintViolationException
import org.slf4j.Logger
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.dao.InvalidDataAccessApiUsageException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.validation.BindException
import org.springframework.validation.FieldError
import org.springframework.validation.ObjectError
import org.springframework.web.HttpMediaTypeNotSupportedException
import org.springframework.web.HttpRequestMethodNotSupportedException
import org.springframework.web.bind.MissingRequestHeaderException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException
import org.springframework.web.multipart.MultipartException
import org.springframework.web.multipart.support.MissingServletRequestPartException
import org.springframework.web.servlet.resource.NoResourceFoundException
import java.util.function.Consumer

@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
class AppExceptionHandler() {
    private val log: Logger by logger()

    @ExceptionHandler(HttpRequestMethodNotSupportedException::class)
    fun handleHttpRequestMethodNotSupported(e: HttpRequestMethodNotSupportedException): ResponseEntity<ErrorResponse> =
        build(
            HttpStatus.METHOD_NOT_ALLOWED,
            "Method ${e.method} is not supported for this request. Supported methods are: ${e.supportedHttpMethods}"
        ).also { log.error(e.toString(), e.message) }

    @ExceptionHandler(
        BadRequestException::class,
        MultipartException::class,
        MissingServletRequestPartException::class,
        HttpMediaTypeNotSupportedException::class,
        MethodArgumentTypeMismatchException::class,
        IllegalArgumentException::class,
        InvalidDataAccessApiUsageException::class,
        ConstraintViolationException::class,
        MissingRequestHeaderException::class,
        HttpMessageNotReadableException::class
    )
    fun handleBadRequestException(e: Exception): ResponseEntity<ErrorResponse> = build(
        HttpStatus.BAD_REQUEST,
        (e.cause?.message ?: e.message)!!
    ).also { log.error(e.toString(), e.message) }

    @ExceptionHandler(NotFoundException::class, NoResourceFoundException::class)
    fun handleNotFoundException(e: Exception): ResponseEntity<ErrorResponse> = build(
        HttpStatus.NOT_FOUND,
        (e.cause?.message ?: e.message)!!
    ).also { log.error(e.toString(), e.message) }

    @ExceptionHandler(BindException::class)
    fun handleBindException(e: BindException): ResponseEntity<ErrorResponse> = run {
        log.error(e.toString(), e.message)
        val errors: MutableMap<String, String?> = HashMap()

        e.bindingResult.allErrors.forEach(Consumer { error: ObjectError ->
            val fieldName = (error as FieldError).field
            val message = error.defaultMessage
            errors[fieldName] = message
        })

        build(HttpStatus.UNPROCESSABLE_ENTITY, "Validation error!", errors)
    }

    @ExceptionHandler(Exception::class)
    fun handleAllExceptions(e: Exception): ResponseEntity<ErrorResponse> = build(
        HttpStatus.INTERNAL_SERVER_ERROR,
        "Internal server error"
    ).also { log.error("Exception: {}", e.message) }

    /**
     * Build error response.
     *
     * @param httpStatus HttpStatus enum to response status field
     * @param message    String for response message field
     * @param errors     MutableMap for response errors field
     * @return ResponseEntity
     */
    private fun build(
        httpStatus: HttpStatus,
        message: String,
        errors: MutableMap<String, String?>
    ): ResponseEntity<ErrorResponse> = ResponseEntity.status(httpStatus).body(
        errors.takeIf { it.isNotEmpty() }
            ?.let { DetailedErrorResponse(message = message, items = it) }
            ?: ErrorResponse(message = message)
    )

    /**
     * Build error response.
     *
     * @param httpStatus HttpStatus enum to response status field
     * @param message    String for response message field
     * @return ResponseEntity
     */
    private fun build(httpStatus: HttpStatus, message: String): ResponseEntity<ErrorResponse> =
        build(httpStatus, message, HashMap())
}
