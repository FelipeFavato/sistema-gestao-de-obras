package com.gobra.sistemagestaodeobras.dto;

public record FornecedorRequestDTO (

  Integer codigo,

  String nome,

  String telefone,

  String endereco,

  String tipoFornecedor
) { }
