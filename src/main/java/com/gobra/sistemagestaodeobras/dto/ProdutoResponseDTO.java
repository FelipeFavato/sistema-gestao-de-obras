package com.gobra.sistemagestaodeobras.dto;

import com.gobra.sistemagestaodeobras.model.Produto;
import com.gobra.sistemagestaodeobras.utils.TipoProdutoEnum;

public record ProdutoResponseDTO (

  Integer codigo,

  String nome,

  TipoProdutoEnum tipoProduto

) {

  public ProdutoResponseDTO(Produto produtoModel) {
    this(
      produtoModel.getCodigo(),
      produtoModel.getNome(),
      produtoModel.getTipoProduto()
    );
  }
}
