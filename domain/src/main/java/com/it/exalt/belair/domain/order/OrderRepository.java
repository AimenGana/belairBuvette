package com.it.exalt.belair.domain.order;

import java.util.List;

public interface OrderRepository {

    void save(Order order);

    Order findById(String orderId);

    void updateStatus(String orderId, OrderStatus status);

    List<Order> findByFestivalierIdAndStatus(FestivalierId festivalierId, OrderStatus status);
}
