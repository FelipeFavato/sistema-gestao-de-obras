package com.gobra.sistemagestaodeobras.dto;

import java.util.Date;

import com.gobra.sistemagestaodeobras.model.Fornecedor;
import com.gobra.sistemagestaodeobras.model.Obra;

public record CompraRequestDTO (

  Integer codigo,

  Obra obra,

  Fornecedor fornecedor,

  Date dataCompra,

  Date dataEntrega,

  Date dataPagamento,

  Date dataVencimento,

  Integer valorOriginal,

  Integer valorDesconto,

  Integer valorFinal

) { }
