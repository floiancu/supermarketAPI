package com.supermarket.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;

public record ItemRequest(@NotBlank String name, @NotNull @Positive(message = "Price value should be greater than zero.") Double price,
                          Integer offerQuantity, Double offerPrice, LocalDate offerStartDate, LocalDate offerEndDate) {
}
