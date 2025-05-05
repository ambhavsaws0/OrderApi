package com.example.OrderApi.ordermanagementsystem.exceptionhandler;

import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {

    private final String type;

    public BusinessException(String type, String message) {
        super(message);
        this.type = type;
    }
}
