package com.gobra.sistemagestaodeobras.dto;

import com.gobra.sistemagestaodeobras.utils.TipoProdutoEnum;

public record ProdutoRequestDTO (

  Integer codigo,

  String nome,

  TipoProdutoEnum tipoProduto

) { }
