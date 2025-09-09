package com.supermarket.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "Items")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Item{
        @Id
        @GeneratedValue(strategy = GenerationType.UUID)
        UUID id;
        String name;
        Double price;
        @OneToOne(cascade = CascadeType.ALL)
        @JoinColumn(name = "offer", referencedColumnName = "id")
        Offer offer;
}
