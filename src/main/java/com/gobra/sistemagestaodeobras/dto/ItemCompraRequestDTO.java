package com.gobra.sistemagestaodeobras.dto;

import com.gobra.sistemagestaodeobras.model.Compra;
import com.gobra.sistemagestaodeobras.model.LocalUso;
import com.gobra.sistemagestaodeobras.model.Produto;
import com.gobra.sistemagestaodeobras.model.UnidadeMedida;

public record ItemCompraRequestDTO (

  Integer codigo,

  Compra compra,

  Produto produto,

  LocalUso localUso,

  UnidadeMedida unidadeMedida,

  int quantidade,

  double valorUnitario,

  double valorTotal

) { }
