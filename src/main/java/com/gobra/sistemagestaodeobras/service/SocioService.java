package com.gobra.sistemagestaodeobras.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.gobra.sistemagestaodeobras.dto.SocioRequestDTO;
import com.gobra.sistemagestaodeobras.dto.SocioResponseDTO;
import com.gobra.sistemagestaodeobras.exceptionHandler.CustomErrorMessage;
import com.gobra.sistemagestaodeobras.exceptionHandler.dto.CodXCodYDTO;
import com.gobra.sistemagestaodeobras.model.Socio;
import com.gobra.sistemagestaodeobras.repository.SocioRepository;

import jakarta.persistence.EntityNotFoundException;



@Service
public class SocioService {
  
  @Autowired
  private SocioRepository socioRepository;


  // GET
  public List<SocioResponseDTO> getAllSocios () {
    return socioRepository.findAll().stream().map(SocioResponseDTO::new).toList();
  }

  // INSERT
  public ResponseEntity<?> saveSocio (@RequestBody SocioRequestDTO data) {

    Optional<List<Socio>> optionalSocio = socioRepository.findByNome(data.nome());
    if (!optionalSocio.get().isEmpty()) {
      String resposta = "Não foi possível INSERIR, pois o(a) Sócio(a) '" + data.nome() + "' já existe!";
      Integer statusCode = 409;
      CustomErrorMessage customErrorMessage = new CustomErrorMessage(HttpStatus.CONFLICT, statusCode, resposta);
      return new ResponseEntity<>(customErrorMessage, HttpStatus.CONFLICT);
    }

    Socio socio = new Socio(data);
    socioRepository.save(socio);
    return new ResponseEntity<Socio>(socio, HttpStatus.CREATED);
  }

  // UPDATE
  public ResponseEntity<Socio> updateSocio(@RequestBody SocioRequestDTO data) {
    Optional<Socio> optionalSocio = socioRepository.findById(data.codigo());

    if (optionalSocio.isPresent()) {
      Socio socio = optionalSocio.get();
      socio.setNome(data.nome());
      socio.setDataDesativacao(data.dataDesativacao());
      socio.setTelegramID(data.telegramID());
      // socio.setObras(data.obras());

      return ResponseEntity.ok(socio);
    } else {
      throw new EntityNotFoundException();
    }
  }

  // DELETE
  public ResponseEntity<?> deletaSocio (@RequestBody Socio data) {
    Socio socio = socioRepository.getReferenceById(data.getCodigo());
    String nomeSocio = socio.getNome();

    List<CodXCodYDTO> listaSocioObras = socioRepository.obterSocioQueEstejaAtribuidoAAlgumaObra(data.getCodigo()).stream().map(CodXCodYDTO::new).toList();
    if (!listaSocioObras.isEmpty()) {
      String resposta = "Não foi possível DELETAR '" + nomeSocio + "', pois há OBRAS vinculadas!";
      Integer statusCode = 422;
      CustomErrorMessage customErrorMessage = new CustomErrorMessage(HttpStatus.UNPROCESSABLE_ENTITY, statusCode, resposta);
      return new ResponseEntity<>(customErrorMessage, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    // Compras vinculadas, porém para vincular uma compra, é necessário primeiro vincular a uma obra.
    List<CodXCodYDTO> listaSocioCompras = socioRepository.obterSocioQueTenhaComprasVinculadas(data.getCodigo()).stream().map(CodXCodYDTO::new).toList();
    if (!listaSocioCompras.isEmpty()) {
      String resposta = "Não foi possível DELETAR '" + nomeSocio + "', pois há COMPRAS vinculadas!";
      Integer statusCode = 422;
      CustomErrorMessage customErrorMessage = new CustomErrorMessage(HttpStatus.UNPROCESSABLE_ENTITY, statusCode, resposta);
      return new ResponseEntity<>(customErrorMessage, HttpStatus.UNPROCESSABLE_ENTITY); 
    }


    socioRepository.delete(data);
    return new ResponseEntity<>("Sócio(a): '" + nomeSocio + "' DELETADO(A) com sucesso.", HttpStatus.OK);
  }
}
