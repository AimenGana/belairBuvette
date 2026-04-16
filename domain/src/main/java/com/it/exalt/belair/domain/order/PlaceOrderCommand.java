package com.it.exalt.belair.domain.order;

import java.util.List;

public record PlaceOrderCommand(String customerId, List<OrderLine> lines) {
}
