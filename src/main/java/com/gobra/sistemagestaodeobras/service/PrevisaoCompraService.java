package com.gobra.sistemagestaodeobras.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.gobra.sistemagestaodeobras.dto.PrevisaoCompraRequestDTO;
import com.gobra.sistemagestaodeobras.dto.PrevisaoCompraResponseDTO;
import com.gobra.sistemagestaodeobras.model.PrevisaoCompra;
import com.gobra.sistemagestaodeobras.repository.PrevisaoCompraRepository;

import jakarta.persistence.EntityNotFoundException;



@Service
public class PrevisaoCompraService {

  @Autowired
  private PrevisaoCompraRepository previsaoCompraRepository;


  // GET
  public List<PrevisaoCompraResponseDTO> getAllPrevisoes () {
    return previsaoCompraRepository.findAll().stream().map(PrevisaoCompraResponseDTO::new).toList();
  }

  // INSERT
  public ResponseEntity<PrevisaoCompra> savePrevisaoCompra (@RequestBody PrevisaoCompraRequestDTO data) {
    PrevisaoCompra previsaoCompra = new PrevisaoCompra(data);
    previsaoCompraRepository.save(previsaoCompra);

    return new ResponseEntity<PrevisaoCompra>(previsaoCompra, HttpStatus.CREATED);
  }

  // UPDATE
  public ResponseEntity<PrevisaoCompra> updatePrevisaoCompra (@RequestBody PrevisaoCompraRequestDTO data) {
    Optional<PrevisaoCompra> optionalPrevisaoCompra = previsaoCompraRepository.findById(data.codigo());

    if (optionalPrevisaoCompra.isPresent()) {
      PrevisaoCompra previsaoCompra = optionalPrevisaoCompra.get();
      previsaoCompra.setCodigoObra(data.codigoObra());
      previsaoCompra.setDescricaoProdutoServico(data.descricaoProdutoServico());
      previsaoCompra.setQuantidade(data.quantidade());
      previsaoCompra.setValorUnitario(data.valorUnitario());
      previsaoCompra.setValorTotalPrevisto(data.valorTotalPrevisto());
      previsaoCompra.setCompraPrevistaRealizada(data.compraPrevistaRealizada());

      return ResponseEntity.ok(previsaoCompra);
    } else {
      throw new EntityNotFoundException();
    }
  }

  // DELETE
  public ResponseEntity<?> deletaPrevisaoCompra (@RequestBody PrevisaoCompra data) {
    PrevisaoCompra previsaoCompra = previsaoCompraRepository.getReferenceById(data.getCodigo());
    String nomePrevisao = previsaoCompra.getDescricaoProdutoServico();

    previsaoCompraRepository.delete(data);

    return new ResponseEntity<> ("Previsão de custo: '" + nomePrevisao + "' DELETADA com sucesso.", HttpStatus.OK);
  }

}
