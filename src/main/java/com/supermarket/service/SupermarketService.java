package com.supermarket.service;

import com.supermarket.exception.SupermarketException;
import com.supermarket.model.Item;
import com.supermarket.model.Offer;
import com.supermarket.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SupermarketService {

    private final ItemRepository itemRepository;

    public Double checkout(final List<String> basketContents) {
        Map<String, Long> itemsAndQuantitiesInBasket = basketContents.stream()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        List<Item> items = itemRepository.findAllByNameInIgnoreCase(new ArrayList<>(itemsAndQuantitiesInBasket.keySet()));
        if(items.size() != itemsAndQuantitiesInBasket.size()) {
            List<String> foundItems = items.stream().map(item -> item.getName().toLowerCase()).toList();
            throw new SupermarketException(String.format("Item(s) not found: %s", itemsAndQuantitiesInBasket.keySet().stream()
                    .filter(item -> !foundItems.contains(item.toLowerCase()))
                    .collect(Collectors.joining(", "))));
        }
        return itemsAndQuantitiesInBasket.entrySet().stream().map(itemAndQuantity -> getPrice(itemAndQuantity, items)).reduce(0.0, Double::sum);
    }

    private Double getPrice(Map.Entry<String, Long> itemAndQuantity, List<Item> items) {
        Map<String, Item> mappedItems = items.stream().collect(Collectors.toUnmodifiableMap(item -> item.getName().toLowerCase(), Function.identity()));
        Item item = mappedItems.get(itemAndQuantity.getKey().trim().toLowerCase());
        return (item.getOffer() != null && isOfferValidToday(item.getOffer())) ?
                (itemAndQuantity.getValue() / item.getOffer().getQuantity()) * item.getOffer().getPrice() +
                itemAndQuantity.getValue() % item.getOffer().getQuantity() * item.getPrice()
                : itemAndQuantity.getValue() * item.getPrice();
    }

    private boolean isOfferValidToday(Offer offer) {
        return !LocalDate.now().isAfter(offer.getEndDate())
                && !LocalDate.now().isBefore(offer.getStartDate());
    }
}
