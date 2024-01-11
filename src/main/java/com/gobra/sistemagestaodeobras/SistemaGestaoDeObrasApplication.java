// Arquivo de start da aplicação 
package com.gobra.sistemagestaodeobras;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.gobra.sistemagestaodeobras.bot.BotService;

// 1. @@SpringBootApplication: Essa notação indica que essa classe é o começo de tudo.
@SpringBootApplication
public class SistemaGestaoDeObrasApplication {


	@Autowired
	BotService botService;

	// metodo main = ponto de start da aplicação
	public static void main(String[] args) {
		// Chama a SpringApplication e da um run na aplicação
		SpringApplication.run(SistemaGestaoDeObrasApplication.class, args);
	}

	public void telegram () {
		botService.configuraTelegram();
	}

}
