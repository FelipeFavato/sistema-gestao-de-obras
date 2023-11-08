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

import com.gobra.sistemagestaodeobras.dto.MarcaRequestDTO;
import com.gobra.sistemagestaodeobras.dto.MarcaResponseDTO;
import com.gobra.sistemagestaodeobras.model.Marca;
import com.gobra.sistemagestaodeobras.repository.MarcaRepository;

import jakarta.persistence.EntityNotFoundException;

@RestController
@RequestMapping("/api/marca")
public class MarcaController {
  
  @Autowired
  private MarcaRepository repository;

  @PostMapping
  public void saveMarca(@RequestBody MarcaRequestDTO data) {
    Marca marcaData = new Marca(data);
    repository.save(marcaData);
  }

  @PutMapping
  @Transactional
  public ResponseEntity<Marca> updateMarca(@RequestBody MarcaRequestDTO data) {
    Optional<Marca> optionalMarca = repository.findById(data.codigo());

    if (optionalMarca.isPresent()) {
      Marca marca = optionalMarca.get();
      marca.setNome(data.nome());
      marca.setDataDesativacao(data.dataDesativacao());

      return ResponseEntity.ok(marca);
    } else {
      throw new EntityNotFoundException();
    }
  }

  @GetMapping
  public List<MarcaResponseDTO> getAll () {
    List<MarcaResponseDTO> marcaList = repository.findAll().stream().map(MarcaResponseDTO::new).toList();
    return marcaList;
  }

  @DeleteMapping
  public void deletaMarca(@RequestBody Marca marca) {
    repository.delete(marca);
  }

}
