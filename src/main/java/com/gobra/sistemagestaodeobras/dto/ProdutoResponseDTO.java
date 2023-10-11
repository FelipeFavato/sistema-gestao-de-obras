package com.gobra.sistemagestaodeobras.dto;

import java.util.List;

import com.gobra.sistemagestaodeobras.model.ItemCompra;
import com.gobra.sistemagestaodeobras.model.Produto;
import com.gobra.sistemagestaodeobras.utils.TipoProdutoEnum;

public record ProdutoResponseDTO (

  Integer codigo,

  String nome,

  TipoProdutoEnum tipoProduto,

  List<ItemCompra> itensCompra

) {

  public ProdutoResponseDTO(Produto produtoModel) {
    this(
      produtoModel.getCodigo(),
      produtoModel.getNome(),
      produtoModel.getTipoProduto(),
      produtoModel.getItensCompra()
    );
  }
}
