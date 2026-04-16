package com.it.exalt.belair.infrastructure.order;

import com.it.exalt.belair.domain.order.InsufficientBalanceException;
import com.it.exalt.belair.domain.order.OrderLine;
import com.it.exalt.belair.domain.order.OutOfStockException;
import com.it.exalt.belair.domain.order.PlaceOrderCommand;
import com.it.exalt.belair.domain.order.PlaceOrderResult;
import com.it.exalt.belair.domain.order.PlaceOrderUseCase;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

@Service
public class InMemoryPlaceOrderUseCase implements PlaceOrderUseCase {

    private static final Map<String, Integer> STOCK_BY_ARTICLE = Map.of(
            "beer", 10,
            "soda", 10,
            "fries", 10,
            "burger", 5
    );

    private static final Map<String, Integer> TOKENS_BY_CUSTOMER = Map.of(
            "customer-1", 20,
            "customer-2", 3
    );

    @Override
    public PlaceOrderResult placeOrder(PlaceOrderCommand command) {
        for (OrderLine line : command.lines()) {
            int availableStock = STOCK_BY_ARTICLE.getOrDefault(line.articleId(), Integer.MAX_VALUE);
            if (line.quantity() > availableStock || line.articleId().toLowerCase().contains("rupture")) {
                throw new OutOfStockException(line.articleId());
            }
        }

        int totalCost = command.lines().stream().mapToInt(OrderLine::quantity).sum();
        int availableTokens = TOKENS_BY_CUSTOMER.getOrDefault(command.customerId(), 20);

        if (command.customerId().toLowerCase().contains("insuffisant")
                || command.customerId().toLowerCase().contains("insufficient")
                || totalCost > availableTokens) {
            throw new InsufficientBalanceException(command.customerId());
        }

        return new PlaceOrderResult(UUID.randomUUID().toString(), command.lines());
    }
}
