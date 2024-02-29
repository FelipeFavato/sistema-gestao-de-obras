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

import com.gobra.sistemagestaodeobras.dto.UnidadeMedidaRequestDTO;
import com.gobra.sistemagestaodeobras.dto.UnidadeMedidaResponseDTO;
import com.gobra.sistemagestaodeobras.model.UnidadeMedida;
import com.gobra.sistemagestaodeobras.service.UnidadeMedidaService;


@RestController
@RequestMapping("/api/unidademedida")
public class UnidadeMedidaController {

  @Autowired
  private UnidadeMedidaService unidadeMedidaService;
  

  @GetMapping
  public List<UnidadeMedidaResponseDTO> getAll () {
    return unidadeMedidaService.getAllUnidades();
  }

  @PostMapping
  public ResponseEntity<?> saveOne (@RequestBody UnidadeMedidaRequestDTO data) {
    return unidadeMedidaService.saveUnidadeMedida(data);
  }

  @PutMapping
  @Transactional
  public ResponseEntity<UnidadeMedida> updateOne (@RequestBody UnidadeMedidaRequestDTO data) {
    return unidadeMedidaService.updateUnidadeMedida(data);
  }

  @DeleteMapping
  public ResponseEntity<?> deleteOne (@RequestBody UnidadeMedida unidadeMedida) {
    return unidadeMedidaService.deleteUnidade(unidadeMedida);
  }
}
