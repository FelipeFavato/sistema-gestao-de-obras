package com.gobra.sistemagestaodeobras.utils;

public enum TipoProdutoEnum {

  Material("Material"),

  Serviço("Serviço"),

  TaxasImpostos("Taxas/Impostos");

  private String tipo;

  TipoProdutoEnum(String tipo) {
    this.tipo = tipo;
  }

  public String getTipo () {
    return this.tipo;
  }

}
