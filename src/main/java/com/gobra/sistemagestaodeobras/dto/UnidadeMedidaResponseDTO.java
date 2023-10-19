package com.gobra.sistemagestaodeobras.dto;

import java.util.List;

import com.gobra.sistemagestaodeobras.model.ItemCompra;
import com.gobra.sistemagestaodeobras.model.UnidadeMedida;

public record UnidadeMedidaResponseDTO (
  
  Integer codigo,

  String unidade,

  List<ItemCompra> itensCompra

) {

  public UnidadeMedidaResponseDTO(UnidadeMedida unidadeMedidaModel) {
    this(
      unidadeMedidaModel.getCodigo(),
      unidadeMedidaModel.getUnidade(),
      unidadeMedidaModel.getItensCompra()
      );
  }

}
