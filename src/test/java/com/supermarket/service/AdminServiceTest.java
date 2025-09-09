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
    private ArgumentCaptor<List<Item>> itemArgumentCaptor = ArgumentCaptor.forClass(List.class);

    @Test
    void addItem_whenItemValid() {
        doReturn(new ArrayList<>()).when(itemRepository).findAllByNameInIgnoreCase(List.of("Apple"));

        ItemRequest itemRequest = TestUtils.buildItemRequestWithAllFields();
        adminService.addItems(List.of(itemRequest));

        verify(itemRepository).saveAll(itemArgumentCaptor.capture());
        List<Item> capturedItems = itemArgumentCaptor.getValue();
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
    void getItem_whenItemExists() {
        Item item = TestUtils.buildApple();
        doReturn(item).when(itemRepository).findByNameIgnoreCase("apple");

        assertEquals(item, adminService.getItem("apple"));
    }
}