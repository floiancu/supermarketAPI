package com.supermarket.controller;

import com.supermarket.dto.AddItemResponse;
import com.supermarket.dto.ItemRequest;
import com.supermarket.dto.ItemResponse;
import com.supermarket.service.AdminService;
import com.supermarket.util.ItemMapper;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;
    private final ItemMapper itemMapper;

    @GetMapping("/item/{name}")
    ResponseEntity<ItemResponse> getItem(@PathVariable String name) {
        return ResponseEntity.ok(itemMapper.toDto(adminService.getItem(name)));
    }

    @PostMapping("/item")
    ResponseEntity<AddItemResponse> addItems(@Valid @RequestBody List<ItemRequest> itemRequestList){
        return ResponseEntity.ok(new AddItemResponse(this.adminService.addItems(itemRequestList).size()));
    }

    @PutMapping("/item/{id}")
    ResponseEntity<Void> updateItem(@PathVariable @NotNull UUID id, @Valid @RequestBody ItemRequest itemRequest) {
        this.adminService.updateItem(id, itemRequest);
        return ResponseEntity.ok().build();
    }
}
