package com.it.exalt.belair.domain.order;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class InMemoryOrderRepository implements OrderRepository {

	private final Map<String, Order> ordersById = new LinkedHashMap<>();

	@Override
	public void save(Order order) {
		ordersById.put(order.id(), order);
	}

	@Override
	public Order findById(String orderId) {
		return ordersById.get(orderId);
	}

	@Override
	public void updateStatus(String orderId, OrderStatus status) {
		var order = ordersById.get(orderId);
		if (order != null) {
			order.updateStatus(status);
		}
	}

	@Override
	public List<Order> findByFestivalierIdAndStatus(FestivalierId festivalierId, OrderStatus status) {
		return ordersById.values().stream()
				.filter(order -> order.festivalierId().equals(festivalierId))
				.filter(order -> order.status() == status)
				.toList();
	}
}
