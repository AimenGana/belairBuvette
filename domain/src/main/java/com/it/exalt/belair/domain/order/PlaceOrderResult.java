package com.it.exalt.belair.domain.order;

import java.util.List;

public record PlaceOrderResult(String orderId, List<OrderLine> lines) {
}
