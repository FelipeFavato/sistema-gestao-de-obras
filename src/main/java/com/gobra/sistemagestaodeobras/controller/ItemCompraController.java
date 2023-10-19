package com.gobra.sistemagestaodeobras.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gobra.sistemagestaodeobras.dto.ItemCompraRequestDTO;
import com.gobra.sistemagestaodeobras.dto.ItemCompraResponseDTO;
import com.gobra.sistemagestaodeobras.model.ItemCompra;
import com.gobra.sistemagestaodeobras.repository.ItemCompraRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

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

  @PutMapping
  @Transactional
  public ResponseEntity<ItemCompra> updateItemCompra(@RequestBody ItemCompraRequestDTO data) {
    Optional<ItemCompra> optionalItemCompra = repository.findById(data.codigo());

    if (optionalItemCompra.isPresent()) {
      ItemCompra itemCompra = optionalItemCompra.get();
      itemCompra.setCompra(data.compra());
      itemCompra.setProduto(data.produto());
      itemCompra.setLocalUso(data.localUso());
      itemCompra.setUnidadeMedida(data.unidadeMedida());
      itemCompra.setQuantidade(data.quantidade());
      itemCompra.setValorUnitario(data.valorUnitario());
      itemCompra.setValorTotal(data.valorTotal());

      return ResponseEntity.ok(itemCompra);
    } else {
      throw new EntityNotFoundException();
    }
  }

  @DeleteMapping
  public void deletaItemCompra(@RequestBody ItemCompra itemCompra) {
    repository.delete(itemCompra);
  }
}
