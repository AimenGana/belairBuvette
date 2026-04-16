package com.it.exalt.belair.infrastructure.order;

import com.it.exalt.belair.domain.order.InsufficientBalanceException;
import com.it.exalt.belair.domain.order.OrderLine;
import com.it.exalt.belair.domain.order.OutOfStockException;
import com.it.exalt.belair.domain.order.PlaceOrderCommand;
import com.it.exalt.belair.domain.order.PlaceOrderResult;
import com.it.exalt.belair.domain.order.PlaceOrderUseCase;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class InMemoryPlaceOrderUseCase implements PlaceOrderUseCase {

    private static final Map<String, Integer> INITIAL_STOCK_BY_ARTICLE = Map.of(
            "beer", 10,
            "soda", 10,
            "fries", 10,
            "burger", 5
    );

    private static final Map<String, Integer> PRICE_BY_ARTICLE = Map.of(
            "beer", 2,
            "soda", 1,
            "fries", 2,
            "burger", 5
    );

    private static final Map<String, Integer> INITIAL_TOKENS_BY_CUSTOMER = Map.of(
            "customer-1", 20,
            "customer-2", 3
    );

    private final Map<String, Integer> stockByArticle = new HashMap<>(INITIAL_STOCK_BY_ARTICLE);
    private final Map<String, Integer> tokensByCustomer = new HashMap<>(INITIAL_TOKENS_BY_CUSTOMER);

    @Override
    public synchronized PlaceOrderResult placeOrder(PlaceOrderCommand command) {
        for (OrderLine line : command.lines()) {
            int availableStock = stockByArticle.getOrDefault(line.articleId(), 0);
            if (line.quantity() > availableStock) {
                throw new OutOfStockException(line.articleId());
            }
        }

        int totalCost = command.lines().stream()
                .mapToInt(line -> line.quantity() * PRICE_BY_ARTICLE.getOrDefault(line.articleId(), 0))
                .sum();

        int availableTokens = tokensByCustomer.getOrDefault(command.customerId(), 0);
        if (totalCost > availableTokens) {
            throw new InsufficientBalanceException(command.customerId());
        }

        for (OrderLine line : command.lines()) {
            int updatedStock = stockByArticle.get(line.articleId()) - line.quantity();
            stockByArticle.put(line.articleId(), updatedStock);
        }
        tokensByCustomer.put(command.customerId(), availableTokens - totalCost);

        return new PlaceOrderResult(UUID.randomUUID().toString(), command.lines());
    }
}
