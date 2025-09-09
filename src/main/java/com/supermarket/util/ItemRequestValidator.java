package com.supermarket.util;


import com.supermarket.dto.ItemRequest;
import com.supermarket.exception.SupermarketException;

import java.util.List;
import java.util.stream.Collectors;

public class ItemRequestValidator {

    static final String OFFER_ERROR_MESSAGE = ": invalid offer fields.";

    public static void validate(List<ItemRequest> itemRequestList) {
        String errorItemsList = itemRequestList.stream()
                .filter(itemRequest -> !validateItemRequest(itemRequest))
                .map(ItemRequest::name)
                .collect(Collectors.joining(", "));
        if (!errorItemsList.isEmpty()) {
            throw new SupermarketException(errorItemsList + OFFER_ERROR_MESSAGE);
        }
    }

    private static boolean validateItemRequest(ItemRequest itemRequest) {
        return (itemRequest.offerPrice() == null && itemRequest.offerQuantity() == null &&
                itemRequest.offerStartDate() == null && itemRequest.offerEndDate() == null) ||
                (itemRequest.offerPrice() != null && itemRequest.offerQuantity() != null &&
                itemRequest.offerStartDate() != null && itemRequest.offerEndDate() != null &&
                itemRequest.offerPrice() > 0 && itemRequest.offerQuantity() > 1 &&
                itemRequest.offerEndDate().isAfter(itemRequest.offerStartDate()));
    }
}
