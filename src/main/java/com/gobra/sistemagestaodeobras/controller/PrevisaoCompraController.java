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

import com.gobra.sistemagestaodeobras.dto.PrevisaoCompraRequestDTO;
import com.gobra.sistemagestaodeobras.dto.PrevisaoCompraResponseDTO;
import com.gobra.sistemagestaodeobras.model.PrevisaoCompra;
import com.gobra.sistemagestaodeobras.service.PrevisaoCompraService;

@RestController
@RequestMapping("/api/previsao-compra")
public class PrevisaoCompraController {
  
  @Autowired
  private PrevisaoCompraService previsaoCompraService;


  @GetMapping
  public List<PrevisaoCompraResponseDTO> getAll () {
    return previsaoCompraService.getAllPrevisoes();
  }

  @PostMapping
  public ResponseEntity<PrevisaoCompra> saveOne (@RequestBody PrevisaoCompraRequestDTO data) {
    return previsaoCompraService.savePrevisaoCompra(data);
  }

  @PutMapping
  @Transactional
  public ResponseEntity<PrevisaoCompra> updateOne (@RequestBody PrevisaoCompraRequestDTO data) {
    return previsaoCompraService.updatePrevisaoCompra(data);
  }

  @DeleteMapping
  public ResponseEntity<?> deleteOne (@RequestBody PrevisaoCompra data) {
    return previsaoCompraService.deletaPrevisaoCompra(data);
  }

}
