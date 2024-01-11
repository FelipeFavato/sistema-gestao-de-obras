package com.gobra.sistemagestaodeobras.bot.dto;

import com.gobra.sistemagestaodeobras.bot.projection.TelegramIDBotProjection;

public class TelegramIDBotDTO {
  
  Long telegramID;
  
  public TelegramIDBotDTO() {

  }

  public TelegramIDBotDTO(Long teleID) {
    this.telegramID = teleID;
  }

  public TelegramIDBotDTO(TelegramIDBotProjection projection) {
    telegramID = projection.getTelegramID();
  }

  public String toString () {
    return "Telegram ID: " + telegramID;
  }

  // Getters e Setters
  public Long getTelegramID () {
    return telegramID;
  }

  public void setTelegramID (Long teleID) {
    this.telegramID = teleID;
  }
}
