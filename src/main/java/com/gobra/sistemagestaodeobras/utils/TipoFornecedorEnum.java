package com.gobra.sistemagestaodeobras.utils;

public enum TipoFornecedorEnum {

  Material("Material"),

  Serviço("Serviço"),

  Ambos("Ambos");

  private String tipo;

  TipoFornecedorEnum(String tipo) {
    this.tipo = tipo;
  }

  public String getTipo () {
    return this.tipo;
  }

}
