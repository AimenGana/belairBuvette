package com.it.exalt.belair.application.order;

import java.util.List;

public record CreateOrderHttpRequest(String festivalierId, List<OrderArticlePayload> articles) {

    public static CreateOrderHttpRequest of(String festivalierId, List<OrderArticlePayload> articles) {
        return new CreateOrderHttpRequest(festivalierId, articles);
    }

    public static CreateOrderHttpRequest empty() {
        return new CreateOrderHttpRequest("", List.of());
    }
}
