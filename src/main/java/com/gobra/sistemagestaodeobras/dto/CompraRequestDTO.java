package com.gobra.sistemagestaodeobras.dto;

import java.util.Date;
import java.util.List;

import com.gobra.sistemagestaodeobras.model.Fornecedor;
import com.gobra.sistemagestaodeobras.model.ItemCompra;
import com.gobra.sistemagestaodeobras.model.Obra;
import com.gobra.sistemagestaodeobras.model.Socio;

public record CompraRequestDTO (

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

) { }
