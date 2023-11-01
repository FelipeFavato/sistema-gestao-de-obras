package com.gobra.sistemagestaodeobras.dto;

import java.util.List;
import java.util.Set;

import com.gobra.sistemagestaodeobras.model.Compra;
import com.gobra.sistemagestaodeobras.model.Obra;

public record SocioRequestDTO (

  Integer codigo,

  String nome,

  List<Compra> compras,

  Set<Obra> obras

) { }
