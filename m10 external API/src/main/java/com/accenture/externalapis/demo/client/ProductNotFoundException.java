package com.accenture.externalapis.demo.client;

public class ProductNotFoundException extends ClientException {
    public ProductNotFoundException(String message) {
        super(message);
    }
}
