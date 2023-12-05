package com.gobra.sistemagestaodeobras.bot;

// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
// import org.springframework.web.client.RestTemplate;

// import com.fasterxml.jackson.databind.JsonNode;
// import com.fasterxml.jackson.databind.ObjectMapper;

// telegram.token = 6483491941:AAEz5chdrSXuc-Xl9ieafkOh4KBdYeKG6tA
// telegram.chatId = 5986351015


@RestController
@RequestMapping("consulta-bot")
public class Bot {

  public void sendMessage (String message) {

  }

  // @GetMapping("{message}")
  // public JsonNode consultaCep (@PathVariable("message") String cep) {
  //   // RestTemplate restTemplate = new RestTemplate();
  //   try {
  //     JsonNode jn = new ObjectMapper().readTree(new RestTemplate().
  //                               getForEntity("https://api.telegram.org/bot6483491941:AAEz5chdrSXuc-Xl9ieafkOh4KBdYeKG6tA", String.class).getBody());
      
  //     // return jn;
  //   } catch (Exception e) {
  //     e.printStackTrace();
  //   }

  // }



  // Esse método deve sempre retornar o username do meu BOT.
  // Substituir conteúdo pelo nome desejado.
  public String getBotUsername() {
    return "HubConstrucoesBot";
  }

  // Esse método será usado pelo framework para recuperar o TOKEN do BOT.
  // Substituir conteúdo pelo valor do TOKEN gerado no BOTFATHER.
  public String getBotToken() {
    return "6483491941:AAEz5chdrSXuc-Xl9ieafkOh4KBdYeKG6tA";
  }

}
