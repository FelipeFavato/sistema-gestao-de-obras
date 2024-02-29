package com.gobra.sistemagestaodeobras.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.gobra.sistemagestaodeobras.dto.FornecedorRequestDTO;
import com.gobra.sistemagestaodeobras.dto.FornecedorResponseDTO;
import com.gobra.sistemagestaodeobras.exceptionHandler.CustomErrorMessage;
import com.gobra.sistemagestaodeobras.exceptionHandler.dto.CodXCodYDTO;
import com.gobra.sistemagestaodeobras.model.Fornecedor;
import com.gobra.sistemagestaodeobras.repository.CompraRepository;
import com.gobra.sistemagestaodeobras.repository.FornecedorRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class FornecedorService {
  
  @Autowired
  FornecedorRepository fornecedorRepository;

  @Autowired
  CompraRepository compraRepository;


  // GET
  public List<FornecedorResponseDTO> getAllFornecedores () {
    return fornecedorRepository.findAll().stream().map(FornecedorResponseDTO::new).toList();
  }

  // INSERT
  public ResponseEntity<?> saveFornecedor (@RequestBody FornecedorRequestDTO data) {

    Optional<List<Fornecedor>> optionalFornecedor = fornecedorRepository.findByNome(data.nome());
    if (!optionalFornecedor.get().isEmpty()) {
      String resposta = "Não foi possível INSERIR, pois o Fornecedor: '" + data.nome() + "' já existe.";
      Integer statusCode = 409;
      CustomErrorMessage customErrorMessage = new CustomErrorMessage(HttpStatus.CONFLICT, statusCode, resposta);
      return new ResponseEntity<>(customErrorMessage, HttpStatus.CONFLICT);
    }

    Fornecedor fornecedorData = new Fornecedor(data);
    fornecedorRepository.save(fornecedorData);
    return new ResponseEntity<Fornecedor>(fornecedorData, HttpStatus.CREATED);
  }

  // UPDATE
  public ResponseEntity<Fornecedor> updateFornecedor(@RequestBody FornecedorRequestDTO data) {
    Optional<Fornecedor> optionalFornecedor = fornecedorRepository.findById(data.codigo());

    if (optionalFornecedor.isPresent()) {
      Fornecedor fornecedor = optionalFornecedor.get();
      fornecedor.setNome(data.nome());
      fornecedor.setTelefone(data.telefone());
      fornecedor.setEndereco(data.endereco());
      fornecedor.setEmail(data.email());
      fornecedor.setTipoFornecedor(data.tipoFornecedor());

      return ResponseEntity.ok(fornecedor);
    } else {
      throw new EntityNotFoundException();
    }
  }

  // DELETE
  public ResponseEntity<?> deletaFornecedor (@RequestBody Fornecedor data) {
    Fornecedor fornecedor = fornecedorRepository.getReferenceById(data.getCodigo());
    String nomeFornecedor = fornecedor.getNome();

    List<CodXCodYDTO> listaComprasComFornecedores = compraRepository.obterListaComprasComFornecedoresVinculados(data.getCodigo()).stream().map(CodXCodYDTO::new).toList();
    if (!listaComprasComFornecedores.isEmpty()) {
      String resposta = "Não foi possível DELETAR '" + nomeFornecedor + "', pois há COMPRAS vinculadas!";
      Integer statusCode = 422;
      CustomErrorMessage customErrorMessage = new CustomErrorMessage(HttpStatus.UNPROCESSABLE_ENTITY, statusCode, resposta);
      return new ResponseEntity<>(customErrorMessage, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    fornecedorRepository.delete(data);
    return new ResponseEntity<>("Fornecedor: '" + nomeFornecedor + "' DELETADO com sucesso.", HttpStatus.OK);
  
  }
}
