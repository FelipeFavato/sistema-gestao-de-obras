package com.gobra.sistemagestaodeobras.dto;

import com.gobra.sistemagestaodeobras.model.Obra;
import com.gobra.sistemagestaodeobras.model.ObraArquivo;

public record ObraArquivoResponseDTO (

  Integer codigo,

  byte[] conteudoArquivo,

  String descricao,

  String nomeArquivo,

  String hashArquivo,

  Obra idObra

) {

  public ObraArquivoResponseDTO(ObraArquivo obraArquivoModel) {
    this(
      obraArquivoModel.getCodigo(),
      obraArquivoModel.getConteudoArquivo(),
      obraArquivoModel.getDescricao(),
      obraArquivoModel.getNomeArquivo(),
      obraArquivoModel.getHashArquivo(),
      obraArquivoModel.getIdObra()
    );
  }

}
