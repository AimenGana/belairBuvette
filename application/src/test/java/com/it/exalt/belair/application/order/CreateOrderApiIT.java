package com.it.exalt.belair.application.order;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class CreateOrderApiIT {

    @Test
    void givenAuthenticatedFestivalierAndAvailableItems_whenPostCommandes_thenReturns201WithCommandeIdAndPendingStatus() {
        // Given
        var festivalierId = "festivalier-42";
    var authenticationProvider = new InMemoryAuthenticationProvider();
    authenticationProvider.authenticate(festivalierId);
    var orderCreationService = new InMemoryOrderCreationService()
        .withAvailableArticle("mojito", 10)
        .withAvailableArticle("eau-plate", 50);
    var api = new CreateOrderApi(authenticationProvider, orderCreationService);

        var request = CreateOrderHttpRequest.of(
                festivalierId,
                List.of(new OrderArticlePayload("mojito", 2))
        );

        // When
        var response = api.postCommandes(request);

        // Then
        assertEquals(201, response.httpStatus());
        assertFalse(response.commandeId().isBlank());
        assertEquals("EN_ATTENTE", response.status());
    }

    @Test
    void givenNoAuthenticatedFestivalier_whenPostCommandes_thenReturns401() {
        // Given
        var authenticationProvider = new InMemoryAuthenticationProvider();
        authenticationProvider.clear();
        var api = new CreateOrderApi(authenticationProvider, new InMemoryOrderCreationService());

        // When
        var response = api.postCommandes(CreateOrderHttpRequest.empty());

        // Then
        assertEquals(401, response.httpStatus());
    }

    @Test
    void givenAuthenticatedFestivalier_whenPostCommandesWithoutArticles_thenReturns400() {
        // Given
        var authenticationProvider = new InMemoryAuthenticationProvider();
        authenticationProvider.authenticate("festivalier-42");
        var api = new CreateOrderApi(authenticationProvider, new InMemoryOrderCreationService());

        var invalidRequest = CreateOrderHttpRequest.of("festivalier-42", List.of());

        // When
        var response = api.postCommandes(invalidRequest);

        // Then
        assertEquals(400, response.httpStatus());
    }
}
