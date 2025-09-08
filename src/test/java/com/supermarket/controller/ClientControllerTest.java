package com.supermarket.controller;

import com.supermarket.dto.CheckoutResponse;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class ClientControllerTest {

    private final WebTestClient webTestClient = WebTestClient.bindToServer()
            .baseUrl("http://localhost:8080")
            .build();

    @Test
    void checkout_ok(){
        webTestClient.post()
                .uri("/checkout")
                .bodyValue(List.of("apple", "banana"))
                .exchange()
                .expectStatus().isOk()
                .expectBody(CheckoutResponse.class).isEqualTo(new CheckoutResponse(40));
    }
}