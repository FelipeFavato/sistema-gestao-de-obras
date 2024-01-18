package com.gobra.sistemagestaodeobras.dto;

import org.springframework.web.multipart.MultipartFile;

import com.gobra.sistemagestaodeobras.model.Obra;

public record ObraArquivoRequestDTO (

  Integer codigo,

  MultipartFile conteudoImagem,

  String descricao,

  String nome,

  Obra idObra

) { }
