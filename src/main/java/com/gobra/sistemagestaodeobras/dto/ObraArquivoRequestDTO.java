package com.gobra.sistemagestaodeobras.dto;


import com.gobra.sistemagestaodeobras.model.Obra;

public record ObraArquivoRequestDTO (

  Integer codigo,

  Byte[] conteudoArquivo,

  String descricao,

  String nomeArquivo,

  String hashArquivo,

  Obra idObra

) { }
