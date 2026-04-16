package com.it.exalt.belair.domain.order;

public interface PlaceOrderUseCase {
    PlaceOrderResult placeOrder(PlaceOrderCommand command);
}
