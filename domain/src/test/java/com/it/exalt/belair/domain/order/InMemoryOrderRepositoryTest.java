package com.it.exalt.belair.domain.order;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class InMemoryOrderRepositoryTest {

    @Test
    void givenNewOrderWithTwoLines_whenSave_thenCanRetrieveByIdWithSameStatusAndLines() {
        // Given
        var repository = new InMemoryOrderRepository();
        var order = Order.newOrder(
                FestivalierId.of("festivalier-42"),
                OrderStatus.EN_ATTENTE,
                List.of(
                        new OrderLine("Mojito", 2),
                        new OrderLine("Eau plate", 1)
                )
        );

        // When
        repository.save(order);
        var retrieved = repository.findById(order.id());

        // Then
        assertNotNull(retrieved);
        assertEquals(OrderStatus.EN_ATTENTE, retrieved.status());
        assertEquals(2, retrieved.lines().size());
        assertEquals("Mojito", retrieved.lines().get(0).article());
        assertEquals(2, retrieved.lines().get(0).quantity());
        assertEquals("Eau plate", retrieved.lines().get(1).article());
        assertEquals(1, retrieved.lines().get(1).quantity());
    }

    @Test
    void givenSavedOrderWithEnAttente_whenUpdateStatusToPrete_thenRetrievedOrderHasPreteStatus() {
        // Given
        var repository = new InMemoryOrderRepository();
        var order = Order.newOrder(
                FestivalierId.of("festivalier-42"),
                OrderStatus.EN_ATTENTE,
                List.of(new OrderLine("Mojito", 1))
        );
        repository.save(order);

        // When
        repository.updateStatus(order.id(), OrderStatus.PRETE);
        var retrieved = repository.findById(order.id());

        // Then
        assertEquals(OrderStatus.PRETE, retrieved.status());
    }

    @Test
    void givenThreeOrdersForFestivalierWithTwoPending_whenFindPendingByFestivalier_thenReturnsExactlyTwoOrders() {
        // Given
        var repository = new InMemoryOrderRepository();
        var festivalierId = FestivalierId.of("festivalier-42");
        repository.save(Order.newOrder(festivalierId, OrderStatus.EN_ATTENTE, List.of(new OrderLine("Mojito", 1))));
        repository.save(Order.newOrder(festivalierId, OrderStatus.EN_ATTENTE, List.of(new OrderLine("Eau plate", 1))));
        repository.save(Order.newOrder(festivalierId, OrderStatus.PRETE, List.of(new OrderLine("Mojito", 2))));

        // When
        var pendingOrders = repository.findByFestivalierIdAndStatus(festivalierId, OrderStatus.EN_ATTENTE);

        // Then
        assertEquals(2, pendingOrders.size());
    }
}
