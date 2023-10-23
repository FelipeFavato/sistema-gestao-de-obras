package com.gobra.sistemagestaodeobras.utils;

public enum TipoPerfilEnum {
  
  Gestor("Gestor"),

  Operacional("Operacional");

  private String tipo;

  TipoPerfilEnum(String tipo) {
    this.tipo = tipo;
  }

  public String getTipo () {
    return this.tipo;
  }

}
