package com.supermarket.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "Offers")
@Data
public class Offer {
        @Id
        @GeneratedValue(strategy = GenerationType.UUID)
        UUID  id;
        Double price;
        int quantity;
        LocalDate startDate;
        LocalDate endDate;
}
