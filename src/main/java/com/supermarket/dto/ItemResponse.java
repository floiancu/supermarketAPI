package com.supermarket.dto;

import java.util.UUID;

public record ItemResponse (UUID id, String name, double price, OfferResponse offer){
}
