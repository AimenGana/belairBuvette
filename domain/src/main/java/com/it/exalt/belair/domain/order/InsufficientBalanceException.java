package com.it.exalt.belair.domain.order;

public class InsufficientBalanceException extends RuntimeException {
    public InsufficientBalanceException(String customerId) {
        super("Insufficient token balance for customer: " + customerId);
    }
}
