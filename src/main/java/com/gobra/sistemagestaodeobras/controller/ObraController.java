package com.gobra.sistemagestaodeobras.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gobra.sistemagestaodeobras.dto.AssociacaoSocioObraDTO;
import com.gobra.sistemagestaodeobras.dto.ObraRequestDTO;
import com.gobra.sistemagestaodeobras.dto.ObraResponseDTO;
import com.gobra.sistemagestaodeobras.exceptionHandler.CustomErrorMessage;
import com.gobra.sistemagestaodeobras.exceptionHandler.dto.CodXCodYDTO;
import com.gobra.sistemagestaodeobras.model.Obra;
import com.gobra.sistemagestaodeobras.repository.CompraRepository;
import com.gobra.sistemagestaodeobras.repository.ObraRepository;
import com.gobra.sistemagestaodeobras.service.ObraService;

import jakarta.persistence.EntityNotFoundException;



@RestController
@RequestMapping("/api/obra")
public class ObraController {
  
  @Autowired
  private ObraRepository repository;

  @Autowired
  private CompraRepository compraRepository;

  @Autowired
  private ObraService obraService;


  @GetMapping
  public List<ObraResponseDTO> getAll() {
    List<ObraResponseDTO> obrasList = repository.findAll().stream().map(ObraResponseDTO::new).toList();
    return obrasList;
  }

  @PostMapping
  public ResponseEntity<?> saveObra(@RequestBody ObraRequestDTO data) {

    Optional<List<Obra>> optionalObra = repository.findByNome(data.nome());
    if (!optionalObra.get().isEmpty()) {
      String resposta = "Não foi possível INSERIR, pois a Obra: '" + data.nome() + "' já existe";
      Integer statusCode = 409;
      CustomErrorMessage customErrorMessage = new CustomErrorMessage(HttpStatus.CONFLICT, statusCode, resposta);
      return new ResponseEntity<>(customErrorMessage, HttpStatus.CONFLICT);
    }
    
    Obra obraData = new Obra(data);
    repository.save(obraData);
    return new ResponseEntity<Obra>(obraData, HttpStatus.CREATED);
  }

  @PutMapping
  @Transactional
  public ResponseEntity<Obra> updateObra(@RequestBody ObraRequestDTO data) {
    Optional<Obra> optionalObra = repository.findById(data.codigo());

    if (optionalObra.isPresent()) {
      Obra obra = optionalObra.get();
      obra.setNome(data.nome());
      obra.setEndereco(data.endereco());
      obra.setDataInicio(data.dataInicio());
      obra.setDataPrevistaFim(data.dataPrevistaFim());
      obra.setDataRealFim(data.dataRealFim());
      obra.setCustoMaoDeObra(data.custoMaoDeObra());
      obra.setCustoPrevisto(data.custoPrevisto());
      obra.setCompras(data.compras());
      // obra.setPrevisoes(data.previsoes());
      // obra.setSocios(data.socios());
      // obra.setArquivos(data.arquivos());

      return ResponseEntity.ok(obra);
    } else {
      throw new EntityNotFoundException();
    }
  }

  @DeleteMapping
  public ResponseEntity<?> deletaCompra(@RequestBody Obra data) {
    Obra obra = repository.getReferenceById(data.getCodigo());
    String nomeObra = obra.getNome();

    List<CodXCodYDTO> listaObraCompras = compraRepository.obterObraComComprasCadastradas(data.getCodigo()).stream().map(CodXCodYDTO::new).toList();
    if (!listaObraCompras.isEmpty()) {
      String resposta = "Não foi possível DELETAR '" + nomeObra + "', pois há COMPRAS vinculadas!";
      Integer statusCode = 422;
      CustomErrorMessage customErrorMessage = new CustomErrorMessage(HttpStatus.UNPROCESSABLE_ENTITY, statusCode, resposta);
      return new ResponseEntity<>(customErrorMessage, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    repository.delete(obra);
    return new ResponseEntity<>("Obra: '" + nomeObra + "' DELETADA com sucesso.", HttpStatus.OK);
  }

  // Associar sócio a uma obra
  @PostMapping("/associar-socio-obra")
  public ResponseEntity<String> associarSocioObra(@RequestBody AssociacaoSocioObraDTO associacaoDTO) {
    try {
      obraService.associarSocioAObra(associacaoDTO.getIdSocio(), associacaoDTO.getIdObra());
      return ResponseEntity.ok("Associação realizada com sucesso!");
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body("Erro ao realizar a associação: " + e.getMessage());
    }
  }

  // Desassociar um sócio a uma obra
  @PostMapping("/desassociar-socio-obra")
  public ResponseEntity<String> desassociarSocioObra(@RequestBody AssociacaoSocioObraDTO desassociacaoDTO) {
    try {
      obraService.desassociarSocioDeObra(desassociacaoDTO.getIdSocio(), desassociacaoDTO.getIdObra());
      return ResponseEntity.ok("Desassociação realizada com sucesso!");
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body("Erro ao realizar a desassociação: " + e.getMessage());
    }
  }
}
