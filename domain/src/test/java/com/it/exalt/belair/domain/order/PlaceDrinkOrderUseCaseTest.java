package com.it.exalt.belair.domain.order;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class PlaceDrinkOrderUseCaseTest {

    @Test
    void shouldCreatePendingOrderWithIdForAvailableMojito() {
        // Given
        var festivalierId = FestivalierId.of("fest-123");
        var commandHandler = new PlaceDrinkOrderUseCase(
                new InMemoryDrinkCatalog().withAvailable("Mojito"),
                new InMemoryOrderRepository()
        );

        // When
        var result = commandHandler.handle(new PlaceDrinkOrderCommand(festivalierId, "Mojito", 1));

        // Then
        assertEquals(OrderStatus.EN_ATTENTE, result.status());
        assertNotNull(result.orderId());
    }

    @org.junit.jupiter.api.Test
    void givenAvailableBeer_whenOrderTwoUnits_thenOrderCreatedAndStockDecremented() {
        // Given un article "Bière Pale Ale" disponible en stock (10 unités)
        var festivalierId = FestivalierId.of("fest-321");
        var catalog = new InMemoryDrinkCatalog().withStock("Bière Pale Ale", 10);
        var repository = new InMemoryOrderRepository();
        var useCase = new PlaceDrinkOrderUseCase(catalog, repository);

        // When un client commande 2 unités
        var result = useCase.handle(new PlaceDrinkOrderCommand(festivalierId, "Bière Pale Ale", 2));

        // Then la commande est créée avec succès
        assertEquals(OrderStatus.EN_ATTENTE, result.status());
        assertNotNull(result.orderId());

        // And le stock est décrémenté de 2
        assertEquals(8, catalog.availableQuantityOf("Bière Pale Ale"));
    }
}