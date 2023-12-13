package com.gobra.sistemagestaodeobras.dto;

import java.util.List;
import java.util.Date;

import com.gobra.sistemagestaodeobras.model.Compra;
import com.gobra.sistemagestaodeobras.model.Obra;

public record SocioRequestDTO (

  Integer codigo,

  String nome,

  Date dataDesativacao,

  Long telegramID,

  List<Compra> compras,

  List<Obra> obras

) { }
