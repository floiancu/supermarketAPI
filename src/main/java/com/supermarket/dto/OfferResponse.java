package com.supermarket.dto;

import java.time.LocalDate;
import java.util.UUID;

public record OfferResponse (UUID id, double price, int quantity, LocalDate startDate, LocalDate endDate) {
}
