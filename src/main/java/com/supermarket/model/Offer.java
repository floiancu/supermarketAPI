package com.supermarket.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "Offers")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Offer {
        @Id
        @GeneratedValue(strategy = GenerationType.UUID)
        UUID  id;
        Double price;
        int quantity;
        LocalDate startDate;
        LocalDate endDate;

        public Offer(Double price, int quantity, LocalDate startDate, LocalDate endDate) {
                this.price = price;
                this.quantity = quantity;
                this.startDate = startDate;
                this.endDate = endDate;
        }
}
