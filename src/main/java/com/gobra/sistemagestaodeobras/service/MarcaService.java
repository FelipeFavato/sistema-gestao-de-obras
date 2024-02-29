package com.gobra.sistemagestaodeobras.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.gobra.sistemagestaodeobras.dto.MarcaRequestDTO;
import com.gobra.sistemagestaodeobras.dto.MarcaResponseDTO;
import com.gobra.sistemagestaodeobras.exceptionHandler.CustomErrorMessage;
import com.gobra.sistemagestaodeobras.exceptionHandler.dto.CodXCodYDTO;
import com.gobra.sistemagestaodeobras.model.Marca;
import com.gobra.sistemagestaodeobras.repository.MarcaRepository;

import jakarta.persistence.EntityNotFoundException;


@Service
public class MarcaService {
  
  @Autowired
  private MarcaRepository marcaRepository;


  // GET
  public List<MarcaResponseDTO> getAllMarca () {
    return marcaRepository.findAll().stream().map(MarcaResponseDTO::new).toList();
  }

  // INSERT
  public ResponseEntity<?> saveMarca (@RequestBody MarcaRequestDTO data) {

    Optional<List<Marca>> optionalMarca = marcaRepository.findByNome(data.nome());
    if (!optionalMarca.get().isEmpty()) {
      String resposta = "Não foi possível INSERIR, pois a Marca/Empresa: '" + data.nome() + "' já existe!";
      Integer statusCode = 409;
      CustomErrorMessage customErrorMessage = new CustomErrorMessage(HttpStatus.CONFLICT, statusCode, resposta);
      return new ResponseEntity<>(customErrorMessage, HttpStatus.CONFLICT);
    }
  
    Marca marca = new Marca(data);
    marcaRepository.save(marca);
    return new ResponseEntity<Marca>(marca, HttpStatus.CREATED);
  }

  // UPDATE
  public ResponseEntity<Marca> updateMarca (@RequestBody MarcaRequestDTO data) {
    Optional<Marca> optionalMarca = marcaRepository.findById(data.codigo());

    if (optionalMarca.isPresent()) {
      Marca marca = optionalMarca.get();
      marca.setNome(data.nome());
      marca.setDataDesativacao(data.dataDesativacao());

      return ResponseEntity.ok(marca);
    } else {
      throw new EntityNotFoundException();
    }
  }

  // DELETE
  public ResponseEntity<?> deleteMarca (@RequestBody Marca data) {
    Marca marca = marcaRepository.getReferenceById(data.getCodigo());
    String nomeMarca = marca.getNome();

    List<CodXCodYDTO> listaMarca = marcaRepository.obterListaMarcasComProdutosVinculados(data.getCodigo()).stream().map(CodXCodYDTO::new).toList();
    if (!listaMarca.isEmpty()) {
      String resposta = "Não foi possível DELETAR '" + nomeMarca + "', pois há PRODUTOS vinculados!";
      Integer statusCode = 422;
      CustomErrorMessage customErrorMessage = new CustomErrorMessage(HttpStatus.UNPROCESSABLE_ENTITY, statusCode, resposta);
      return new ResponseEntity<>(customErrorMessage, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    marcaRepository.delete(data);
    return new ResponseEntity<>("Marca/Empresa: '" + nomeMarca + "' DELETADA com sucesso.", HttpStatus.OK);
  }
}
