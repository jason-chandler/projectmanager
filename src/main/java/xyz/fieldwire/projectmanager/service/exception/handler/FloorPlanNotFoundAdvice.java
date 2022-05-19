package xyz.fieldwire.projectmanager.service.exception.handler;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import xyz.fieldwire.projectmanager.service.exception.FloorPlanNotFoundException;
import xyz.fieldwire.projectmanager.service.response.ErrorResponse;

@RestControllerAdvice
public class FloorPlanNotFoundAdvice {
        @ExceptionHandler(Exception.class)
        @ResponseStatus(HttpStatus.NOT_FOUND)
        ErrorResponse floorPlanNotFoundExceptionHandler(FloorPlanNotFoundException ex) {
            return new ErrorResponse(ex.getMessage());
        }
}
