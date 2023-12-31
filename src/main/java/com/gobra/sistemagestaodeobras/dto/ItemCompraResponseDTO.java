package com.gobra.sistemagestaodeobras.dto;

import com.gobra.sistemagestaodeobras.model.Compra;
import com.gobra.sistemagestaodeobras.model.ItemCompra;
import com.gobra.sistemagestaodeobras.model.LocalUso;
import com.gobra.sistemagestaodeobras.model.Produto;
import com.gobra.sistemagestaodeobras.model.UnidadeMedida;

public record ItemCompraResponseDTO (

  Integer codigo,

  Compra compra,

  Produto produto,

  LocalUso localUso,

  UnidadeMedida unidadeMedida,

  int quantidade,

  double valorUnitario,

  double valorTotal

) {

  public ItemCompraResponseDTO(ItemCompra itemCompraModel) {
    this(
      itemCompraModel.getCodigo(),
      itemCompraModel.getCompra(),
      itemCompraModel.getProduto(),
      itemCompraModel.getLocalUso(),
      itemCompraModel.getUnidadeMedida(),
      itemCompraModel.getQuantidade(),
      itemCompraModel.getValorUnitario(),
      itemCompraModel.getValorTotal()
    );
  }

}
