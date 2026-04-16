package com.it.exalt.belair.application.order;

import java.util.List;

public interface OrderCreationService {

    CreateOrderResult createOrder(String festivalierId, List<OrderArticlePayload> articles);
}
