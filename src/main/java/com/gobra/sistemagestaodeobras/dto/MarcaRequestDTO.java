package com.gobra.sistemagestaodeobras.dto;

import java.util.Date;
import java.util.List;

import com.gobra.sistemagestaodeobras.model.Produto;

public record MarcaRequestDTO (

  Integer codigo,

  String nome,

  Date dataDesativacao,

  List<Produto> produtos

) { }
