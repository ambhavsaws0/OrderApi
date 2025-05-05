package com.example.OrderApi.ordermanagementsystem.exceptionhandler;

public class InvalidOrderDataException extends BusinessException {

    public InvalidOrderDataException(String type, String message) {
        super(type, message);
    }
}
