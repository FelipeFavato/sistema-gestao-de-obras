package com.gobra.sistemagestaodeobras.dashboard.dto;

import com.gobra.sistemagestaodeobras.dashboard.projection.ObraCodNomeProjection;

public class ObraCodNomeDTO {

  Integer codigo;

  String nome;

  public ObraCodNomeDTO() {

  }

  public ObraCodNomeDTO(Integer cod, String nome) {
    this.codigo = cod;
    this.nome = nome;
  }

  public ObraCodNomeDTO(ObraCodNomeProjection projection) {
    codigo = projection.getCodigo();
    nome = projection.getNome();
  }

  public String toString() {
    return nome;
  }

  // Getters e Setters
  public Integer getCodigo () {
    return codigo;
  }

  public void setCodigo (Integer cod) {
    this.codigo = cod;
  }

  public String getNome () {
    return nome;
  }

  public void setNome (String nome) {
    this.nome = nome;
  }

}
