package com.supermarket.service;

import com.supermarket.dto.ItemRequest;
import com.supermarket.model.Item;
import com.supermarket.repository.ItemRepository;
import com.supermarket.util.ItemMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final ItemRepository itemRepository;
    private final ItemMapper itemMapper;
    public List<Item> addItems(List<ItemRequest> itemRequestList){
        return itemRepository.saveAll(itemRequestList.stream().map(itemMapper::map).toList());
    }

    public Item getItem(String name) {
        return itemRepository.findByNameIgnoreCase(name);
    }
}
