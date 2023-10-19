package com.gobra.sistemagestaodeobras.dto;

import java.util.List;

import com.gobra.sistemagestaodeobras.model.ItemCompra;

public record UnidadeMedidaRequestDTO (

  Integer codigo,

  String unidade,

  List<ItemCompra> itensCompra

) { }
