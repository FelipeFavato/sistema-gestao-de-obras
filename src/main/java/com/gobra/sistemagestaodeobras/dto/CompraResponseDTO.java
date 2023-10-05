package com.gobra.sistemagestaodeobras.dto;

import java.util.Date;

import com.gobra.sistemagestaodeobras.model.Compra;
import com.gobra.sistemagestaodeobras.model.Fornecedor;
import com.gobra.sistemagestaodeobras.model.Obra;

public record CompraResponseDTO (

  Integer codigo,

  Obra obra,

  Fornecedor fornecedor,

  Date dataCompra,

  Date dataEntrega,

  Date dataPagamento,

  Date dataVencimento,

  double valorOriginal,

  double valorDesconto,

  double valorFinal

) {
  
  public CompraResponseDTO(Compra compraModel) {
    this(
      compraModel.getCodigo(),
      compraModel.getObra(),
      compraModel.getFornecedor(),
      compraModel.getDataCompra(),
      compraModel.getDataEntrega(),
      compraModel.getDataPagamento(),
      compraModel.getDataVencimento(),
      compraModel.getValorOriginal(),
      compraModel.getValorDesconto(),
      compraModel.getValorFinal()
    );
  }

}
