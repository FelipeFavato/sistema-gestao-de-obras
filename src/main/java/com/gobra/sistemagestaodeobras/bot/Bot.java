package com.gobra.sistemagestaodeobras.bot;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

public class Bot extends TelegramLongPollingBot {

  // Esse método deve sempre retornar o username do meu BOT.
  // Substituir conteúdo pelo nome desejado.
  @Override
  public String getBotUsername() {
    return "HUB Construções INFO BOT";
  }

  // Esse método será usado pelo framework para recuperar o TOKEN do BOT.
  // Substituir conteúdo pelo valor do TOKEN.
  @Override
  public String getBotToken() {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'getBotToken'");
  }

  @Override
  public void onUpdateReceived(Update arg0) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'onUpdateReceived'");
  }

}
