// Arquivo de start da aplicação
package com.gobra.sistemagestaodeobras;

import java.io.FileOutputStream;
import java.text.Normalizer;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
// import org.telegram.telegrambots.meta.TelegramBotsApi;
// import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
// import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gobra.sistemagestaodeobras.dashboard.dto.MDOOrcamentoDTO;
import com.gobra.sistemagestaodeobras.repository.CompraRepository;

import jakarta.annotation.PostConstruct;

// import com.gobra.sistemagestaodeobras.bot.Bot;

// Saber os endpoints, outputs e inputs.

// Essa notação indica que essa classe é o começo de tudo
@SpringBootApplication
public class SistemaGestaoDeObrasApplication {

	@Autowired
	CompraRepository compraRepository;

	// metodo main = ponto de start da aplicação
	public static void main(String[] args) {
		// Registrando o BOT na API:
		// TelegramBotsApi botsAPI = new TelegramBotsApi(DefaultBotSession.class);
		// botsAPI.registerBot(new Bot());
		
		// Chama a SpringApplication e da um run na aplicação
		SpringApplication.run(SistemaGestaoDeObrasApplication.class, args);
	}

	@PostConstruct
	public void configuraTelegram () {
		new Thread(new Runnable() {

            @Override
            public void run() {
                int maxUpdateId = 0;
                while (true) {
                    try {
                        JsonNode jn = new ObjectMapper().readTree(new RestTemplate().
                                getForEntity("https://api.telegram.org/bot6483491941:AAEz5chdrSXuc-Xl9ieafkOh4KBdYeKG6tA/getUpdates?timeout=60" + (maxUpdateId != 0 ? "&offset=" + (maxUpdateId + 1) : ""), String.class).getBody());
                        for (JsonNode mensagem : jn.get("result")) {
                            if (mensagem.get("update_id").asInt() > maxUpdateId) {
                                maxUpdateId = mensagem.get("update_id").asInt();
                            }
                            new Thread(new Runnable() {

                                public void run() {
                                    try {
                                        HttpHeaders headers = new HttpHeaders();
                                        headers.setContentType(MediaType.APPLICATION_JSON);
																				MDOOrcamentoDTO mdoOrcamento = compraRepository.obterMDOGastoComprasOrcamento(Integer codigo);
                                        System.out.println("\n" + mensagem.toString());
                                        System.out.println("=> " + new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()) + " [" + mensagem.get("message").get("chat").get("id").asText() + "] " + mensagem.get("message").get("from").get("first_name").asText() + /*" " + mensagem.get("message").get("from").get("last_name").asText() +*/ " (" + mensagem.get("message").get("from").get("id").asInt() + "): " + mensagem.get("message").get("text").asText());  
																				String resposta = "Respondendo: " + mensagem.toString();
																				String json = new ObjectMapper().writeValueAsString(new ObjectMapper().createObjectNode().
																								put("chat_id", mensagem.get("message").get("chat").get("id").asText()).
																								put("text", resposta).
																								put("parse_mode", "html"));
																				System.out.println("<= " + resposta);
																				new RestTemplate().postForEntity("https://api.telegram.org/bot6483491941:AAEz5chdrSXuc-Xl9ieafkOh4KBdYeKG6tA/sendMessage",
																								new HttpEntity(json, headers), String.class);
																				
																				// String retorno = "Usuário sem permissão!";
                                        // HttpHeaders headers = new HttpHeaders();
                                        // headers.setContentType(MediaType.APPLICATION_JSON);

                                        // if (Arrays.asList("663437082" /*sussumu*/, "765070396" /*favato*/, ).contains(mensagem.get("message").get("from").get("id").asInt() + "")) {
                                        //     String entrada = Normalizer.normalize(mensagem.get("message").get("text").asText().toUpperCase(), Normalizer.Form.NFD).replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
                                        //     String modo = "proc";
                                           
                                        //     if ("proc".equals(modo)) {
                                        //         System.out.println(new RestTemplate().postForEntity("https://uspdigital.usp.br/uspproc/listar/jupiterweb/pubMVItesteChatbot",
                                        //                 new HttpEntity(new ObjectMapper().writeValueAsString(new ObjectMapper().createObjectNode().
                                        //                         put("entrada", entrada)), headers), String.class));
                                        //         retorno = new ObjectMapper().readTree(new RestTemplate().postForEntity("https://uspdigital.usp.br/uspproc/listar/jupiterweb/pubMVItesteChatbot",
                                        //                 new HttpEntity(new ObjectMapper().writeValueAsString(new ObjectMapper().createObjectNode().
                                        //                         put("entrada", entrada)), headers), String.class).getBody()).get(0).get("saida").asText();
                                        //     }
                                        //     if ("graf".equals(modo)) {
                                        //         DefaultCategoryDataset dataset = new DefaultCategoryDataset();
                                        //         String titulo = "", eixoX = "", eixoY = "", conjunto = "";
                                        //         int altura = 400;
                                        //         for (JsonNode par : new ObjectMapper().readTree(new RestTemplate().postForEntity("https://uspdigital.usp.br/uspproc/listar/jupiterweb/pubMVItesteChatbot",
                                        //                 new HttpEntity(new ObjectMapper().writeValueAsString(new ObjectMapper().createObjectNode().
                                        //                         put("entrada", entrada)), headers), String.class).getBody())) {
                                        //             titulo = par.get("titulo").asText();
                                        //             eixoX = par.get("eixo_x").asText();
                                        //             eixoY = par.get("eixo_y").asText();
                                        //             altura = par.get("altura").asInt();
                                        //             conjunto = par.get("conjunto").asText();
                                        //             dataset.addValue(par.get("y").asDouble(), conjunto, par.get("x").asText());
                                        //         }
                                        //         JFreeChart chart = ChartFactory.createStackedBarChart(titulo, eixoX, eixoY, dataset, PlotOrientation.HORIZONTAL, true, true, false);
                                        //         File temp = new File(new Random().nextInt(1000 * 1000 * 1000) + ".jpg");
                                        //         ChartUtilities.writeChartAsJPEG(new FileOutputStream(temp), chart, 400, altura);    
                                        //         MultiValueMap<String, Object> params = new LinkedMultiValueMap();
                                        //         params.add("photo", new FileSystemResource(temp));
                                        //         params.add("chat_id", mensagem.get("message").get("chat").get("id").asText());
                                        //         HttpHeaders headers2 = new HttpHeaders();
                                        //         headers2.setContentType(MediaType.MULTIPART_FORM_DATA);
                                        //         new RestTemplate().postForEntity("https://api.telegram.org/bot709125466:/sendPhoto",
                                        //                 new HttpEntity(params, headers2), String.class);
                                        //         temp.delete();
                                        //     }
                                        // }
                                        // if (!retorno.isEmpty()) {
                                        //     String json = new ObjectMapper().writeValueAsString(new ObjectMapper().createObjectNode().
                                        //             put("chat_id", mensagem.get("message").get("chat").get("id").asText()).
                                        //             put("text", retorno).
                                        //             put("parse_mode", "html"));
                                        //     System.out.println("<= " + retorno);
                                        //     new RestTemplate().postForEntity("https://api.telegram.org/bot709125466:/sendMessage",
                                        //             new HttpEntity(json, headers), String.class);
                                        // }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }).start();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
	}


}


// EXEMPLOS 
// // Component BookingCommandLineRunner implementa a interface CommandLineRunner que tem
// // um método run
// @Component
// class BookingCommandLineRunner implements CommandLineRunner {

// 	// Usando o método de sobrecarga para reescrever um método da super()
// 	@Override
// 	public void run(String... args) throws Exception {

// 		// for (Booking b : this.bookingRepository.findAll())
// 		// 	System.out.println(b.toString());
// 	}

// 	// @Autowired BookingRepository bookingRepository;
// }


// // Interface BookingRepository extende a interface JpaRepository que recebe uma classe e um ID
// // A classe JpaRepository extende de outras interfaces (classes abstratas) que possuem Crud e 
// // getters elaborados com sorting, etc
// interface BookingRepository extends JpaRepository<Booking, Long> {

// 	// Collection é uma interface com diversos metodos: add, remove, size, removeAll, etc...
// 	Collection<Booking> findByBookingName(String bookingName);

// }


// // Entidade básica
// @Entity
// class Booking {

// 	@Id @GeneratedValue
//   // primary key
// 	// Long => Tipo de dado que pode guardar numeros inteiros
// 	// (de -9223372036854775808 até 9223372036854775808)
// 	private Long id;
// 	private String bookingName;

// 	// Essa função recebe uma string com o nome da reserva e seta
// 	// o valor do objeto.bookingName instanciado
// 	// Chama o método da superclasse
// 	public Booking(String bookingName) {
// 		super();
// 		this.bookingName = bookingName;
// 	}

// 	public Booking() {

// 	}

// 	// Método publico para retornar o id
// 	public Long getId() {
// 		return id;
// 	}

// 	// Método público para retornar o nome da reserva
// 	public String getBookingName() {
// 		return bookingName;
// 	}
// }


// // Configuração pom.xml
// // <!-- <dependency>
// //   <groupId>com.h2database</groupId>
// //   <artifactId>h2</artifactId>
// // </dependency> -->