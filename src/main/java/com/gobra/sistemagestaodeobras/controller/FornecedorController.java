package com.gobra.sistemagestaodeobras.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gobra.sistemagestaodeobras.dto.FornecedorRequestDTO;
import com.gobra.sistemagestaodeobras.dto.FornecedorResponseDTO;
import com.gobra.sistemagestaodeobras.model.Fornecedor;
import com.gobra.sistemagestaodeobras.service.FornecedorService;



@RestController
@RequestMapping("/api/fornecedor")
public class FornecedorController {

  @Autowired
  private FornecedorService fornecedorService;
  
  
  @GetMapping
  public List<FornecedorResponseDTO> getAll () {
    return fornecedorService.getAllFornecedores();
  }
  
  @PostMapping
  public ResponseEntity<?> salva (@RequestBody FornecedorRequestDTO data) {
    return fornecedorService.saveFornecedor(data);
  }

  @PutMapping
  @Transactional
  public ResponseEntity<Fornecedor> update (@RequestBody FornecedorRequestDTO data) {
    return fornecedorService.updateFornecedor(data);
  }

  @DeleteMapping
  public ResponseEntity<?> deleta (@RequestBody Fornecedor fornecedor) {
    return fornecedorService.deletaFornecedor(fornecedor);
  }
}
