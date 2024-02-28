package com.gobra.sistemagestaodeobras.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.gobra.sistemagestaodeobras.dto.LocalUsoRequestDTO;
import com.gobra.sistemagestaodeobras.dto.LocalUsoResponseDTO;
import com.gobra.sistemagestaodeobras.exceptionHandler.CustomErrorMessage;
import com.gobra.sistemagestaodeobras.exceptionHandler.dto.CodItemCodNomeLocalDTO;
import com.gobra.sistemagestaodeobras.model.LocalUso;
import com.gobra.sistemagestaodeobras.repository.ItemCompraRepository;
import com.gobra.sistemagestaodeobras.repository.LocalUsoRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class LocalUsoService {
  
  @Autowired
  LocalUsoRepository localUsoRepository;

  @Autowired
  ItemCompraRepository itemCompraRepository;

  // GET-------------------------------------------------------------
  public List<LocalUsoResponseDTO> getAllLocalUso () {
    // Como transformar a entidade criada em um DTO?
    // stream(): Pega tudo que vem do repository.findAll() e coloca dentro de um funil.
    // map(x::new): Para cada objeto dentro desse funil, cria um DTO.
    // toList(): Transforma em uma List.
    return localUsoRepository.findAll().stream().map(LocalUsoResponseDTO::new).toList();
  }

  // INSERT ----------------------------------------------------------
  public ResponseEntity<?> saveLocalUso(@RequestBody LocalUsoRequestDTO data) {

    Optional<List<LocalUso>> optionalLocalUso = localUsoRepository.findByNomeLocalUsoObra(data.nomeLocalUsoObra());
    if (!optionalLocalUso.get().isEmpty()) {
      String resposta = "Não foi possível INSERIR, pois o Local de Uso: '" + data.nomeLocalUsoObra() + "' já existe.";
      Integer statusCode = 409;
      CustomErrorMessage customErrorMessage = new CustomErrorMessage(HttpStatus.CONFLICT, statusCode, resposta);
      return new ResponseEntity<>(customErrorMessage, HttpStatus.CONFLICT);
    }

    LocalUso localUsoData = new LocalUso(data);
    localUsoRepository.save(localUsoData);
    return new ResponseEntity<LocalUso>(localUsoData, HttpStatus.CREATED);
  }

  // UPDATE
  public ResponseEntity<LocalUso> updateLocalUso(@RequestBody LocalUsoRequestDTO data){
    // ResponseEntity => Representa a resposta HTTP como um todo (status code, headers e body)
    // Optional permite ter um valor presente ou não, pois estou procurando pelo ID e pode não ter o id procurado
    Optional<LocalUso> optionalLocalUso = localUsoRepository.findById(data.codigoLocalUsoObra());

    // .isPresent() => Caso exista algum valor em optionalLocalUso {...faça isso...}
    if (optionalLocalUso.isPresent()) {
      // .get() => Retorna o valor
      LocalUso localUso = optionalLocalUso.get();
      // Seta o nomeLocalUsoObra por meio do setter definido na Model
      localUso.setNomeLocalUsoObra(data.nomeLocalUsoObra());
      localUso.setDataDesativacao(data.dataDesativacao());
      // Retorna a resposta em fora de ResponseEntity
      return ResponseEntity.ok(localUso);
    } else {
      // Caso algo de errado, trata o erro
      throw new EntityNotFoundException();
    }
  }

  // DELETE
  public ResponseEntity<?> deletaLocalUso (@RequestBody LocalUso data) {
    LocalUso localUso = localUsoRepository.getReferenceById(data.getCodigoLocalUsoObra());
    String nomeLocalUso = localUso.getNomeLocalUsoObra();

    List<CodItemCodNomeLocalDTO> listaItensLocais = itemCompraRepository.obterListaDeLocaisDeUsoAssociadosAItens(data.getCodigoLocalUsoObra()).stream().map(CodItemCodNomeLocalDTO::new).toList();
    if (!listaItensLocais.isEmpty()) {
      String resposta = "Não foi possível DELETAR '" + localUso.getNomeLocalUsoObra() + "', pois há ITENS DE COMPRA vinculados!";
      Integer statusCode = 422;
      CustomErrorMessage customErrorMessage = new CustomErrorMessage(HttpStatus.UNPROCESSABLE_ENTITY, statusCode, resposta);
      return new ResponseEntity<>(customErrorMessage, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    localUsoRepository.delete(data);
    return new ResponseEntity<>("Local de Uso: '"+ nomeLocalUso + "' DELETADO(A) com sucesso.", HttpStatus.OK);
  }

}
