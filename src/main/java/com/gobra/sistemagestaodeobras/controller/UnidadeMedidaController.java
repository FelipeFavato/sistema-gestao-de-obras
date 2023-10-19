package com.gobra.sistemagestaodeobras.controller;

import java.util.List;
import java.util.Optional;

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
import com.gobra.sistemagestaodeobras.repository.UnidadeMedidaRepository;

import jakarta.persistence.EntityNotFoundException;

@RestController
@RequestMapping("/api/unidademedida")
public class UnidadeMedidaController {
  
  @Autowired
  private UnidadeMedidaRepository repository;

  @PostMapping
  public void saveUnidadeMedida(@RequestBody UnidadeMedidaRequestDTO data) {
    UnidadeMedida unidadeMedidaData = new UnidadeMedida(data);
    repository.save(unidadeMedidaData);
  }

  @GetMapping
  public List<UnidadeMedidaResponseDTO> getAll() {
    List<UnidadeMedidaResponseDTO> unidadeMedidaList = repository.findAll().stream().map(UnidadeMedidaResponseDTO::new).toList();
    return unidadeMedidaList;
  }

  @PutMapping
  @Transactional
  public ResponseEntity<UnidadeMedida> updateUnidadeMedida(@RequestBody UnidadeMedidaRequestDTO data) {
    Optional<UnidadeMedida> optionalUnidadeMedida = repository.findById(data.codigo());
  
    if (optionalUnidadeMedida.isPresent()) {
      UnidadeMedida unidadeMedida = optionalUnidadeMedida.get();
      unidadeMedida.setUnidade(data.unidade());

      return ResponseEntity.ok(unidadeMedida);
    } else {
      throw new EntityNotFoundException();
    }
  }

  @DeleteMapping
  public void deletaUnidadeMedida(@RequestBody UnidadeMedida unidadeMedida) {
    repository.delete(unidadeMedida);
  }
}
