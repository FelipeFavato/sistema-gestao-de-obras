package com.gobra.sistemagestaodeobras.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gobra.sistemagestaodeobras.dto.SocioRequestDTO;
import com.gobra.sistemagestaodeobras.dto.SocioResponseDTO;
import com.gobra.sistemagestaodeobras.model.Socio;
import com.gobra.sistemagestaodeobras.repository.SocioRepository;





@RestController
@RequestMapping("/api/socio")
public class SocioController {

  @Autowired
  private SocioRepository repository;

  @PostMapping
  public void saveSocio(@RequestBody SocioRequestDTO data) {
    Socio socioData = new Socio(data);
    repository.save(socioData);
  }

  @GetMapping
  public List<SocioResponseDTO> getAll () {
    List<SocioResponseDTO> socioList = repository.findAll().stream().map(SocioResponseDTO::new).toList();
    return socioList;
  }

  @DeleteMapping
  public void deletaSocio (@RequestBody Socio socio) {
    repository.delete(socio);
  }
}
