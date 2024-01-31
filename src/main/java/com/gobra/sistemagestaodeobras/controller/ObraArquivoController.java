package com.gobra.sistemagestaodeobras.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.gobra.sistemagestaodeobras.dto.ObraArquivoResponseDTO;
import com.gobra.sistemagestaodeobras.model.ObraArquivo;
import com.gobra.sistemagestaodeobras.service.ObraArquivoService;



@RestController
@RequestMapping("/api/obra-arquivos")
public class ObraArquivoController {

  @Autowired
  private ObraArquivoService service;

  // Faz o UPLOAD de um ÚNICO arquivo.
  @PostMapping(consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
  public ResponseEntity<Object> uploadFile(@RequestPart MultipartFile file,
  @RequestPart String descricao, @RequestPart String nomeArquivo, Integer idObra) throws Exception {
    service.saveAttachment(file, descricao, nomeArquivo, idObra);

    return ResponseEntity.ok().build();
  }

  // Recupera todos os arquivos.
  @GetMapping
  public List<ObraArquivoResponseDTO> getAll() {
    return service.getAllFiles();
  }

  // Exclui um ÚNICO arquivo.
  @DeleteMapping
  public void deletaObraArquivo (@RequestBody ObraArquivo arquivo) {
    service.deletaObraArquivo(arquivo);
  }

}
