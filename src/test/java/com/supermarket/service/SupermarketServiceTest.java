package com.supermarket.service;

import com.supermarket.exception.SupermarketException;
import com.supermarket.model.Item;
import com.supermarket.model.Offer;
import com.supermarket.repository.ItemRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class SupermarketServiceTest {

    @InjectMocks
    private SupermarketService supermarketService;
    @Mock
    private ItemRepository itemRepository;

    @Test
    void checkout_oneItem_ok() {
        doReturn(List.of(new Item(UUID.randomUUID(), "Apple", 10.0, null)))
                .when(itemRepository).findAllByNameInIgnoreCase(List.of("apple"));

        assertEquals(10, supermarketService.checkout(List.of("apple")));
    }

    @Test
    void checkout_twoItems_ok() {
        doReturn(List.of(new Item(UUID.randomUUID(), "Apple", 10.0, null)))
                .when(itemRepository).findAllByNameInIgnoreCase(List.of("apple"));

        assertEquals(20, supermarketService.checkout(List.of("apple", "apple")));
    }

    @Test
    void checkout_twoDifferentItems_ok() {
        doReturn(List.of(new Item(UUID.randomUUID(), "Apple", 10.0, null),
                new Item(UUID.randomUUID(), "Banana", 30.0, null)))
                .when(itemRepository).findAllByNameInIgnoreCase(List.of("banana", "apple"));

        assertEquals(40, supermarketService.checkout(List.of("apple", "banana")));
    }

    @Test
    void checkout_itemsOnOffer_ok() {
        doReturn(List.of(new Item(UUID.randomUUID(), "Apple", 10.0,
                new Offer(UUID.randomUUID(), 25.0, 3, LocalDate.now().minusDays(3), LocalDate.now().plusDays(3)))))
                .when(itemRepository).findAllByNameInIgnoreCase(List.of("apple"));

        assertEquals(25, supermarketService.checkout(List.of("apple", "apple", "apple")));
    }

    @Test
    void checkout_itemsOnOffer_moreItemsThanOffer() {
        doReturn(List.of(new Item(UUID.randomUUID(), "Apple", 10.0,
                new Offer(UUID.randomUUID(), 25.0, 3, LocalDate.now().minusDays(3), LocalDate.now().plusDays(3)))))
                .when(itemRepository).findAllByNameInIgnoreCase(List.of("apple"));

        assertEquals(35, supermarketService.checkout(List.of("apple", "apple", "apple", "apple")));
    }

    @Test
    void checkout_itemsOnOffer_offerNotValidNow() {
        doReturn(List.of(new Item(UUID.randomUUID(), "Apple", 10.0,
                new Offer(UUID.randomUUID(), 25.0, 3, LocalDate.now().minusMonths(1), LocalDate.now().minusWeeks(1)))))
                .when(itemRepository).findAllByNameInIgnoreCase(List.of("apple"));

        assertEquals(30, supermarketService.checkout(List.of("apple", "apple", "apple")));
    }

    @Test
    void checkout_itemsNotFound() {
        doReturn(new ArrayList<>())
                .when(itemRepository).findAllByNameInIgnoreCase(List.of("apple"));

        SupermarketException supermarketException = assertThrows(SupermarketException.class, () -> supermarketService.checkout(List.of("apple")));
        assertEquals("Item(s) not found: apple", supermarketException.getMessage());
    }
}