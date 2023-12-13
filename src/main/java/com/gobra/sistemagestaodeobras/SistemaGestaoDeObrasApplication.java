// Arquivo de start da aplicação
package com.gobra.sistemagestaodeobras;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gobra.sistemagestaodeobras.bot.BotController;
import com.gobra.sistemagestaodeobras.bot.dto.GastoSocioBotDTO;
import com.gobra.sistemagestaodeobras.bot.dto.OrcamentoBotDTO;
import com.gobra.sistemagestaodeobras.bot.dto.SocioTelegramIDBotDTO;
import com.gobra.sistemagestaodeobras.repository.CompraRepository;
import com.gobra.sistemagestaodeobras.utils.FormatadorMoeda;
import com.gobra.sistemagestaodeobras.utils.ManipuladorNumeros;
import com.gobra.sistemagestaodeobras.utils.ManipuladorStrings;

import jakarta.annotation.PostConstruct;

// Saber os endpoints, outputs e inputs.

// 1. @@SpringBootApplication: Essa notação indica que essa classe é o começo de tudo.
@SpringBootApplication
public class SistemaGestaoDeObrasApplication {

	// Injeta os repositórios em que estão os serviços.
  @Autowired
	CompraRepository compraRepository;

	@Autowired
	BotController botController;

	// metodo main = ponto de start da aplicação
	public static void main(String[] args) {
		// Chama a SpringApplication e da um run na aplicação
		SpringApplication.run(SistemaGestaoDeObrasApplication.class, args);
	}

	// 1. "@PostConstruct": A anotação @PostConstruct em Java é usada em métodos de uma classe para indicar
	//    que o método deve ser executado uma vez que a inicialização da instância da classe esteja completa.
	//    Este método é chamado automaticamente pelo contêiner de inversão de controle (IoC) após a construção
	//    do bean e antes que qualquer chamada de método seja feita ou antes que a instância seja
	//    disponibilizada para o uso.
	// 2. "public void configuraTelegram () {...}": Método com retorno void (vazio) com nome "ConfiguraTelegram".
	@PostConstruct
	public void configuraTelegram () {
		// 1. "new Thread(new Runnable()) {...}": Inicia uma nova thread (linha de execução paralela) usando a
		//    classe 'Thread' e implementando a interface 'Runnable'. Este bloco de código é uma maneira de
		// 		executar operações em paralelo para melhorar a eficiência.
	  new Thread(new Runnable() {

      @Override
			// 1. "public void run() {...}": Método com retorno void (vazio) com nome 'run'.
      public void run() {
				// BOT PROD oficial:
				// String username = "HubConstrucoesBot";
				// Token necessário para fazer a requisição à API do Telegram.
				String token = "6483491941:AAEz5chdrSXuc-Xl9ieafkOh4KBdYeKG6tA";

				// BOT DEV teste:
				// String usernameTeste = "TESTEHubConstrucoesBot";
				// String token = "6574404905:AAHZHyYnXNFviT17pS1fVtdEpqGvf1ncHyg";

				// Variável auxiliar 'maxUpdateId'.
        int maxUpdateId = 0;

				// 1. while (true): Enquanto 'true === true' vai rodar o código abaixo (loop infinito).
        while (true) {
          try {
						// 1. "JsonNode jn": 'JsonNode' é uma Representação de nó em uma árvore JSON. 'jn' é uma variável
						// 		que armazena a árvode de nós que representa as atualizações do Telegram.
						// 2. "new ObjectMapper().readTree(...)": 'new ObjectMapper()' cria uma instância do ObjectMapper,
						// 		que é uma classe da biblioteca Jackson para mapeamento de JSON para objetos JAVA.
						//    'readTree(...)' lê o JSON fornecido e o converte para uma arvore de nós do tipo JsonNode.
						// 3. "new RestTemplate().getForEntity(...)": 'RestTemplate()'' é uma classe que faz parte do
						//    framework do Spring e fornece método convenientes para interagir com API's REST externas.
						// 		'new RestTemplate()' cria uma instância de 'RestTemplate' e o método 'getForEntity' é
						// 		chamado para realizar uma requisição GET à URL do Telegram.
						// 4. "https://api.telegram.org/bot" + token + "/getUpdates?timeout=60": URL da API do Telegram.
						// 		Possui o método getUpdates que é usado para obter as atualizações recentes do bot.
						// 5. "+ (maxUpdateId != 0 ? "&offset=" + (maxUpdateId + 1) : "")": O parametro 'maxUpdateId' é
						//    opcional e, se não for zero, é adicionado à URL como o parametro 'offset', indicando para a
						// 		API do telegram que deve começar a buscar atualizações a partir do 'ID especificado + 1'.
						// 6. "String.class": Este é o tipo de resposta esperada. Neste caso, espera-se uma resposta no
						//    formato de string; 
						// 7. "getBody()": Este método é chamado na resposta da requisição para obter o corpo da resposta,
						//    que, no caso, é um JSON contendo as informações de atualizações do bot.
            JsonNode jn = new ObjectMapper().readTree(new RestTemplate().getForEntity(
							"https://api.telegram.org/bot" + token + "/getUpdates?timeout=60" + (maxUpdateId != 0 ? "&offset=" + (maxUpdateId + 1) : ""),
							String.class).getBody());

						// 1. "for (JsonNode mensagem : jn.get("result"))": Inicia um loop for que itera sobre cada elemento
						// 		do ARRAY "result" em um objeto JsonNode chamado 'jn'. Então existe um array chamado 'result' e
						//    dentro desse array tem varios objetos chamados 'jn' e ai o loop for declara uma variável chamada
						//    'mensagem' do tipo 'JsonNode' que são cada 'jn' presente no array.
						// 2. "if (mensagem.get("update_id").asInt() > maxUpdateId)": Verifica se o valor associado a chave
						//    'update_id' no objeto JsonNode 'mensagem' é maior do que o valor atual de 'maxUpdateId'. Ou seja,
						// 		verifica o valor do chave 'update_id' de cada mensagem e compara com o valor de 'maxUpdateId' do
						// 		programa. Se o valor dessa chave do objeto mensagem for de fato maior, executa o código 3 abaixo.
						// 3. "maxUpdateId = mensagem.get("update_id").asInt()": Atualiza o valor de 'maxUpdateId' para o mesmo
						//    valor presente no objeto 'mensagem'.
						for (JsonNode mensagem : jn.get("result")) {
              if (mensagem.get("update_id").asInt() > maxUpdateId) {
                maxUpdateId = mensagem.get("update_id").asInt();
              }
              
							// 1. "new Thread(new Runnable()) {...}": Inicia uma nova thread (linha de execução paralela) usando a
							//    classe 'Thread' e implementando a interface 'Runnable'. Este bloco de código é uma maneira de
							// 		executar operações em paralelo para melhorar a eficiência.
							// 2. "public void run () {...}": Implementa o método 'run' da interface 'Runnable', que contém o
							//    código que sera executado na nova thread.
							// 3. "HttpHeaders headers = new HttpHeaders()": Cria um objeto do tipo 'HttpHeaders' para configurar
							//    os cabeçalhos HTTP.
							// 4. "headers.setContentType(MediaType.APPLICATION_JSON)": Define o tipo de conteúdo do cabeçalho
							// 		como JSON.
							new Thread(new Runnable() {

              	public void run() {
                  try {
										String retorno = "Usuário sem permissão!";
                    HttpHeaders headers = new HttpHeaders();
                    headers.setContentType(MediaType.APPLICATION_JSON);
										
										List<String> arrayIDs = new ArrayList<>();
										arrayIDs.add("663437082"); /*sussumu*/
										arrayIDs.add("5986351015"); /*felipe*/
										List<SocioTelegramIDBotDTO> listaSocioTeleID = botController.getSocioTeleID();
										for (SocioTelegramIDBotDTO socioTeleID : listaSocioTeleID) {
											String idAsString = String.valueOf(socioTeleID.getTelegramID());
											arrayIDs.add(idAsString);
										}

										// "663437082"/*sussumu*/,"765070396"/*favato*/,"5986351015"/*felipe */
										if(/*Arrays.asList*/(arrayIDs).contains(
											mensagem.get("message").get("from").get("id") + "")) {

											String entrada = Normalizer.normalize(
												mensagem.get("message").get("text").asText().toUpperCase(),
												Normalizer.Form.NFD).replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");

											if (entrada.matches(".*OPC.*")) {
												retorno = "Possíveis OPÇÕES" + "\n \n" +
																	"Opcoes|Opc" + "\n" +
																	"Orcamento|Orc [Código da obra]" + "\n" +
																	"Socio|Soc [Código da obra]";
											
											} else if (entrada.matches(".*ORC.*")) {
												OrcamentoBotDTO orcamento = botController.getOrcamento(ManipuladorNumeros.extrairNumero(entrada));
												retorno = entrada.toString() + "\n" + orcamento.toString();
												
											} else if (entrada.matches(".*SOC.*")) {
												String retornoSocios = "";
												String nomeObra = "";
												List<GastoSocioBotDTO> listGastoSocio = botController.getGastoSocio(ManipuladorNumeros.extrairNumero(entrada));
												for (GastoSocioBotDTO socio : listGastoSocio) {
													retornoSocios += ManipuladorStrings.extrairPrimeiroUltimoNome(socio.getNomeSocio()) + ": "
																				   + "<b>" + FormatadorMoeda.formatarMoeda(socio.getValorFinal()) + "</b>" + "\n";
													nomeObra = socio.getNomeObra();
												}
												retorno = entrada.toString() + "\n" + "Obra: " + "<b>" + nomeObra + "</b>" + "\n \n"
																	+ "<b>GASTO TOTAL POR SÓCIO</b>" + "\n \n" + retornoSocios;
											}

                    	String json = new ObjectMapper().writeValueAsString(new ObjectMapper().createObjectNode().
                              put("chat_id", mensagem.get("message").get("chat").get("id").asText()).
                              put("text", retorno).
                              put("parse_mode", "html"));
                    	new RestTemplate().postForEntity(
												"https://api.telegram.org/bot" + token + "/sendMessage", new HttpEntity<>(json, headers),
												String.class);
										} else {
											String json = new ObjectMapper().writeValueAsString(new ObjectMapper().createObjectNode().
                              put("chat_id", mensagem.get("message").get("chat").get("id").asText()).
                              put("text", retorno).
                              put("parse_mode", "html"));
                    	new RestTemplate().postForEntity(
												"https://api.telegram.org/bot" + token + "/sendMessage", new HttpEntity<>(json, headers),
												String.class);

										}

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