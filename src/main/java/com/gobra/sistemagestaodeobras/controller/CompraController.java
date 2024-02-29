package com.gobra.sistemagestaodeobras.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gobra.sistemagestaodeobras.dto.CompraRequestDTO;
import com.gobra.sistemagestaodeobras.dto.CompraResponseDTO;
import com.gobra.sistemagestaodeobras.model.Compra;
import com.gobra.sistemagestaodeobras.service.CompraService;


@RestController
@RequestMapping("/api/compra")
public class CompraController {

  // @Autowired
  // private CompraRepository compraRepository;

  @Autowired
  private CompraService compraService;

  @GetMapping
  public List<CompraResponseDTO> getAll () {
    return compraService.getAllCompras();
  }

  @PostMapping
  public ResponseEntity<Compra> saveOne (@RequestBody CompraRequestDTO data) {
    return compraService.saveCompra(data);
  }

  @PutMapping
  @Transactional
  public ResponseEntity<Compra> updateOne (@RequestBody CompraRequestDTO data) {
    return compraService.updateCompra(data);
  }

  @DeleteMapping
  public ResponseEntity<?> deleteOne (@RequestBody Compra compra) {
    return compraService.deletaCompra(compra);
  }
}
