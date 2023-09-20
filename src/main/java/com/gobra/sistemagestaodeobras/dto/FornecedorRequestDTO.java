package com.gobra.sistemagestaodeobras.dto;

import com.gobra.sistemagestaodeobras.utils.TipoFornecedorEnum;

public record FornecedorRequestDTO (

  Integer codigo,

  String nome,

  String telefone,

  String endereco,

  TipoFornecedorEnum tipoFornecedor

) { }
