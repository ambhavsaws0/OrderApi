/*
package com.example.OrderApi.ordermanagementsystem.advice;

import com.example.OrderApi.ordermanagementsystem.exceptionhandler.BusinessException;
import com.example.OrderApi.ordermanagementsystem.exceptionhandler.InvalidOrderDataException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ProblemDetail handleBusinessException(final BusinessException businessException) throws URISyntaxException {
        final ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.UNPROCESSABLE_ENTITY);
        problemDetail.setTitle(businessException.getType());
        problemDetail.setType(new URI("http://localhost:8080/"));
        problemDetail.setDetail(businessException.getType());
        problemDetail.setProperty("timeStamp", LocalDateTime.now());
        problemDetail.setProperty("message", businessException.getMessage());
        return problemDetail;
    }

    @ExceptionHandler(InvalidOrderDataException.class)
    public ProblemDetail handleInvalidOrderDataException(final InvalidOrderDataException invalidOrderDataException) throws URISyntaxException {
        final ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problemDetail.setTitle(invalidOrderDataException.getType());
        problemDetail.setType(new URI("http://localhost:8080/"));
        problemDetail.setDetail(invalidOrderDataException.getType());
        problemDetail.setProperty("timeStamp", LocalDateTime.now());
        problemDetail.setProperty("message", invalidOrderDataException.getMessage());
        return problemDetail;
    }
}
*/
