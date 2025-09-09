package com.supermarket.util;

import com.supermarket.dto.ItemRequest;
import com.supermarket.dto.ItemResponse;
import com.supermarket.dto.OfferResponse;
import com.supermarket.model.Item;
import com.supermarket.model.Offer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ItemMapper {

    @Mapping(target = "name", source = "itemRequest.name")
    @Mapping(target = "price", source = "itemRequest.price")
    @Mapping(target = "offer", expression = "java(mapOffer(itemRequest))")
    Item map(ItemRequest itemRequest);

    default Offer mapOffer(ItemRequest itemRequest){
        if(itemRequest.offerPrice() == null || itemRequest.offerQuantity() == null ||
                itemRequest.offerStartDate() == null || itemRequest.offerEndDate() == null) {
            return null;
        }
        return new Offer(itemRequest.offerPrice(), itemRequest.offerQuantity(), itemRequest.offerStartDate(), itemRequest.offerEndDate());
    }

    ItemResponse toDto(Item item);

    OfferResponse toDto(Offer offer);
}
