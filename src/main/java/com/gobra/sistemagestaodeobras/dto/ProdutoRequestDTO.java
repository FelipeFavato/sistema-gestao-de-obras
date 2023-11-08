package com.gobra.sistemagestaodeobras.dto;

import java.util.List;

import com.gobra.sistemagestaodeobras.model.ItemCompra;
import com.gobra.sistemagestaodeobras.model.Marca;
import com.gobra.sistemagestaodeobras.utils.TipoProdutoEnum;

public record ProdutoRequestDTO (

  Integer codigo,

  String nome,

  TipoProdutoEnum tipoProduto,

  List<ItemCompra> itensCompra,

  Marca marca

) { }
