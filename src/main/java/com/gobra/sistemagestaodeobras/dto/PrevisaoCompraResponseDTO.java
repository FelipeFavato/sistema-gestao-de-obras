package com.gobra.sistemagestaodeobras.dto;

import com.gobra.sistemagestaodeobras.model.Obra;
import com.gobra.sistemagestaodeobras.model.PrevisaoCompra;

public record PrevisaoCompraResponseDTO (
  
  Integer codigo,

  Obra codigoObra,

  String descricaoProdutoServico,

  Integer quantidade,

  double valorUnitario,

  double valorTotalPrevisto,
  
  boolean compraPrevistaRealizada

) {

  public PrevisaoCompraResponseDTO(PrevisaoCompra previsaoCompraModel) {
    this(
      previsaoCompraModel.getCodigo(),
      previsaoCompraModel.getCodigoObra(),
      previsaoCompraModel.getDescricaoProdutoServico(),
      previsaoCompraModel.getQuantidade(),
      previsaoCompraModel.getValorUnitario(),
      previsaoCompraModel.getValorUnitario(),
      previsaoCompraModel.isCompraPrevistaRealizada()
    );
  }

}
