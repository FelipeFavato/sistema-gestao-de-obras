package com.gobra.sistemagestaodeobras.dto;


import org.springframework.web.multipart.MultipartFile;

import com.gobra.sistemagestaodeobras.model.Obra;

public record ObraArquivoRequestDTO (

  Integer codigo,

  MultipartFile conteudoArquivo,

  String descricao,

  String nomeArquivo,

  String hashArquivo,

  Obra idObra

) { }
