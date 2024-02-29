package com.gobra.sistemagestaodeobras.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.gobra.sistemagestaodeobras.dto.UnidadeMedidaRequestDTO;
import com.gobra.sistemagestaodeobras.dto.UnidadeMedidaResponseDTO;
import com.gobra.sistemagestaodeobras.exceptionHandler.CustomErrorMessage;
import com.gobra.sistemagestaodeobras.exceptionHandler.dto.CodXCodYDTO;
import com.gobra.sistemagestaodeobras.model.UnidadeMedida;
import com.gobra.sistemagestaodeobras.repository.UnidadeMedidaRepository;

import jakarta.persistence.EntityNotFoundException;


@Service
public class UnidadeMedidaService {
  
  @Autowired
  private UnidadeMedidaRepository unidadeMedidaRepository;


  // GET
  public List<UnidadeMedidaResponseDTO> getAllUnidades () {
    return unidadeMedidaRepository.findAll().stream().map(UnidadeMedidaResponseDTO::new).toList();
  }

  // INSERT
  public ResponseEntity<?> saveUnidadeMedida (@RequestBody UnidadeMedidaRequestDTO data) {

    Optional<List<UnidadeMedida>> optionalUnidade = unidadeMedidaRepository.findByUnidade(data.unidade());
    if (!optionalUnidade.get().isEmpty()) {
      String resposta = "Não foi possível INSERIR, pois a Unidade de medida: '" + data.unidade() + "' já existe!";
      Integer statusCode = 409;
      CustomErrorMessage customErrorMessage = new CustomErrorMessage(HttpStatus.CONFLICT, statusCode, resposta);
      return new ResponseEntity<>(customErrorMessage, HttpStatus.CONFLICT);
    }
    
    UnidadeMedida unidadeMedida = new UnidadeMedida(data);
    unidadeMedidaRepository.save(unidadeMedida);
    return new ResponseEntity<UnidadeMedida>(unidadeMedida, HttpStatus.CREATED);
  }

  // UPDATE
  public ResponseEntity<UnidadeMedida> updateUnidadeMedida(@RequestBody UnidadeMedidaRequestDTO data) {
    Optional<UnidadeMedida> optionalUnidadeMedida = unidadeMedidaRepository.findById(data.codigo());
  
    if (optionalUnidadeMedida.isPresent()) {
      UnidadeMedida unidadeMedida = optionalUnidadeMedida.get();
      unidadeMedida.setUnidade(data.unidade());

      return ResponseEntity.ok(unidadeMedida);
    } else {
      throw new EntityNotFoundException();
    }
  }

  // DELETE
  public ResponseEntity<?> deleteUnidade (@RequestBody UnidadeMedida data) {
    UnidadeMedida unidadeMedida = unidadeMedidaRepository.getReferenceById(data.getCodigo());
    String nomeUnidadeMedida = unidadeMedida.getUnidade();

    List<CodXCodYDTO> listaUnidadeProdutos = unidadeMedidaRepository.obterMarcasQueTenhamProdutosVinculados(data.getCodigo()).stream().map(CodXCodYDTO::new).toList();
    if (!listaUnidadeProdutos.isEmpty()) {
      String resposta = "Não foi possível DELETAR '" + nomeUnidadeMedida + "', pois há ITENS DE COMPRA vinculados";
      Integer statusCode = 422;
      CustomErrorMessage customErrorMessage = new CustomErrorMessage(HttpStatus.UNPROCESSABLE_ENTITY, statusCode, resposta);
      return new ResponseEntity<>(customErrorMessage, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    unidadeMedidaRepository.delete(data);
    return new ResponseEntity<>("Unidade de medida: '" + nomeUnidadeMedida + "' DELETADA com sucesso.", HttpStatus.OK);
  }

}
