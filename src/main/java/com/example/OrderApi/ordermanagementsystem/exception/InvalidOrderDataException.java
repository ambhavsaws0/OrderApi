package com.example.OrderApi.ordermanagementsystem.exception;

public class InvalidOrderDataException extends BusinessException {

    public InvalidOrderDataException(String type, String message) {
        super(type, message);
    }
}
