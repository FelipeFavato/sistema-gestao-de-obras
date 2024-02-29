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

import com.gobra.sistemagestaodeobras.dto.MarcaRequestDTO;
import com.gobra.sistemagestaodeobras.dto.MarcaResponseDTO;
import com.gobra.sistemagestaodeobras.model.Marca;
import com.gobra.sistemagestaodeobras.service.MarcaService;


@RestController
@RequestMapping("/api/marca")
public class MarcaController {

  @Autowired
  private MarcaService marcaService;


  @GetMapping
  public List<MarcaResponseDTO> getAll () {
    return marcaService.getAllMarca();
  }

  @PostMapping
  public ResponseEntity<?> saveOne (@RequestBody MarcaRequestDTO data) {
    return marcaService.saveMarca(data);
  }

  @PutMapping
  @Transactional
  public ResponseEntity<Marca> updateOne (@RequestBody MarcaRequestDTO data) {
    return marcaService.updateMarca(data);
  }

  @DeleteMapping
  public ResponseEntity<?> deleteOne (@RequestBody Marca marca) {
    return marcaService.deleteMarca(marca);
  }

}
