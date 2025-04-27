package com.example.OrderApi.ordermanagementsystem.exception;

import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {

    private final String type;

    public BusinessException(String type, String message) {
        super(message);
        this.type = type;
    }
}
