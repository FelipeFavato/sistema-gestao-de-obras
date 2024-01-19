package com.gobra.sistemagestaodeobras.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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



@RestController
@RequestMapping("/api/obra-arquivos")
public class ObraArquivoController {

  @Autowired
  private ObraArquivoService service;

   // for uploading the SINGLE file to the database
  @PostMapping()
  public ResponseEntity<ObraArquivo> uploadFile(@RequestParam("file") MultipartFile file, @RequestBody ObraArquivoRequestDTO data) throws Exception {

    ObraArquivo attachment = null;
    String downloadURl = "";
    attachment = service.saveArquivo(file, data.descricao(), data.nomeArquivo(), data.idObra());
    downloadURl = ServletUriComponentsBuilder.fromCurrentContextPath()
      .path("/download/")
      .path(attachment.getCodigo().toString())
      .toUriString();

    return ResponseEntity.ok(attachment);
  }

  //for uploading the SINGLE file to the File System
  // @PostMapping("/single/file")
  // public ResponseEntity<ResponseClass> handleFileUpload(@RequestParam("file") MultipartFile file) {
  //   String fileName = file.getOriginalFilename();
  //   try {
  //     file.transferTo(new File("D:\\Folder\\" + fileName));
  //     String downloadUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
  //       .path("/download/")
  //       .path(fileName)
  //       .toUriString();
  //     ResponseClass response = new ResponseClass(fileName,
  //       downloadUrl,
  //       file.getContentType(),
  //       file.getSize());
  //     return ResponseEntity.ok(response);
  //    } catch (Exception e) {
  //       return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
  //      }
  //    }

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
