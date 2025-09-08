package com.supermarket.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Entity
@Table(name = "Items")
@Data
public class Item{
        @Id
        @GeneratedValue(strategy = GenerationType.UUID)
        UUID id;
        String name;
        Double price;
        @OneToOne
        @JoinColumn(name = "offer_id", referencedColumnName = "id")
        Offer offer;
}
