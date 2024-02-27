package dev.batuhanyetgin.ms_security.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler({AuthException.class})
    public ResponseEntity<String> handleAuthException(AuthException exception) {
        log.error(exception.getMessage(), exception);
        return new ResponseEntity<String>(exception.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler({BadCredentialsException.class})
    public ResponseEntity<String> handleBadCredentialsException(BadCredentialsException exception) {
        log.error(exception.getMessage(), exception);
        return new ResponseEntity<String>("Wrong username or password",
                HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<String> handleException(Exception exception) {
        log.error(exception.getMessage(), exception);
        return new ResponseEntity<String>("Wrong username or password",
                HttpStatus.UNAUTHORIZED);
    }
}
