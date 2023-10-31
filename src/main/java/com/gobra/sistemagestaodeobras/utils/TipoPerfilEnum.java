package com.gobra.sistemagestaodeobras.utils;

public enum TipoPerfilEnum {
  
  ADMIN("admin"),

  USER("user");

  private String role;

  TipoPerfilEnum(String role) {
    this.role = role;
  }

  public String getRole () {
    return this.role;
  }

}
