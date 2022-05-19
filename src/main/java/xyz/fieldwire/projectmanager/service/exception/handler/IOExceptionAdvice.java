package xyz.fieldwire.projectmanager.service.exception.handler;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import xyz.fieldwire.projectmanager.service.response.ErrorResponse;

import java.io.IOException;

@RestControllerAdvice
public class IOExceptionAdvice {
    @ExceptionHandler(IOException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    ErrorResponse ioExceptionHandler(IOException ex) {
        return new ErrorResponse(ex.getMessage());
    }
}
