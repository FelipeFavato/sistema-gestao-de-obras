package com.gobra.sistemagestaodeobras.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gobra.sistemagestaodeobras.dto.ItemCompraRequestDTO;
import com.gobra.sistemagestaodeobras.dto.ItemCompraResponseDTO;
import com.gobra.sistemagestaodeobras.model.ItemCompra;
import com.gobra.sistemagestaodeobras.repository.ItemCompraRepository;

@RestController
@RequestMapping("/api/itemcompra")
public class ItemCompraController {
  
  @Autowired
  private ItemCompraRepository repository;

  @PostMapping
  public void saveItemCompra(@RequestBody ItemCompraRequestDTO data) {
    ItemCompra itemCompraData = new ItemCompra(data);
    repository.save(itemCompraData);
  }

  @GetMapping
  public List<ItemCompraResponseDTO> getAll() {
    List<ItemCompraResponseDTO> itensCompraList = repository.findAll().stream().map(ItemCompraResponseDTO::new).toList();
    return itensCompraList;
  }
}
