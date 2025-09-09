package com.supermarket.controller;

import com.supermarket.dto.AddItemResponse;
import com.supermarket.dto.ItemRequest;
import com.supermarket.dto.ItemResponse;
import com.supermarket.dto.OfferResponse;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class AdminControllerTest {

    private final WebTestClient webTestClient = WebTestClient.bindToServer()
            .baseUrl("http://localhost:8080/admin")
            .build();

    @Test
    void addItems_whenBodyOk_ok(){
        webTestClient.post()
                .uri("/item")
                .bodyValue(List.of(new ItemRequest("Grapes", 20, 2, 15,
                        LocalDate.of(2025, 9, 1), LocalDate.of(2025, 9, 7))))
                .exchange()
                .expectStatus().isOk()
                .expectBody(AddItemResponse.class).isEqualTo(new AddItemResponse(1));
    }

    @Test
    void getItem_whenItemExists_ok(){
        webTestClient.get()
                .uri("/item/peach")
                .exchange()
                .expectStatus().isOk()
                .expectBody(ItemResponse.class)
                    .isEqualTo(new ItemResponse(UUID.fromString("fe7cabb7-c0f7-4f22-87ea-4c3a3b0585d8"), "Peach", 20,
                            new OfferResponse(UUID.fromString("a4a1d2db-f633-4da7-a03e-08f044c024f8"), 30, 2, LocalDate.of(2025, 9, 1), LocalDate.of(2025, 9, 7))));
    }
}