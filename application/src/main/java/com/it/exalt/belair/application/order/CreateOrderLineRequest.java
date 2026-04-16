package com.it.exalt.belair.application.order;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record CreateOrderLineRequest(
        @NotBlank String articleId,
        @Positive int quantity
) {
}
