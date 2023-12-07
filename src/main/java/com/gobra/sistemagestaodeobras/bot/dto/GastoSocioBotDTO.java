package com.gobra.sistemagestaodeobras.bot.dto;

import com.gobra.sistemagestaodeobras.bot.projection.GastoSocioBotProjection;
import com.gobra.sistemagestaodeobras.utils.FormatadorMoeda;

public class GastoSocioBotDTO {
  
  private String nomeObra;

  private String nomeSocio;

  private double valorFinal;


  public GastoSocioBotDTO() {

  }

  public GastoSocioBotDTO(String nomeObra, String nomeSocio, double valorF) {
    this.nomeObra = nomeObra;
    this.nomeSocio = nomeSocio;
    this.valorFinal = valorF;
  } 

  public GastoSocioBotDTO(GastoSocioBotProjection projection) {
    nomeObra = projection.getNomeObra();
    nomeSocio = projection.getNomeSocio();
    valorFinal = projection.getValorFinal();
  }


  public String toString () {
    return nomeSocio + ": " + FormatadorMoeda.formatarMoeda(valorFinal);
  }

  
  // Getters e Setters
  public String getNomeObra () {
    return nomeObra;
  }

  public void setNomeObra (String nomeO) {
    this.nomeObra = nomeO;
  }

  public String getNomeSocio () {
    return nomeSocio;
  }

  public void setNomeSocio (String nomeS) {
    this.nomeSocio = nomeS;
  }

  public double getValorFinal () {
    return valorFinal;
  }

  public void setValorFinal (double valorF) {
    this.valorFinal = valorF;
  }
}
