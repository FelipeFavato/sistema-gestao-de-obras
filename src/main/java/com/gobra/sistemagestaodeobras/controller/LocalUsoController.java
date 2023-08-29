package com.gobra.sistemagestaodeobras.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gobra.sistemagestaodeobras.dto.LocalUsoRequestDTO;
import com.gobra.sistemagestaodeobras.dto.LocalUsoResponseDTO;
import com.gobra.sistemagestaodeobras.model.LocalUsoModel;
import com.gobra.sistemagestaodeobras.repository.LocalUsoRepository;


@RestController  // Essa notação indica que essa classe é um controller
@RequestMapping("/localuso")  // Essa notação indica qual endpoint essa classe controla
public class LocalUsoController {

  // Essa notação indica para o spring que é ele quem vai fazer a injeção
  @Autowired  // dessa dependencia na minha classe LocalUsoController
  private LocalUsoRepository repository;  // Instancia de LocalUsoRepository criada

  // Essa faz com que quando eu acessar esse endpoint
  @PostMapping  // com o metodo POST, é pra executar o metodo abaixo
  public void saveLocalUso(@RequestBody LocalUsoRequestDTO data) {
    // O parametro desse metodo é o body que veio da request do client
    LocalUsoModel localUsoData = new LocalUsoModel(data);
    repository.save(localUsoData);
    return;
  }

  // Essa notação faz com que quando eu acessar esse endpoint
  @GetMapping  // com o metodo GET, é pra executar o metodo abaixo
  public List<LocalUsoResponseDTO> getAll() { // LocalUso
    // stream => Vai pegar tudo que ta vindo do repository.findAll() e vai colocar dentro de um funil
    // map => Para cada objeto dentro desse funil, vou criar um DTO
    List<LocalUsoResponseDTO> localUsoList = repository.findAll().stream().map(LocalUsoResponseDTO::new).toList(); // repository.findAll()
    return localUsoList;
  }
}
