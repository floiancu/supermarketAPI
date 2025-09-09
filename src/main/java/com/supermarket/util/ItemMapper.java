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

    @Mapping(target = "quantity", source = "itemRequest.offerQuantity")
    @Mapping(target = "price", source = "itemRequest.offerPrice")
    @Mapping(target = "startDate", source = "itemRequest.offerStartDate")
    @Mapping(target = "endDate", source = "itemRequest.offerEndDate")
    Offer mapOffer(ItemRequest itemRequest);

    ItemResponse toDto(Item item);

    OfferResponse toDto(Offer offer);
}
