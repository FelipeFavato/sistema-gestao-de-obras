package com.gobra.sistemagestaodeobras.dto;

import com.gobra.sistemagestaodeobras.model.Obra;

public record PrevisaoCompraRequestDTO (

  Integer codigo,

  Obra codigoObra,

  String descricaoProdutoServico,

  Integer quantidade,

  double valorUnitario,

  double valorTotalPrevisto,

  boolean compraPrevistaRealizada

) { }
