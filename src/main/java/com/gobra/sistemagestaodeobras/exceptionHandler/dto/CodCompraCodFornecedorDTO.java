package com.gobra.sistemagestaodeobras.exceptionHandler.dto;

import com.gobra.sistemagestaodeobras.exceptionHandler.projection.CodCompraCodFornecedorProjection;

public class CodCompraCodFornecedorDTO {
  
  Integer codigoCompra;

  Integer codigoFornecedor;

  public CodCompraCodFornecedorDTO() {

  }

  public CodCompraCodFornecedorDTO(Integer codCom, Integer codForn) {
    this.codigoCompra = codCom;
    this.codigoFornecedor = codForn;
  }

  public CodCompraCodFornecedorDTO(CodCompraCodFornecedorProjection projection) {
    codigoCompra = projection.getCodigoCompra();
    codigoFornecedor = projection.getCodigoFornecedor();
  }

  // Getters e setters:
  public Integer getCodigoCompra () {
    return codigoCompra;
  }

  public void setCodigoCompra (Integer cod) {
    this.codigoCompra = cod;
  }

  public Integer getCodigoFornecedor () {
    return codigoFornecedor;
  }

  public void setCodigoFornecedor (Integer cod) {
    this.codigoFornecedor = cod;
  }
}
