package com.it.exalt.belair.application.order;

import com.it.exalt.belair.domain.order.FestivalierId;
import com.it.exalt.belair.domain.order.OrderLine;
import com.it.exalt.belair.infrastructure.order.InMemoryTransactionalOrderCreationAdapter;

import java.util.List;

public class InMemoryOrderCreationService implements OrderCreationService {

    private final InMemoryTransactionalOrderCreationAdapter adapter = new InMemoryTransactionalOrderCreationAdapter();

    public InMemoryOrderCreationService withAvailableArticle(String articleId, int quantity) {
        adapter.withAvailableArticle(articleId, quantity);
        return this;
    }

    @Override
    public CreateOrderResult createOrder(String festivalierId, List<OrderArticlePayload> articles) {
        var lines = articles.stream()
                .map(article -> new OrderLine(article.id(), article.quantite()))
                .toList();

        var order = adapter.persistOrderWithStockUpdate(FestivalierId.of(festivalierId), lines);
        return new CreateOrderResult(order.id(), order.status().name());
    }
}
