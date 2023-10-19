package com.gobra.sistemagestaodeobras.dto;

import java.util.List;

import com.gobra.sistemagestaodeobras.model.Compra;
import com.gobra.sistemagestaodeobras.utils.TipoFornecedorEnum;

public record FornecedorRequestDTO (

  Integer codigo,

  String nome,

  String telefone,

  String endereco,

  String email,

  TipoFornecedorEnum tipoFornecedor,

  List<Compra> compras

) { }
