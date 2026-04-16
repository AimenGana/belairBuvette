package com.it.exalt.belair.domain.order;

public class OutOfStockException extends RuntimeException {
    public OutOfStockException(String articleId) {
        super("Article out of stock: " + articleId);
    }
}
