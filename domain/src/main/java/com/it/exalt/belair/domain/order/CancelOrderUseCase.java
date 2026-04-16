package com.it.exalt.belair.domain.order;

public class CancelOrderUseCase {

    private final OrderRepository orderRepository;

    public CancelOrderUseCase(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public CancelOrderResult handle(CancelOrderCommand command) {
        var order = orderRepository.findById(command.orderId());
        if (order == null) return new CancelOrderResult(false);

        orderRepository.updateStatus(order.id(), OrderStatus.CANCELLED);
        return new CancelOrderResult(true);
    }
}
