package com.supermarket.dto;

import java.time.LocalDate;

public record ItemRequest (String name, double price, int offerQuantity, double offerPrice, LocalDate offerStartDate, LocalDate offerEndDate){
}
