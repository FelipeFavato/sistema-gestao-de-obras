package com.gobra.sistemagestaodeobras.dto;

import com.gobra.sistemagestaodeobras.model.Fornecedor;

public record FornecedorResponseDTO (
  
  Integer codigo,

  String nome,

  String telefone,

  String endereco,

  String tipoFornecedor

) {

  public FornecedorResponseDTO(Fornecedor fornecedorModel) {
    this(
      fornecedorModel.getCodigo(),
      fornecedorModel.getNome(),
      fornecedorModel.getTelefone(),
      fornecedorModel.getEndereco(),
      fornecedorModel.getTipoFornecedor()
    );
  }

}
