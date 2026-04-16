package com.it.exalt.belair.application.order;

public record CreateOrderHttpResponse(int httpStatus, String commandeId, String status) {
}
