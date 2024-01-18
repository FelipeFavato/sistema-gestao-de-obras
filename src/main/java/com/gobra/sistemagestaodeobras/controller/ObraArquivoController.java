package com.gobra.sistemagestaodeobras.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gobra.sistemagestaodeobras.dto.ObraArquivoRequestDTO;
import com.gobra.sistemagestaodeobras.dto.ObraArquivoResponseDTO;
import com.gobra.sistemagestaodeobras.service.ObraArquivoService;



@RestController
@RequestMapping("/api/obra-arquivos")
public class ObraArquivoController {

  @Autowired
  private ObraArquivoService service;

  @PostMapping
  public void saveArquivo(@RequestBody ObraArquivoRequestDTO data) {
    service.saveArquivoToDB(data.conteudoImagem(), data.descricao(), data.nome(), data.idObra());
  }

  @GetMapping
  public List<ObraArquivoResponseDTO> getAll() {
    return service.findAllArquivos();
  }


}
