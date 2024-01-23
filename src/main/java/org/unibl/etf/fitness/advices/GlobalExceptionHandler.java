package org.unibl.etf.fitness.advices;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.unibl.etf.fitness.exceptions.AccountBlockedException;
import org.unibl.etf.fitness.exceptions.NotApprovedException;
import org.unibl.etf.fitness.exceptions.NotFoundException;
import org.unibl.etf.fitness.exceptions.UnauthorizedException;
import org.unibl.etf.fitness.services.LogService;

@ControllerAdvice
public class GlobalExceptionHandler {

    private final LogService logService;

    public GlobalExceptionHandler(LogService logService) {
        this.logService = logService;
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<Object> handleUnauthorizedException(UnauthorizedException e)
    {
        logService.error("Unauthorized exception occurred: " + e.getMessage());
        return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(NotApprovedException.class)
    public ResponseEntity<Object> handleNotApprovedException(NotApprovedException e)
    {
        logService.error("Not acceptable exception occurred: " + e.getMessage());
        return new ResponseEntity<>(e.getMessage(),HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler(AccountBlockedException.class)
    public ResponseEntity<Object> handleAccountBlockedException(AccountBlockedException e)
    {
        logService.error("Forbidden exception occurred: " + e.getMessage());
        return new ResponseEntity<>(e.getMessage(),HttpStatus.FORBIDDEN);
    }
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Object> handleNotFoundException(NotFoundException e)
    {
        logService.error("Not found exception occurred: " + e.getMessage());
        return new ResponseEntity<>(e.getMessage(),HttpStatus.NOT_FOUND);
    }
}
