package com.gobra.sistemagestaodeobras.bot;

import java.time.LocalTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.context.annotation.ApplicationScope;



@ApplicationScope
public class MessageCalculator {

  @Autowired
  Bot bot;

  @GetMapping
  public void message () {
    String message = "Now it is " + LocalTime.now() + ": " + UUID.randomUUID();
    bot.sendMessage(message);
  }


}
