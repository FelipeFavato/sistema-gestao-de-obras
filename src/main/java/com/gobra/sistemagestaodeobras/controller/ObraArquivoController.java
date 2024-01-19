package com.gobra.sistemagestaodeobras.controller;


import java.io.File;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.gobra.sistemagestaodeobras.dto.ObraArquivoRequestDTO;
import com.gobra.sistemagestaodeobras.dto.ObraArquivoResponseDTO;
import com.gobra.sistemagestaodeobras.model.ObraArquivo;
import com.gobra.sistemagestaodeobras.service.ObraArquivoService;
import com.gobra.sistemagestaodeobras.service.ResponseClass;



@RestController
@RequestMapping("/api/obra-arquivos")
public class ObraArquivoController {

  @Autowired
  private ObraArquivoService service;

   // for uploading the SINGLE file to the database
  // @PostMapping()
  // public ResponseEntity<ObraArquivo> uploadFile(@RequestParam("file") MultipartFile file, @RequestBody ObraArquivoRequestDTO data) throws Exception {

  //   ObraArquivo attachment = null;
  //   String downloadURl = "";
  //   attachment = service.saveArquivo(file, data.descricao(), data.nomeArquivo(), data.idObra());
  //   downloadURl = ServletUriComponentsBuilder.fromCurrentContextPath()
  //     .path("/download/")
  //     .path(attachment.getCodigo().toString())
  //     .toUriString();

  //   return ResponseEntity.ok(attachment);
  // }

  // for uploading the SINGLE file to the File System
  @PostMapping("/single/base")
  public ResponseClass uploadFile(@RequestParam("file") MultipartFile file) throws Exception {

    ObraArquivo attachment = null;
    String downloadURl = "";
    attachment = service.saveAttachment(file);
    downloadURl = ServletUriComponentsBuilder.fromCurrentContextPath()
      .path("/download/")
      .path(attachment.getCodigo().toString(0))
      .toUriString();

    return new ResponseClass(attachment.getNomeArquivo(),
      downloadURl,
      file.getContentType(),
      file.getSize());
   }

  // @PostMapping
  // public void saveArquivo(@RequestBody ObraArquivoRequestDTO data) {
  //   // MultipartFile file, String descricao, String nome, Obra idObra
  //   service.saveArquivo(data.conteudoArquivo(), data.descricao(), data.nomeArquivo(), data.idObra());
  // }

  @GetMapping
  public List<ObraArquivoResponseDTO> getAll() {
    return service.getAllFiles();
  }

}
