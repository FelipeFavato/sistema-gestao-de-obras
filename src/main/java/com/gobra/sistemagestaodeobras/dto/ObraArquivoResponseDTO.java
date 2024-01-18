package com.gobra.sistemagestaodeobras.dto;

import com.gobra.sistemagestaodeobras.model.Obra;
import com.gobra.sistemagestaodeobras.model.ObraArquivo;

public record ObraArquivoResponseDTO (

  Integer codigo,

  String conteudoImagem,

  String descricao,

  String nome,

  Obra idObra

) {

  public ObraArquivoResponseDTO(ObraArquivo obraArquivoModel) {
    this(
      obraArquivoModel.getCodigo(),
      obraArquivoModel.getConteudoImagem(),
      obraArquivoModel.getDescricao(),
      obraArquivoModel.getNome(),
      obraArquivoModel.getIdObra()
    );
  }

}
