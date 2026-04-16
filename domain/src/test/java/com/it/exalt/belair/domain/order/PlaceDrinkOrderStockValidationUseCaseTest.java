package com.it.exalt.belair.domain.order;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PlaceDrinkOrderStockValidationUseCaseTest {

    @Test
    void givenSufficientStock_whenCreateOrder_thenCreatesPendingOrderAndDecrementsStock() {
        // Given
        var festivalierId = FestivalierId.of("festivalier-42");
        var catalog = new InMemoryDrinkCatalog().withStock("Mojito", 10);
        var useCase = new PlaceDrinkOrderUseCase(catalog, new InMemoryOrderRepository());

        // When
        var result = useCase.handle(new PlaceDrinkOrderCommand(festivalierId, "Mojito", 2));

        // Then
        assertEquals(OrderStatus.EN_ATTENTE, result.status());
        assertEquals(8, catalog.availableQuantityOf("Mojito"));
    }

    @Test
    void givenInsufficientStock_whenCreateOrder_thenRefusesOrderWithStockInsuffisantErrorAndKeepsStockUnchanged() {
        // Given
        var festivalierId = FestivalierId.of("festivalier-42");
        var catalog = new InMemoryDrinkCatalog().withStock("Mojito", 1);
        var useCase = new PlaceDrinkOrderUseCase(catalog, new InMemoryOrderRepository());

        // When
        var exception = assertThrows(
                OrderCreationException.class,
                () -> useCase.handle(new PlaceDrinkOrderCommand(festivalierId, "Mojito", 2))
        );

        // Then
        assertEquals(OrderErrorCode.STOCK_INSUFFISANT, exception.errorCode());
        assertEquals(1, catalog.availableQuantityOf("Mojito"));
    }

    @Test
    void givenEmptyCatalog_whenCreateOrder_thenRefusesOrderWithArticleInconnuError() {
        // Given
        var festivalierId = FestivalierId.of("festivalier-42");
        var catalog = new InMemoryDrinkCatalog();
        var useCase = new PlaceDrinkOrderUseCase(catalog, new InMemoryOrderRepository());

        // When
        var exception = assertThrows(
                OrderCreationException.class,
                () -> useCase.handle(new PlaceDrinkOrderCommand(festivalierId, "Champagne", 1))
        );

        // Then
        assertEquals(OrderErrorCode.ARTICLE_INCONNU, exception.errorCode());
    }
}
