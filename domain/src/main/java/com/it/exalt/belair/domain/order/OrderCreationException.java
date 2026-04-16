package com.it.exalt.belair.domain.order;

public class OrderCreationException extends RuntimeException {

    private final OrderErrorCode errorCode;

    public OrderCreationException(OrderErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    public OrderErrorCode errorCode() {
        return errorCode;
    }
}
