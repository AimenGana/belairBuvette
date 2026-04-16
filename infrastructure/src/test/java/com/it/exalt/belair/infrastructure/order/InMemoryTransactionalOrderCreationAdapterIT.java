package com.it.exalt.belair.infrastructure.order;

import com.it.exalt.belair.domain.order.FestivalierId;
import com.it.exalt.belair.domain.order.OrderCreationException;
import com.it.exalt.belair.domain.order.OrderErrorCode;
import com.it.exalt.belair.domain.order.OrderLine;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class InMemoryTransactionalOrderCreationAdapterIT {

    @Test
    void givenSufficientStock_whenCreateOrderTransaction_thenPersistsOrderAndDecrementsStock() {
        // Given
        var adapter = new InMemoryTransactionalOrderCreationAdapter()
                .withAvailableArticle("mojito", 10)
                .withAvailableArticle("sandwich", 5);

        // When
        var order = adapter.persistOrderWithStockUpdate(
                FestivalierId.of("festivalier-42"),
                List.of(new OrderLine("mojito", 2), new OrderLine("sandwich", 3))
        );

        // Then
        assertEquals(2, order.lines().size());
        assertEquals(8, adapter.availableQuantityOf("mojito"));
        assertEquals(2, adapter.availableQuantityOf("sandwich"));
        assertFalse(adapter.findById(order.id()).lines().isEmpty());
    }

    @Test
    void givenMultiArticleOrder_whenAvailabilityCheckFails_thenRollsBackWithoutStockOrPartialOrderPersistence() {
        // Given
        var adapter = new InMemoryTransactionalOrderCreationAdapter()
                .withAvailableArticle("mojito", 10)
                .withAvailableArticle("sandwich", 1);

        // When
        var exception = org.junit.jupiter.api.Assertions.assertThrows(
                OrderCreationException.class,
                () -> adapter.persistOrderWithStockUpdate(
                        FestivalierId.of("festivalier-42"),
                        List.of(new OrderLine("mojito", 2), new OrderLine("sandwich", 3))
                )
        );

        // Then
        assertEquals(OrderErrorCode.STOCK_INSUFFISANT, exception.errorCode());
        assertEquals(10, adapter.availableQuantityOf("mojito"));
        assertEquals(1, adapter.availableQuantityOf("sandwich"));
        assertEquals(0, adapter.persistedOrderCount());
    }

    @Test
    void givenConcurrentOrdersOnLimitedStock_whenExecutedConcurrently_thenOnlyOneConsumesRemainingStock() throws InterruptedException {
        // Given
        var adapter = new InMemoryTransactionalOrderCreationAdapter()
                .withAvailableArticle("mojito", 1);

        var start = new CountDownLatch(1);
        var done = new CountDownLatch(2);
        var successes = new AtomicInteger(0);
        var stockFailures = new AtomicInteger(0);

        Runnable placeOrder = () -> {
            try {
                assertTrue(start.await(1, TimeUnit.SECONDS), "Le démarrage concurrent des threads a expiré");
                adapter.persistOrderWithStockUpdate(
                        FestivalierId.of("festivalier-42"),
                        List.of(new OrderLine("mojito", 1))
                );
                successes.incrementAndGet();
            } catch (OrderCreationException exception) {
                if (exception.errorCode() == OrderErrorCode.STOCK_INSUFFISANT) {
                    stockFailures.incrementAndGet();
                }
            } catch (InterruptedException ignored) {
                Thread.currentThread().interrupt();
            } finally {
                done.countDown();
            }
        };

        var first = new Thread(placeOrder);
        var second = new Thread(placeOrder);
        first.start();
        second.start();

        // When
        start.countDown();

        // Then
        assertTrue(done.await(2, TimeUnit.SECONDS));
        assertEquals(1, successes.get());
        assertEquals(1, stockFailures.get());
        assertEquals(0, adapter.availableQuantityOf("mojito"));
        assertEquals(1, adapter.persistedOrderCount());
    }
}
