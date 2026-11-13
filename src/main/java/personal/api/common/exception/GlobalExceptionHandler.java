package personal.api.common.exception;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.validation.method.ParameterErrors;
import org.springframework.validation.method.ParameterValidationResult;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.server.ResponseStatusException;

import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Path;
import lombok.extern.slf4j.Slf4j;
import personal.api.common.response.ApiResponse;
import personal.api.common.response.FieldErrorResponse;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleMethodArgumentNotValid(MethodArgumentNotValidException exception) {

        List<FieldErrorResponse> errors = exception.getBindingResult().getAllErrors().stream()
                .map(err -> new FieldErrorResponse(
                        err instanceof FieldError fe ? fe.getField() : null,
                        err.getDefaultMessage()))
                .toList();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.fail(errors));
    }

    @ExceptionHandler(HandlerMethodValidationException.class)
    public ResponseEntity<ApiResponse<Void>> handleHandlerMethodValidation(
            HandlerMethodValidationException exception) {

        List<FieldErrorResponse> errors = new ArrayList<>();

        for (ParameterValidationResult result : exception.getParameterValidationResults()) {
            if (result instanceof ParameterErrors pe) {
                pe.getFieldErrors()
                        .forEach(fe -> errors.add(new FieldErrorResponse(fe.getField(), fe.getDefaultMessage())));
            } else {
                String name = result.getMethodParameter().getParameterName();
                result.getResolvableErrors()
                        .forEach(re -> errors.add(new FieldErrorResponse(name, re.getDefaultMessage())));
            }
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.fail(errors));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponse<Void>> handleConstraintViolation(ConstraintViolationException exception) {

        List<FieldErrorResponse> errors = exception.getConstraintViolations().stream()
                .map(violation -> {
                    String field = null;
                    for (Path.Node node : violation.getPropertyPath()) {
                        field = node.getName();
                    }
                    return new FieldErrorResponse(field, violation.getMessage());
                })
                .toList();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.fail(errors));
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ApiResponse<Void>> handleResponseStatus(ResponseStatusException exception) {

        String message = exception.getReason() != null ? exception.getReason()
                : HttpStatus.valueOf(exception.getStatusCode().value()).getReasonPhrase();

        return ResponseEntity.status(exception.getStatusCode())
                .body(ApiResponse.fail(List.of(new FieldErrorResponse(null, message))));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleException(Exception exception) {

        if (exception instanceof ErrorResponse error) {
            return ResponseEntity.status(error.getStatusCode())
                    .body(ApiResponse.fail(List.of(new FieldErrorResponse(null, error.getBody().getDetail()))));
        }

        if (exception instanceof HttpMessageNotReadableException) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.fail(List.of(new FieldErrorResponse(null, "Invalid request body"))));
        }

        if (exception instanceof MethodArgumentTypeMismatchException mismatch) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.fail(List.of(new FieldErrorResponse(mismatch.getName(), "Invalid value"))));
        }

        log.error("Unhandled exception", exception);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.fail(List.of(new FieldErrorResponse(null, "A server error occurred"))));
    }
}
