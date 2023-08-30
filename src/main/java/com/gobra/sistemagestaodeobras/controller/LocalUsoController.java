// Controller => Concentra os Requests para captar os
// dados e adicionar novas informações no BD

package com.gobra.sistemagestaodeobras.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
// import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gobra.sistemagestaodeobras.dto.LocalUsoRequestDTO;
import com.gobra.sistemagestaodeobras.dto.LocalUsoResponseDTO;
import com.gobra.sistemagestaodeobras.model.LocalUso;
import com.gobra.sistemagestaodeobras.repository.LocalUsoRepository;

import jakarta.persistence.EntityNotFoundException;


@RestController  // Essa notação indica que essa classe é um controller
@RequestMapping("/localuso")  // Essa notação indica qual endpoint essa classe controla
public class LocalUsoController {

  // Essa notação indica para o spring que é ele quem vai fazer a injeção
  @Autowired  // dessa dependencia na minha classe LocalUsoController
  private LocalUsoRepository repository;  // Instancia de LocalUsoRepository criada

  
  // Ajuda na conexão com o Front. "*" = permite a conexão de todas as origens
  // @CrossOrigin(origins = "*", allowedHeaders = "*")  // trocar origins = "http://localhost/8080" (mais seguro)
  @PostMapping  // Crud - Create
  public void saveLocalUso(@RequestBody LocalUsoRequestDTO data) {
    // O parametro desse metodo é o body que veio da request do cliente
    LocalUso localUsoData = new LocalUso(data);
    repository.save(localUsoData);
    return;
  }


  // Ajuda na conexão com o Front. "*" = permite a conexão de todas as origens
  // @CrossOrigin(origins = "*", allowedHeaders = "*")  // trocar origins = "http://localhost/8080" (mais seguro)
  @GetMapping  // cRud - Read
  public List<LocalUsoResponseDTO> getAll() {
    // Como transformar a entidade criada em um DTO?
    // stream() => Pega tudo que vem do repository.findAll() e coloca dentro de um funil
    // map() => Para cada objeto dentro desse funil, cria um DTO
    // toList() => Transforma em uma List
    List<LocalUsoResponseDTO> localUsoList = repository.findAll().stream().map(LocalUsoResponseDTO::new).toList(); // repository.findAll()
    return localUsoList;
  }


  @PutMapping  // crUd - Update
  @Transactional  // Método só deve ser executado se todas as transações tiverem sucesso
  public ResponseEntity<LocalUso> updateLocalUso(@RequestBody LocalUsoRequestDTO data){
    // ResponseEntity => Representa a resposta HTTP como um todo (status code, headers e body)
    // Optional permite ter um valor presente ou não, pois estou procurando pelo ID e pode não ter o id procurado
    Optional<LocalUso> optionalLocalUso = repository.findById(data.codigoLocalUsoObra());
    // .isPresent() => Caso exista algum valor em optionalLocalUso {...faça isso...}
    if (optionalLocalUso.isPresent()) {
      // .get() => Retorna o valor
      LocalUso localUso = optionalLocalUso.get();
      // Seta o nomeLocalUsoObra por meio do setter definido na Model
      localUso.setNomeLocalUsoObra(data.nomeLocalUsoObra());
      // Retorna a resposta em fora de ResponseEntity
      return ResponseEntity.ok(localUso);
    } else {
      // Caso algo de errado, trata o erro
      throw new EntityNotFoundException();
    }
  }


  // Deleta um localUso, passando codigoLocalUsoObra e nomeLocalUsoObra
  @DeleteMapping  // cruD - Delete
  public void deletaLocalUso(@RequestBody LocalUso localUso) {
    // Não tem retorno. Usa as informações do corpo da requisição, espera receber um LocalUso
    // Deleta o localUso recebido por parametro
    repository.delete(localUso);
  }

}
