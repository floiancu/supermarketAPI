package com.supermarket.repository;

import com.supermarket.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ItemRepository extends JpaRepository<Item, UUID> {

    List<Item> findAllByNameInIgnoreCase(final List<String> itemNames);

    Item findByNameIgnoreCase(final String name);
}
