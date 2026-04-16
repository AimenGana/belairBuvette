package com.it.exalt.belair.domain.order;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class Order {

    private static final AtomicInteger SEQUENCE = new AtomicInteger(0);

    private final String id;
    private final FestivalierId festivalierId;
    private final List<OrderLine> lines;
    private OrderStatus status;

    private Order(String id, FestivalierId festivalierId, OrderStatus status, List<OrderLine> lines) {
        this.id = id;
        this.festivalierId = festivalierId;
        this.status = status;
        this.lines = List.copyOf(lines);
    }

    public static Order newOrder(FestivalierId festivalierId, OrderStatus status, List<OrderLine> lines) {
        var id = "order-" + SEQUENCE.incrementAndGet();
        return new Order(id, festivalierId, status, lines);
    }

    public String id() {
        return id;
    }

    public FestivalierId festivalierId() {
        return festivalierId;
    }

    public OrderStatus status() {
        return status;
    }

    public List<OrderLine> lines() {
        return lines;
    }

    public void updateStatus(OrderStatus status) {
        this.status = status;
    }
}
