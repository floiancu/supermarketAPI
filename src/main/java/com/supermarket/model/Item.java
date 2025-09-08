package com.supermarket.model;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
public record Item(
        @Id
        int id,
        String name,
        Double price,
        @OneToOne
        @JoinColumn(name = "offer_id", referencedColumnName = "id")
        Offer offer) {

}
