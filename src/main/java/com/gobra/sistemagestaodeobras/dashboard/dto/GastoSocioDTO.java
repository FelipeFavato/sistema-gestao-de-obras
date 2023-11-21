package com.gobra.sistemagestaodeobras.dashboard.dto;

import com.gobra.sistemagestaodeobras.dashboard.projection.GastoPorSocioProjection;

public class GastoSocioDTO {
  
  private String nomeSocio;

  private double valorFinal;

  public GastoSocioDTO() {

  }

  public GastoSocioDTO(String nomeSocio, double valorFinal) {
    this.nomeSocio = nomeSocio;
    this.valorFinal = valorFinal;
  }

  public GastoSocioDTO(GastoPorSocioProjection projection) {
    nomeSocio = projection.getNomeSocio();
    valorFinal = projection.getValorFinal();
  }

  // Getter e Setters
  public String getNomeSocio() {
    return nomeSocio;
  }

  public void setNomeSocio(String nome) {
    this.nomeSocio = nome;
  }

  public double getValorFinal() {
    return valorFinal;
  }

  public void setValorFinal(double valor) {
    this.valorFinal = valor;
  }
}
