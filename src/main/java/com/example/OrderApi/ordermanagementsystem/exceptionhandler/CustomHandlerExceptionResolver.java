package com.example.OrderApi.ordermanagementsystem.exceptionhandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;

@Controller
public class CustomHandlerExceptionResolver implements HandlerExceptionResolver {

    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception exception) {
        final ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        try {
            switch (exception) {
                case InvalidOrderDataException invalidOrderDataException -> {
                    final ProblemDetail problemDetail = getProblemDetailInvalidOrderDataException(invalidOrderDataException);
                    setResponseWriter(response, objectMapper, problemDetail);
                    response.setStatus(HttpStatus.BAD_REQUEST.value());
                }
                case BusinessException businessException -> {
                    final ProblemDetail problemDetail = getProblemDetailBusinessException(businessException);
                    setResponseWriter(response, objectMapper, problemDetail);
                    response.setStatus(HttpStatus.UNPROCESSABLE_ENTITY.value());
                }
                default -> response.getWriter().write("Handled via HandlerExceptionResolver");
            }
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
        return new ModelAndView();
    }

    private static void setResponseWriter(HttpServletResponse response, ObjectMapper objectMapper, ProblemDetail problemDetail) throws IOException {
        response.setContentType(MediaType.APPLICATION_PROBLEM_JSON_VALUE);
        response.getWriter().write(objectMapper.writeValueAsString(problemDetail));
    }

    private static ProblemDetail getProblemDetailBusinessException(final BusinessException businessException) throws URISyntaxException {
        final ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.UNPROCESSABLE_ENTITY);
        enrichProblemDetail(problemDetail, businessException.getType(), businessException.getMessage());
        return problemDetail;
    }

    private static ProblemDetail getProblemDetailInvalidOrderDataException(final InvalidOrderDataException invalidOrderDataException) throws URISyntaxException {
        final ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        enrichProblemDetail(problemDetail, invalidOrderDataException.getType(), invalidOrderDataException.getMessage());
        return problemDetail;
    }

    private static void enrichProblemDetail(ProblemDetail problemDetail, String invalidOrderDataException, String invalidOrderDataException1) throws URISyntaxException {
        problemDetail.setTitle(invalidOrderDataException);
        problemDetail.setType(new URI("http://localhost:8080/"));
        problemDetail.setDetail(invalidOrderDataException);
        problemDetail.setProperty("timeStamp", LocalDateTime.now());
        problemDetail.setProperty("message", invalidOrderDataException1);
    }
}
