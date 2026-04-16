package com.it.exalt.belair.application.order;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.it.exalt.belair.domain.order.InsufficientBalanceException;
import com.it.exalt.belair.domain.order.OrderLine;
import com.it.exalt.belair.domain.order.OutOfStockException;
import com.it.exalt.belair.domain.order.PlaceOrderResult;
import com.it.exalt.belair.domain.order.PlaceOrderUseCase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderController.class)
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PlaceOrderUseCase placeOrderUseCase;

    @Test
    void shouldReturnSuccessForValidOrder() throws Exception {
        when(placeOrderUseCase.placeOrder(any())).thenReturn(new PlaceOrderResult(
                "order-123",
                List.of(new OrderLine("beer", 2), new OrderLine("fries", 1))
        ));

        CreateOrderRequest request = new CreateOrderRequest(
                "customer-1",
                List.of(new CreateOrderLineRequest("beer", 2), new CreateOrderLineRequest("fries", 1))
        );

        mockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.orderId").value("order-123"))
                .andExpect(jsonPath("$.lines[0].articleId").value("beer"))
                .andExpect(jsonPath("$.lines[0].quantity").value(2))
                .andExpect(jsonPath("$.lines[1].articleId").value("fries"))
                .andExpect(jsonPath("$.lines[1].quantity").value(1));
    }

    @Test
    void shouldReturnFunctionalErrorForOutOfStock() throws Exception {
        when(placeOrderUseCase.placeOrder(any())).thenThrow(new OutOfStockException("beer"));

        CreateOrderRequest request = new CreateOrderRequest(
                "customer-1",
                List.of(new CreateOrderLineRequest("beer", 12))
        );

        mockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.cause").value("RUPTURE_DE_STOCK"));
    }

    @Test
    void shouldReturnFunctionalErrorForInsufficientBalance() throws Exception {
        when(placeOrderUseCase.placeOrder(any())).thenThrow(new InsufficientBalanceException("customer-2"));

        CreateOrderRequest request = new CreateOrderRequest(
                "customer-2",
                List.of(new CreateOrderLineRequest("burger", 5))
        );

        mockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.cause").value("SOLDE_INSUFFISANT"));
    }

    @Test
    void shouldValidatePayloadBeforeCallingUseCase() throws Exception {
        CreateOrderRequest request = new CreateOrderRequest(
                "customer-1",
                List.of(new CreateOrderLineRequest("beer", 0))
        );

        mockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(placeOrderUseCase);
    }
}
