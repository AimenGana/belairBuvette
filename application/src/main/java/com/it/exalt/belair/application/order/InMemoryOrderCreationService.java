package com.it.exalt.belair.application.order;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class InMemoryOrderCreationService implements OrderCreationService {

    private static final AtomicInteger SEQUENCE = new AtomicInteger(0);

    private final Map<String, Integer> stockByArticle = new HashMap<>();

    public InMemoryOrderCreationService withAvailableArticle(String articleId, int quantity) {
        stockByArticle.put(articleId, quantity);
        return this;
    }

    @Override
    public CreateOrderResult createOrder(String festivalierId, List<OrderArticlePayload> articles) {
        var article = articles.getFirst();
        var available = stockByArticle.getOrDefault(article.id(), 0);
        stockByArticle.put(article.id(), Math.max(0, available - article.quantite()));
        var commandeId = "commande-" + SEQUENCE.incrementAndGet();
        return new CreateOrderResult(commandeId, "EN_ATTENTE");
    }
}
