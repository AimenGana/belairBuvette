package com.it.exalt.belair.application.order;

import com.it.exalt.belair.domain.order.InsufficientBalanceException;
import com.it.exalt.belair.domain.order.OrderLine;
import com.it.exalt.belair.domain.order.OutOfStockException;
import com.it.exalt.belair.domain.order.PlaceOrderCommand;
import com.it.exalt.belair.domain.order.PlaceOrderResult;
import com.it.exalt.belair.domain.order.PlaceOrderUseCase;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {
    private final PlaceOrderUseCase placeOrderUseCase;

    public OrderController(PlaceOrderUseCase placeOrderUseCase) {
        this.placeOrderUseCase = placeOrderUseCase;
    }

    @PostMapping
    public ResponseEntity<CreateOrderResponse> createOrder(@Valid @RequestBody CreateOrderRequest request) {
        PlaceOrderResult result = placeOrderUseCase.placeOrder(new PlaceOrderCommand(
                request.customerId(),
                request.lines().stream()
                        .map(line -> new OrderLine(line.articleId(), line.quantity()))
                        .toList()
        ));

        return ResponseEntity.status(HttpStatus.CREATED).body(new CreateOrderResponse(
                result.orderId(),
                result.lines().stream().map(line -> new OrderLineResponse(line.articleId(), line.quantity())).toList()
        ));
    }

    @ExceptionHandler(OutOfStockException.class)
    public ResponseEntity<FunctionalErrorResponse> onOutOfStock(OutOfStockException exception) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new FunctionalErrorResponse("RUPTURE_DE_STOCK", exception.getMessage()));
    }

    @ExceptionHandler(InsufficientBalanceException.class)
    public ResponseEntity<FunctionalErrorResponse> onInsufficientBalance(InsufficientBalanceException exception) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new FunctionalErrorResponse("SOLDE_INSUFFISANT", exception.getMessage()));
    }
}
