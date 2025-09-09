package com.supermarket.service;

import com.supermarket.dto.ItemRequest;
import com.supermarket.exception.SupermarketException;
import com.supermarket.model.Item;
import com.supermarket.repository.ItemRepository;
import com.supermarket.util.ItemMapper;
import com.supermarket.util.ItemRequestValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final ItemRepository itemRepository;
    private final ItemMapper itemMapper;
    public List<Item> addItems(List<ItemRequest> itemRequestList){
        ItemRequestValidator.validate(itemRequestList);
        List<Item> existingItems = itemRepository.findAllByNameInIgnoreCase(itemRequestList.stream().map(ItemRequest::name).toList());
        if(!existingItems.isEmpty()) {
            throw new SupermarketException(String.format("Duplicate item(s): %s.", existingItems.stream().map(Item::getName).collect(Collectors.joining(", "))));
        }
        return itemRepository.saveAll(itemRequestList.stream().map(itemMapper::map).toList());
    }

    public void updateItem(UUID id, ItemRequest itemRequest) {
        if(!ItemRequestValidator.validateItemRequest(itemRequest)){
            throw new SupermarketException("Invalid offer fields.");
        }
        Item item = itemRepository.findById(id).orElseThrow(() -> new SupermarketException(String.format("No item found with ID: %s",id)));
        if(!item.getName().equals(itemRequest.name())) {
            throw new SupermarketException(String.format("Names do not match for item with ID: %s", id));
        }
        itemRepository.save(itemMapper.map(id, itemRequest));
    }

    public Item getItem(String name) {
        return itemRepository.findByNameIgnoreCase(name);
    }
}
