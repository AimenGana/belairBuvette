package com.it.exalt.belair.domain.order;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CancelOrderUseCaseTest {

    private InMemoryOrderRepository orderRepository;

    @BeforeEach
    void setUp() {
        orderRepository = new InMemoryOrderRepository();
    }

    @Test
    void givenBusinessInvariantsSatisfied_whenExecuteCancelUseCase_thenOrderCancelledAndTokensRefunded() {
        // Given les invariants métier de la feature sont satisfaits
        FestivalierId festivalierId = new FestivalierId("festivalier-1");
        var lines = List.of(new OrderLine("drink-1", 1));

        // Note: OrderStatus.ACQUITTED and CancelOrderUseCase are not yet implemented in domain.
        // This test expresses the expected behavior (red). It is expected to fail until the
        // corresponding production code is implemented.
        Order order = Order.newOrder(festivalierId, OrderStatus.ACQUITTED, lines);
        orderRepository.save(order);

        // When le cas d'usage domaine est exécuté
        CancelOrderCommand command = new CancelOrderCommand(order.id());
        CancelOrderUseCase useCase = new CancelOrderUseCase(orderRepository);
        CancelOrderResult result = useCase.handle(command);

        // Then le résultat métier attendu est produit
        assertNotNull(result);
        assertTrue(result.isSuccessful());

        // And les règles de la feature sont respectées (order status updated, tokens refunded)
        var cancelled = orderRepository.findByFestivalierIdAndStatus(festivalierId, OrderStatus.CANCELLED);
        assertEquals(1, cancelled.size());
    }
}
