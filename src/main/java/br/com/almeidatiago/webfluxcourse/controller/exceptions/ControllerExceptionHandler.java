package br.com.almeidatiago.webfluxcourse.controller.exceptions;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.support.WebExchangeBindException;
import reactor.core.publisher.Mono;

import static java.time.LocalDateTime.now;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(DuplicateKeyException.class)
    public ResponseEntity<Mono<StandardError>> duplicateKeyException(
        DuplicateKeyException ex, ServerHttpRequest request
    ) {
        return ResponseEntity.badRequest()
            .body(
                Mono.just(
                    StandardError.builder()
                        .timestamp(now())
                        .status(BAD_REQUEST.value())
                        .error(BAD_REQUEST.getReasonPhrase())
                        .message(verifyDupKeyEmail(ex.getMessage()))
                        .path(request.getPath().toString())
                        .build()
                ));
    }

    @ExceptionHandler(WebExchangeBindException.class)
    public ResponseEntity<Mono<ValidationError>> validationError(
        WebExchangeBindException ex, ServerHttpRequest request
    ) {
        ValidationError error = new ValidationError(
            now(),
            request.getPath().toString(),
            BAD_REQUEST.value(),
            "Validation Error",
            "Error validating attributes"

        );

        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()){
            error.addError(fieldError.getField(), fieldError.getDefaultMessage());
        }

        return ResponseEntity.status(BAD_REQUEST).body(Mono.just(error));
    }

    private String verifyDupKeyEmail(String message) {
        if(message.contains("email dup key")) {
            return "E-mail already registered";
        }
        return "Dup key exception";
    }

}