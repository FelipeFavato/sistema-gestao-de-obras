package com.gobra.sistemagestaodeobras.bot.dto;

import com.gobra.sistemagestaodeobras.bot.projection.SocioTelegramIDBotProjection;

public class SocioTelegramIDBotDTO {

  String nomeSocio;

  Long telegramID;

  public SocioTelegramIDBotDTO() {

  }

  public SocioTelegramIDBotDTO(String nome, Long teleID) {
    this.nomeSocio = nome;
    this.telegramID = teleID;
  }

  public SocioTelegramIDBotDTO(SocioTelegramIDBotProjection projection) {
    nomeSocio = projection.getNomeSocio();
    telegramID = projection.getTelegramID();
  }

  // Getters e Setters
  public String getNomeSocio() {
    return nomeSocio;
  }

  public void setNomeSocio(String nome) {
    this.nomeSocio = nome;
  }

  public Long getTelegramID () {
    return telegramID;
  }

  public void setTelegramID (Long teleID) {
    this.telegramID = teleID;
  }
  
}
