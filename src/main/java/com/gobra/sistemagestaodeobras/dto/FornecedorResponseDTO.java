package com.gobra.sistemagestaodeobras.dto;

import java.util.List;

import com.gobra.sistemagestaodeobras.model.Compra;
import com.gobra.sistemagestaodeobras.model.Fornecedor;
import com.gobra.sistemagestaodeobras.utils.TipoFornecedorEnum;

public record FornecedorResponseDTO (
  
  Integer codigo,

  String nome,

  String telefone,

  String endereco,

  String email,

  TipoFornecedorEnum tipoFornecedor,

  List<Compra> compras

) {

  public FornecedorResponseDTO(Fornecedor fornecedorModel) {
    this(
      fornecedorModel.getCodigo(),
      fornecedorModel.getNome(),
      fornecedorModel.getTelefone(),
      fornecedorModel.getEndereco(),
      fornecedorModel.getEmail(),
      fornecedorModel.getTipoFornecedor(),
      fornecedorModel.getCompras()
    );
  }

}
