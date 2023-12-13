package com.gobra.sistemagestaodeobras.dto;

import java.util.Date;
import java.util.List;
import java.util.Set;

import com.gobra.sistemagestaodeobras.model.Compra;
import com.gobra.sistemagestaodeobras.model.Socio;

public record ObraRequestDTO (

  Integer codigo,

  String nome,

  String endereco,

  Date dataInicio,

  Date dataPrevistaFim,

  Date dataRealFim,

  Integer custoMaoDeObra,

  Integer custoPrevisto,

  List<Compra> compras,

  Set<Socio> socios

) { }
