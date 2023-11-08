package com.gobra.sistemagestaodeobras.dto;

import java.util.Date;
import java.util.List;

import com.gobra.sistemagestaodeobras.model.Marca;
import com.gobra.sistemagestaodeobras.model.Produto;

public record MarcaResponseDTO (

  Integer codigo,

  String nome,

  Date dataDesativacao,

  List<Produto> produtos

) {
  
  public MarcaResponseDTO(Marca marcaModel) {
    this(
      marcaModel.getCodigo(),
      marcaModel.getNome(),
      marcaModel.getDataDesativacao(),
      marcaModel.getProdutos()
    );
  }
}
