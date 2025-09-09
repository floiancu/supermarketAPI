package com.supermarket.controller;

import com.supermarket.dto.AddItemResponse;
import com.supermarket.dto.ItemRequest;
import com.supermarket.dto.ItemResponse;
import com.supermarket.dto.OfferResponse;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;
import util.TestUtils;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class AdminControllerTest {

    private final WebTestClient webTestClient = WebTestClient.bindToServer()
            .baseUrl("http://localhost:8080/admin")
            .build();

    @Test
    void addItems_whenBodyOk_ok(){
        webTestClient.post()
                .uri("/item")
                .bodyValue(List.of(new ItemRequest("Grapes", 20.0, 2, 15.0,
                        LocalDate.of(2025, 9, 1), LocalDate.of(2025, 9, 7))))
                .exchange()
                .expectStatus().isOk()
                .expectBody(AddItemResponse.class).isEqualTo(new AddItemResponse(1));
    }

    @Test
    void addItems_whenWithoutOffer_ok(){
        webTestClient.post()
                .uri("/item")
                .bodyValue(List.of(TestUtils.buildItemRequestWithoutOffer()))
                .exchange()
                .expectStatus().isOk()
                .expectBody(AddItemResponse.class).isEqualTo(new AddItemResponse(1));
        ItemResponse result = webTestClient.get()
                .uri("/item/apricot")
                .exchange()
                .expectStatus().isOk()
                .expectBody(ItemResponse.class)
                .returnResult()
                .getResponseBody();
        assertEquals("Apricot", result.name());
        assertEquals(10, result.price());
        assertNull(result.offer());
    }

    @Test
    void addItems_whenWithInvalidOffer_badRequest(){
        String errorResponse = webTestClient.post()
                .uri("/item")
                .bodyValue(List.of(TestUtils.buildItemRequestWithOfferFields(null, 15.0,
                        LocalDate.of(2025, 9, 1), LocalDate.of(2025, 9, 7))))
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(String.class).returnResult().getResponseBody();
        assertTrue(errorResponse.contains("Apple: invalid offer fields."));
    }

    @Test
    void addItems_whenWithPriceZero_badRequest(){
        webTestClient.post()
                .uri("/item")
                .bodyValue(List.of(TestUtils.buildItemRequestWithPrice(0.0)))
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void addItems_whenItemNameDuplicate_badRequest(){
        webTestClient.post()
                .uri("/item")
                .bodyValue(List.of(TestUtils.buildItemRequestWithAllFields()))
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void updateItem_whenRequestValid_ok(){
        webTestClient.put()
                .uri("/item/0fe10956-ed17-4ac9-a1de-ef55417c362d")
                .bodyValue(TestUtils.buildBananaItemRequestWithAllFields())
                .exchange()
                .expectStatus().isOk();
        ItemResponse result = webTestClient.get()
                .uri("/item/banana")
                .exchange()
                .expectStatus().isOk()
                .expectBody(ItemResponse.class)
                .returnResult()
                .getResponseBody();
        assertEquals("Banana", result.name());
        assertEquals(25, result.price());
        assertNotNull(result.offer());
        assertEquals(2, result.offer().quantity());
        assertEquals(15, result.offer().price());
        assertEquals(LocalDate.of(2025, 9, 1), result.offer().startDate());
        assertEquals(LocalDate.of(2025, 9, 7), result.offer().endDate());
    }

    @Test
    void updateItem_whenNamesDoNotMatch_badRequest(){
        String errorMessage = webTestClient.put()
                .uri("/item/259a8a6b-ddea-491e-85e9-a6ec4511ad9f")
                .bodyValue(TestUtils.buildItemRequestWithoutOffer())
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(String.class).returnResult().getResponseBody();
        assertTrue(errorMessage.contains("Names do not match for item with ID:"));
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