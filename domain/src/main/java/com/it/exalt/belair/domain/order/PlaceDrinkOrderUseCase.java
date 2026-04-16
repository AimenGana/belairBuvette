package com.it.exalt.belair.domain.order;

public class PlaceDrinkOrderUseCase {

    private final DrinkCatalog drinkCatalog;
    private final OrderRepository orderRepository;

    public PlaceDrinkOrderUseCase(DrinkCatalog drinkCatalog, OrderRepository orderRepository) {
        this.drinkCatalog = drinkCatalog;
        this.orderRepository = orderRepository;
    }

    public PlaceDrinkOrderResult handle(PlaceDrinkOrderCommand command) {
        if (!drinkCatalog.hasArticle(command.drinkName())) {
            throw new OrderCreationException(OrderErrorCode.ARTICLE_INCONNU);
        }
        if (!drinkCatalog.hasSufficientStock(command.drinkName(), command.quantity())) {
            throw new OrderCreationException(OrderErrorCode.STOCK_INSUFFISANT);
        }
        drinkCatalog.decrementStock(command.drinkName(), command.quantity());

        var order = Order.newOrder(
                command.festivalierId(),
                OrderStatus.EN_ATTENTE,
                java.util.List.of(new OrderLine(command.drinkName(), command.quantity()))
        );
        orderRepository.save(order);

        return new PlaceDrinkOrderResult(order.status(), order.id());
    }
}
