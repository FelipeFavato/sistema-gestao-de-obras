package com.gobra.sistemagestaodeobras.dto;

import com.gobra.sistemagestaodeobras.model.Fornecedor;
import com.gobra.sistemagestaodeobras.utils.TipoFornecedorEnum;

public record FornecedorResponseDTO (
  
  Integer codigo,

  String nome,

  String telefone,

  String endereco,

  TipoFornecedorEnum tipoFornecedor

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
