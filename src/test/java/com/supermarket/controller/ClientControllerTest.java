package com.supermarket.controller;

import com.supermarket.dto.CheckoutResponse;
import com.supermarket.model.Item;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.List;
import java.util.UUID;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class ClientControllerTest {

    private final WebTestClient webTestClient = WebTestClient.bindToServer()
            .baseUrl("http://localhost:8080")
            .build();

    @Test
    void checkout_whenItemsExist_ok(){
        webTestClient.post()
                .uri("/checkout")
                .bodyValue(List.of("apple", "banana"))
                .exchange()
                .expectStatus().isOk()
                .expectBody(CheckoutResponse.class).isEqualTo(new CheckoutResponse(40));
    }

    @Test
    void checkout_whenMultipleQuantity_ok(){
        webTestClient.post()
                .uri("/checkout")
                .bodyValue(List.of("apple", "apple", "apple"))
                .exchange()
                .expectStatus().isOk()
                .expectBody(CheckoutResponse.class).isEqualTo(new CheckoutResponse(30));
    }

    @Test
    void checkout_whenItemDoesNotExist_badRequest(){
        webTestClient.post()
                .uri("/checkout")
                .bodyValue(List.of("apple", "kumquat"))
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(String.class).isEqualTo("Item(s) not found: kumquat");
    }

    @Test
    void checkout_whenRequestBodyDoesNotMatch_badRequest(){
        webTestClient.post()
                .uri("/checkout")
                .bodyValue(new Item(UUID.randomUUID(), "Apple", 10.0, null))
                .exchange()
                .expectStatus().isBadRequest();
    }
}