// package com.gobra.sistemagestaodeobras.estudo;

// import java.text.Normalizer;
// import java.util.Arrays;

// import org.springframework.http.HttpEntity;
// import org.springframework.http.HttpHeaders;
// import org.springframework.http.MediaType;
// import org.springframework.web.client.RestTemplate;

// import com.fasterxml.jackson.databind.JsonNode;
// import com.fasterxml.jackson.databind.ObjectMapper;

// // telegram.token = 6483491941:AAEz5chdrSXuc-Xl9ieafkOh4KBdYeKG6tA
// // telegram.chatId = 5986351015

// // Web Hook: Quando o fornecedor chama algo do usuario.

// public class Bot {

//   public void ConfiguraTelegram () {
//     new Thread(new Runnable() {

//       @Override
//       public void run() {
//         int maxUpdateId = 0;
//         while (true) {
//           try {
//             JsonNode jn = new ObjectMapper().readTree(new RestTemplate().
//                     getForEntity("https://api.telegram.org/bot709125466:/getUpdates?timeout=60" + (maxUpdateId != 0 ? "&offset=" + (maxUpdateId + 1) : ""), String.class).getBody());
            
//             for (JsonNode mensagem : jn.get("result")) {
//               if (mensagem.get("update_id").asInt() > maxUpdateId) {
//                 maxUpdateId = mensagem.get("update_id").asInt();
//               }
              
//               new Thread(new Runnable() {

//                 public void run() {
//                   try {
//                     String retorno = "Usuário sem permissão!";
//                     HttpHeaders headers = new HttpHeaders();
//                     headers.setContentType(MediaType.APPLICATION_JSON);

//                     if(Arrays.asList("663437082"/*sussumu*/,"765070396"/*favato*/,"5986351015"/*felipe */).contains(
//                         mensagem.get("message").get("from").get("id").asInt() + "")
//                       ) {
//                       String entrada = Normalizer.normalize(
//                         mensagem.get("message").get("text").asText().toUpperCase(),
//                         Normalizer.Form.NFD).replaceAll("[\\p{InCombiningDiacriticalMarks}]",
//                         "");
//                       String modo = "proc";

//                       if (entrada.matches(".*CONFIRM.*")) {

//                         String sufixo = "";
//                         if (entrada.endsWith("2")) {
//                           sufixo = " 2";
//                         } else if (entrada.endsWith("3")) {
//                           sufixo = " 3";
//                         } else if (entrada.endsWith("4")) {
//                           sufixo = " 4";
//                         }

//                         if (entrada.matches(".*GRAF.*SP.*")) {
//                           modo = "graf";
//                           entrada = "CONFIRM GRAF SP" + sufixo;
//                           retorno = "";
//                         } else if (entrada.matches(".*GRAF.*")) {
//                           modo = "graf";
//                           entrada = "CONFIRM GRAF" + sufixo;
//                           retorno = "";
//                         } else if (entrada.matches(".*RANK.*")) {
//                           entrada = "CONFIRM RANK" + sufixo;
//                         } else if (entrada.matches(".*UNID.*")) {
//                           modo = "graf";
//                           entrada = "CONFIRM UNID" + sufixo;
//                           retorno = "";
//                         } else {
//                           entrada = "CONFIRM" + sufixo;
//                         }

//                       } else if (entrada.matches(".*UNID.*")) {
//                         modo = "graf";
//                         if (entrada.matches("UNIDADE [FSME] \\d")) {
//                           retorno = "";
//                         } else {
//                           entrada = "UNIDADE";
//                           retorno = "Você pode gerar o gráfico para outras chamadas, por exemplo:\nUNIDADE F 1\nPara a 1a chamada da Fuvest\nUNIDADE E 2\n Para a 2a chamada do ENEM USP";
//                         }
//                       } else if (entrada.matches(".*L1L2.*")) {
//                         modo = "graf";
//                         if (entrada.matches("L1L2 \\d")) {
//                           retorno = "";
//                         } else {
//                           entrada = "L1L2";
//                           retorno = "Você pode gerar o gráfico para outras chamadas, por exemplo:\nL1L2 2\nPara a 2a chamada";
//                         }
//                       } else if (entrada.matches(".*GRAF.*")) {
//                         modo = "graf";
//                         entrada = "GRAFICO";
//                         retorno = "";
//                       } else if (entrada.matches(".*ACOMP.*")) {
//                         entrada = "ACOMP";
//                       } else if (entrada.matches(".*HETEROID.*")) {
//                           if (entrada.matches("HETEROID [FSME] \\d")) {
//                             retorno = "";
//                           } else {
//                             entrada = "HETEROID";
//                             retorno = "Você pode gerar para outras chamadas, por exemplo:\nHETEROID F 1\nPara a 1a chamada da Fuvest\nHETEROID E 2\n Para a 2a chamada do ENEM USP";
//                           }
//                       } else if (entrada.matches(".*APROVEIT.*")) {
//                         entrada = "APROVEITAMENTO";
//                       } else if (entrada.matches(".*CANCEL.*")) {
//                         if (entrada.matches("CANCEL [FSME]")) {
//                         retorno = "";
//                       } else {
//                         entrada = "CANCEL";
//                         retorno = "Você pode filtrar os dados por tipo de ingresso: <b>CANCEL F</b> ou <b>CANCEL E</b>";
//                         }
//                       } else if (entrada.matches(".*CALEND.*")) {
//                         entrada = "CALEND";
//                       } else {
//                         modo = "";
//                         retorno = "<b>Opções disponíveis</b>:\nacompanhamento\naproveitamento\ncalendario\ngrafico\nheteroidentificacao\nl1l2\nopcao\nunidade";
//                       }
//                       if ("proc".equals(modo)) {
//                         System.out.println(
//                           new RestTemplate().postForEntity("https://uspdigital.usp.br/uspproc/listar/jupiterweb/pubMVItesteChatbot",
//                           new HttpEntity(new ObjectMapper().writeValueAsString(new ObjectMapper().createObjectNode().
//                             put("entrada", entrada)), headers), String.class));
//                         retorno = new ObjectMapper().readTree(new RestTemplate().postForEntity(
//                                     "https://uspdigital.usp.br/uspproc/listar/jupiterweb/pubMVItesteChatbot",
//                                     new HttpEntity(new ObjectMapper().writeValueAsString(new ObjectMapper().createObjectNode().
//                                       put("entrada", entrada)), headers), String.class).getBody()).get(0).get("saida").asText();
//                       }
//                     }

//                     if (!retorno.isEmpty()) {
//                       String json = new ObjectMapper().writeValueAsString(new ObjectMapper().createObjectNode().
//                                       put("chat_id", mensagem.get("message").get("chat").get("id").asText()).
//                                       put("text", retorno).
//                                       put("parse_mode", "html"));
//                       System.out.println("<= " + retorno);
//                       new RestTemplate().postForEntity(
//                         "https://api.telegram.org/bot709125466:/sendMessage",
//                         new HttpEntity(json, headers), String.class);
//                     }

//                   } catch (Exception e) {
//                     e.printStackTrace();
//                   }
//                 }
//               }).start();
//             }
//           } catch (Exception e) {
//             e.printStackTrace();
//           }
//         }
//       }
//     }).start();
//   }


//   // Esse método deve sempre retornar o username do meu BOT.
//   // Substituir conteúdo pelo nome desejado.
//   public String getBotUsername() {
//     return "HubConstrucoesBot";
//   }

//   // Esse método será usado pelo framework para recuperar o TOKEN do BOT.
//   // Substituir conteúdo pelo valor do TOKEN gerado no BOTFATHER.
//   public String getBotToken() {
//     return "6483491941:AAEz5chdrSXuc-Xl9ieafkOh4KBdYeKG6tA";
//   }

// }
