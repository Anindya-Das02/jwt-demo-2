package in.das.jwtdemo.advice;

import in.das.jwtdemo.exception.UnAuthorisedResourceAccessException;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;
import java.util.UUID;

@ControllerAdvice
public class ExceptionAdvice {

    @ExceptionHandler(UnAuthorisedResourceAccessException.class)
    public ResponseEntity<?> unAuthorisedResourceAccessException(UnAuthorisedResourceAccessException ex){
        return ResponseEntity.status(HttpStatusCode.valueOf(403)).body(Map.of(
                "message",ex.getMessage(),
                "errorUUID", UUID.randomUUID().toString()
        ));
    }

    @ExceptionHandler(BadCredentialsException.class)
    public String exceptionHandler() {
        return "Credentials Invalid !!";
    }
}
