package com.gobra.sistemagestaodeobras.bot;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gobra.sistemagestaodeobras.bot.dto.GastoSocioBotDTO;
import com.gobra.sistemagestaodeobras.bot.dto.OrcamentoBotDTO;
import com.gobra.sistemagestaodeobras.bot.dto.SocioTelegramIDBotDTO;
import com.gobra.sistemagestaodeobras.bot.dto.TelegramIDBotDTO;
import com.gobra.sistemagestaodeobras.repository.CompraRepository;
import com.gobra.sistemagestaodeobras.utils.FormatadorMoeda;
import com.gobra.sistemagestaodeobras.utils.ManipuladorNumeros;
import com.gobra.sistemagestaodeobras.utils.ManipuladorStrings;

import jakarta.annotation.PostConstruct;


@Service
public class BotService {
	// Injeta os repositórios em que estão os serviços.
  @Autowired
	CompraRepository compraRepository;

	@Autowired
	BotController botController;


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
										
										// Cria a lista da primeira verificação(sócio cadastrado):
										List<String> arrayIDs = new ArrayList<>();
										arrayIDs.add("663437082"); /*sussumu*/
										arrayIDs.add("5986351015"); /*felipe*/
										List<SocioTelegramIDBotDTO> listaSocioTeleID = botController.getSocioTeleID();
										for (SocioTelegramIDBotDTO socioTeleID : listaSocioTeleID) {
											String idAsString = String.valueOf(socioTeleID.getTelegramID());
											arrayIDs.add(idAsString);
										}

                    // 1. if((arrayIDs).contains(mensagem.get("message").get("from").get("id") + "")) {...}: verifica se o
                    //    sócio está cadastrado no BOT da empresa como um todo, mesmo que ele não participe de algumas obras.
                    //    Aqui é verificado apenas se ele consta como sócio e tem um ID Telegram ativo.
										if((arrayIDs).contains(mensagem.get("message").get("from").get("id") + "")) {

											String entrada = Normalizer.normalize(
												mensagem.get("message").get("text").asText().toUpperCase(), Normalizer.Form.NFD)
                        .replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
											
											// Cria a lista da segunda verificação(sócio pertencente a obra da qual ele pede informações):
											List<String> arraysIDsPorObra = new ArrayList<> ();
											List<TelegramIDBotDTO> listaIDs = botController.getTeleID(ManipuladorNumeros.extrairNumero(entrada));
											arraysIDsPorObra.add("663437082"); /*sussumu*/
											arraysIDsPorObra.add("5986351015"); /*felipe*/
											for (TelegramIDBotDTO teleID : listaIDs) {
												if (teleID.getTelegramID() != 0) {
													String teleIDAString = String.valueOf(teleID.getTelegramID());
													arraysIDsPorObra.add(teleIDAString);
												}
											}

											// 1. if((arraysIDsPorObra).contains(mensagem.get("message").get("from").get("id") + "")): segunda verificação,
											// 		verifica se o sócio esta participando da obra da qual ele esta pedindo informação.
											if((arraysIDsPorObra).contains(mensagem.get("message").get("from").get("id") + "")) {
												
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
													double somaGastosSocios = 0;
													List<GastoSocioBotDTO> listGastoSocio = botController.getGastoSocio(ManipuladorNumeros.extrairNumero(entrada));
													for (GastoSocioBotDTO socio : listGastoSocio) {
														retornoSocios += ManipuladorStrings.extrairPrimeiroUltimoNome(socio.getNomeSocio()) + ": "
																						 + "<b>" + FormatadorMoeda.formatarMoeda(socio.getValorFinal()) + "</b>" + "\n";
														nomeObra = socio.getNomeObra();
														somaGastosSocios += socio.getValorFinal();
													}
													retorno = entrada.toString() + "\n" + "Obra: " + "<b>" + nomeObra + "</b>" + "\n \n"
																		+ "<b>GASTO TOTAL POR SÓCIO</b>" + "\n \n" + retornoSocios + "\n \n"
																		+ "<b>" + "Valor total investido: " + FormatadorMoeda.formatarMoeda(somaGastosSocios) + "</b>";
												}
	
												// Esse é o retorno do Sócio cadastrado e participante da obra da qual ele pede informações.
												String json = new ObjectMapper().writeValueAsString(new ObjectMapper().createObjectNode().
																put("chat_id", mensagem.get("message").get("chat").get("id").asText()).
																put("text", retorno).
																put("parse_mode", "html"));
												new RestTemplate().postForEntity(
													"https://api.telegram.org/bot" + token + "/sendMessage", new HttpEntity<>(json, headers),
													String.class);
											}
										} else {
											// Aqui o retorno expressa a situação em que o sócio não esta cadastrado.
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
