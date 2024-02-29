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

import com.gobra.sistemagestaodeobras.dto.SocioRequestDTO;
import com.gobra.sistemagestaodeobras.dto.SocioResponseDTO;
import com.gobra.sistemagestaodeobras.model.Socio;
import com.gobra.sistemagestaodeobras.service.SocioService;






@RestController
@RequestMapping("/api/socio")
public class SocioController {

  @Autowired
  private SocioService socioService;


  @GetMapping
  public List<SocioResponseDTO> getAll () {
    return socioService.getAllSocios();
  }

  @PostMapping
  public ResponseEntity<?> saveOne (@RequestBody SocioRequestDTO data) {
    return socioService.saveSocio(data);
  }

  @PutMapping
  @Transactional
  public ResponseEntity<Socio> updateOne(@RequestBody SocioRequestDTO data) {
    return socioService.updateSocio(data);
  }

  @DeleteMapping
  public ResponseEntity<?> deletaSocio (@RequestBody Socio socio) {
    return socioService.deletaSocio(socio);
  }

}
