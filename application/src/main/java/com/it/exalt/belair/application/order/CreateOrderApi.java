package com.it.exalt.belair.application.order;

public class CreateOrderApi {

    private final AuthenticationProvider authenticationProvider;
    private final OrderCreationService orderCreationService;

    public CreateOrderApi(AuthenticationProvider authenticationProvider, OrderCreationService orderCreationService) {
        this.authenticationProvider = authenticationProvider;
        this.orderCreationService = orderCreationService;
    }

    public CreateOrderHttpResponse postCommandes(CreateOrderHttpRequest request) {
        if (!authenticationProvider.isAuthenticated()) {
            return new CreateOrderHttpResponse(401, "", "");
        }
        if (request.articles() == null || request.articles().isEmpty()) {
            return new CreateOrderHttpResponse(400, "", "");
        }

        var result = orderCreationService.createOrder(request.festivalierId(), request.articles());
        return new CreateOrderHttpResponse(201, result.commandeId(), result.status());
    }
}
