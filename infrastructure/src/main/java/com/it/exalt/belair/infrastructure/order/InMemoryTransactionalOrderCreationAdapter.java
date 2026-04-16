package com.it.exalt.belair.infrastructure.order;

import com.it.exalt.belair.domain.order.FestivalierId;
import com.it.exalt.belair.domain.order.Order;
import com.it.exalt.belair.domain.order.OrderCreationException;
import com.it.exalt.belair.domain.order.OrderErrorCode;
import com.it.exalt.belair.domain.order.OrderLine;
import com.it.exalt.belair.domain.order.OrderStatus;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

public class InMemoryTransactionalOrderCreationAdapter {

    private final ReentrantLock lock = new ReentrantLock();
    private final Map<String, Integer> stockByArticle = new HashMap<>();
    private final Map<String, Order> ordersById = new LinkedHashMap<>();

    public InMemoryTransactionalOrderCreationAdapter withAvailableArticle(String articleId, int quantity) {
        lock.lock();
        try {
            stockByArticle.put(articleId, quantity);
            return this;
        } finally {
            lock.unlock();
        }
    }

    public Order persistOrderWithStockUpdate(FestivalierId festivalierId, List<OrderLine> lines) {
        lock.lock();
        try {
            var transactionalStock = new HashMap<>(stockByArticle);

            for (var line : lines) {
                var available = transactionalStock.get(line.article());
                if (available == null) {
                    throw new OrderCreationException(OrderErrorCode.ARTICLE_INCONNU);
                }
                if (available < line.quantity()) {
                    throw new OrderCreationException(OrderErrorCode.STOCK_INSUFFISANT);
                }
                transactionalStock.put(line.article(), available - line.quantity());
            }

            var order = Order.newOrder(festivalierId, OrderStatus.EN_ATTENTE, lines);

            stockByArticle.clear();
            stockByArticle.putAll(transactionalStock);
            ordersById.put(order.id(), order);

            return order;
        } finally {
            lock.unlock();
        }
    }

    public int availableQuantityOf(String articleId) {
        lock.lock();
        try {
            return stockByArticle.getOrDefault(articleId, 0);
        } finally {
            lock.unlock();
        }
    }

    public Order findById(String orderId) {
        lock.lock();
        try {
            return ordersById.get(orderId);
        } finally {
            lock.unlock();
        }
    }

    public int persistedOrderCount() {
        lock.lock();
        try {
            return ordersById.size();
        } finally {
            lock.unlock();
        }
    }
}
