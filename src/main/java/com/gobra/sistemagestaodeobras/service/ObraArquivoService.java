package com.gobra.sistemagestaodeobras.service;

import java.io.IOException;
import java.util.Base64;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.gobra.sistemagestaodeobras.dto.ObraArquivoResponseDTO;
import com.gobra.sistemagestaodeobras.model.Obra;
import com.gobra.sistemagestaodeobras.model.ObraArquivo;
import com.gobra.sistemagestaodeobras.repository.ObraArquivoRepository;


@Service
public class ObraArquivoService {

  @Autowired
  private ObraArquivoRepository obraArquivoRepository;

  // codigo | conteudoImagem | descricao | nome | idObra

  public void saveArquivoToDB (MultipartFile file, String descricao, String nome, Obra idObra) {
    ObraArquivo obraArquivo = new ObraArquivo();
    
    String fileName = StringUtils.cleanPath(file.getOriginalFilename());
    if (fileName.contains("..")) {
      System.out.println("Arquivo inválido.");
    }

    try {
      obraArquivo.setConteudoImagem(Base64.getEncoder().encodeToString(file.getBytes()));
    } catch (IOException e) {
      e.printStackTrace();
    }

    obraArquivo.setDescricao(descricao);
    obraArquivo.setNome(nome);
    obraArquivo.setIdObra(idObra);

    obraArquivoRepository.save(obraArquivo);
  }

  public List<ObraArquivoResponseDTO> findAllArquivos () {
    List<ObraArquivoResponseDTO> listaArquivos = obraArquivoRepository.findAll().stream().map(ObraArquivoResponseDTO::new).toList();
    return listaArquivos;
  }

}
