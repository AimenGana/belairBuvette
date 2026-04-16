package com.it.exalt.belair.application.order;

import java.util.List;

public record CreateOrderResponse(String orderId, List<OrderLineResponse> lines) {
}
