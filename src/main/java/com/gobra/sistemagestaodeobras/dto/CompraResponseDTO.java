package com.gobra.sistemagestaodeobras.dto;

import java.util.Date;
import java.util.List;

import com.gobra.sistemagestaodeobras.model.Compra;
import com.gobra.sistemagestaodeobras.model.Fornecedor;
import com.gobra.sistemagestaodeobras.model.ItemCompra;
import com.gobra.sistemagestaodeobras.model.Obra;
import com.gobra.sistemagestaodeobras.model.Socio;

public record CompraResponseDTO (

  Integer codigo,

  Obra obra,

  Fornecedor fornecedor,

  Socio socio,

  Date dataCompra,

  Date dataEntrega,

  Date dataPagamento,

  Date dataVencimento,

  double valorOriginal,

  double valorDesconto,

  double valorFinal,

  List<ItemCompra> itensCompra

) {
  
  public CompraResponseDTO(Compra compraModel) {
    this(
      compraModel.getCodigo(),
      compraModel.getObra(),
      compraModel.getFornecedor(),
      compraModel.getSocio(),
      compraModel.getDataCompra(),
      compraModel.getDataEntrega(),
      compraModel.getDataPagamento(),
      compraModel.getDataVencimento(),
      compraModel.getValorOriginal(),
      compraModel.getValorDesconto(),
      compraModel.getValorFinal(),
      compraModel.getItensCompra()
    );
  }

}
