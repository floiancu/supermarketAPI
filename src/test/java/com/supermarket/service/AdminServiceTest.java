package com.supermarket.service;

import com.supermarket.dto.ItemRequest;
import com.supermarket.exception.SupermarketException;
import com.supermarket.model.Item;
import com.supermarket.repository.ItemRepository;
import com.supermarket.util.ItemMapper;
import com.supermarket.util.ItemMapperImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import util.TestUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdminServiceTest {
    @InjectMocks
    private AdminService adminService;
    @Mock
    private ItemRepository itemRepository;
    @Spy
    private ItemMapper itemMapper = new ItemMapperImpl();
    @Captor
    private ArgumentCaptor<List<Item>> itemListArgumentCaptor = ArgumentCaptor.forClass(List.class);
    @Captor
    private ArgumentCaptor<Item> itemArgumentCaptor = ArgumentCaptor.forClass(Item.class);

    @Test
    void addItem_whenItemValid() {
        doReturn(new ArrayList<>()).when(itemRepository).findAllByNameInIgnoreCase(List.of("Apple"));

        ItemRequest itemRequest = TestUtils.buildItemRequestWithAllFields();
        adminService.addItems(List.of(itemRequest));

        verify(itemRepository).saveAll(itemListArgumentCaptor.capture());
        List<Item> capturedItems = itemListArgumentCaptor.getValue();
        assertEquals(1, capturedItems.size());
        assertEquals(itemRequest.name(), capturedItems.get(0).getName());
        assertEquals(itemRequest.price(), capturedItems.get(0).getPrice());
        assertEquals(itemRequest.offerPrice(), capturedItems.get(0).getOffer().getPrice());
        assertEquals(itemRequest.offerQuantity(), capturedItems.get(0).getOffer().getQuantity());
        assertEquals(itemRequest.offerStartDate(), capturedItems.get(0).getOffer().getStartDate());
        assertEquals(itemRequest.offerEndDate(), capturedItems.get(0).getOffer().getEndDate());
    }

    @Test
    void addItem_whenItemAlreadyExists() {
        doReturn(List.of(TestUtils.buildApple())).when(itemRepository).findAllByNameInIgnoreCase(List.of("Apple"));
        ItemRequest itemRequest = TestUtils.buildItemRequestWithAllFields();

        SupermarketException supermarketException = assertThrows(SupermarketException.class, () -> adminService.addItems(List.of(itemRequest)));
        assertEquals("Duplicate item(s): Apple.", supermarketException.getMessage());
        verify(itemRepository, never()).saveAll(any());
    }

    @Test
    void addItem_whenRequestIsInvalid() {
        ItemRequest itemRequest = TestUtils.buildItemRequestWithOfferFields(null, 10.0, LocalDate.now(), LocalDate.now().minusDays(1));

        SupermarketException supermarketException = assertThrows(SupermarketException.class, () -> adminService.addItems(List.of(itemRequest)));
        assertEquals("Apple: invalid offer fields.", supermarketException.getMessage());
        verify(itemRepository, never()).saveAll(any());
    }

    @Test
    void updateItem_whenItemRequestValid() {
        Item item = TestUtils.buildApple();
        doReturn(Optional.of(item)).when(itemRepository).findById(any());
        ItemRequest itemRequest = TestUtils.buildItemRequestWithAllFields();

        adminService.updateItem(item.getId(), itemRequest);
        verify(itemRepository).save(itemArgumentCaptor.capture());
        Item capturedItem = itemArgumentCaptor.getValue();
        assertEquals(itemRequest.name(), capturedItem.getName());
        assertEquals(itemRequest.price(), capturedItem.getPrice());
        assertEquals(itemRequest.offerQuantity(), capturedItem.getOffer().getQuantity());
        assertEquals(itemRequest.offerPrice(), capturedItem.getOffer().getPrice());
        assertEquals(itemRequest.offerStartDate(), capturedItem.getOffer().getStartDate());
        assertEquals(itemRequest.offerEndDate(), capturedItem.getOffer().getEndDate());
    }

    @Test
    void updateItem_whenItemIdDoesNotExist() {
        doReturn(Optional.empty()).when(itemRepository).findById(any());
        ItemRequest itemRequest = TestUtils.buildItemRequestWithAllFields();
        UUID id = UUID.randomUUID();

        SupermarketException supermarketException = assertThrows(SupermarketException.class, () -> adminService.updateItem(id, itemRequest));
        assertEquals("No item found with ID: " + id, supermarketException.getMessage());
        verify(itemRepository, never()).save(any());
    }

    @Test
    void updateItem_whenItemRequestInvalid() {
        ItemRequest itemRequest = TestUtils.buildItemRequestWithOfferFields(2, 20.0, null, null);
        UUID id = UUID.randomUUID();

        SupermarketException supermarketException = assertThrows(SupermarketException.class, () -> adminService.updateItem(id, itemRequest));
        assertEquals("Invalid offer fields.", supermarketException.getMessage());
        verify(itemRepository, never()).save(any());
    }

    @Test
    void updateItem_whenItemNamesDoNotMatch() {
        doReturn(Optional.of(TestUtils.buildApple())).when(itemRepository).findById(any());
        ItemRequest itemRequest = TestUtils.buildItemRequestWithoutOffer();
        UUID id = UUID.randomUUID();

        SupermarketException supermarketException = assertThrows(SupermarketException.class, () -> adminService.updateItem(id, itemRequest));
        assertEquals("Names do not match for item with ID: " + id, supermarketException.getMessage());
        verify(itemRepository, never()).save(any());
    }

    @Test
    void getItem_whenItemExists() {
        Item item = TestUtils.buildApple();
        doReturn(item).when(itemRepository).findByNameIgnoreCase("apple");

        assertEquals(item, adminService.getItem("apple"));
    }
}