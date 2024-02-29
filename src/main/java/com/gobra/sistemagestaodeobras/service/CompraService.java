package com.gobra.sistemagestaodeobras.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.gobra.sistemagestaodeobras.dto.CompraRequestDTO;
import com.gobra.sistemagestaodeobras.dto.CompraResponseDTO;
import com.gobra.sistemagestaodeobras.exceptionHandler.CustomErrorMessage;
import com.gobra.sistemagestaodeobras.exceptionHandler.dto.CodXCodYDTO;
import com.gobra.sistemagestaodeobras.model.Compra;
import com.gobra.sistemagestaodeobras.repository.CompraRepository;

import jakarta.persistence.EntityNotFoundException;


@Service
public class CompraService {
  
  @Autowired
  private CompraRepository compraRepository;


  // GET
  public List<CompraResponseDTO> getAllCompras () {
    return compraRepository.findAll().stream().map(CompraResponseDTO::new).toList();
  }

  // INSERT
  public ResponseEntity<Compra> saveCompra(@RequestBody CompraRequestDTO data) {
    Compra compraData = new Compra(data);
    compraRepository.save(compraData);
    return new ResponseEntity<Compra>(compraData, HttpStatus.CREATED);
  }
  // UPDATE
  public ResponseEntity<Compra> updateCompra(@RequestBody CompraRequestDTO data) {
    Optional<Compra> optionalCompra = compraRepository.findById(data.codigo());

    if (optionalCompra.isPresent()) {
      Compra compra = optionalCompra.get();
      compra.setObra(data.obra());
      compra.setFornecedor(data.fornecedor());
      compra.setSocio(data.socio());
      compra.setDataCompra(data.dataCompra());
      compra.setDataEntrega(data.dataEntrega());
      compra.setDataPagamento(data.dataPagamento());
      compra.setDataVencimento(data.dataVencimento());
      compra.setValorOriginal(data.valorOriginal());
      compra.setValorDesconto(data.valorDesconto());
      compra.setValorFinal(data.valorFinal());

      return ResponseEntity.ok(compra);
    } else {
      throw new EntityNotFoundException();
    }
  }

  // DELETE
  public ResponseEntity<?> deletaCompra(@RequestBody Compra data) {
    Compra compra = compraRepository.getReferenceById(data.getCodigo());
    String fornecedorCompra = compra.getFornecedor().getNome();
    double valorCompra = compra.getValorFinal();

    List<CodXCodYDTO> listaCompraItens = compraRepository.obterListaDeCompraComItens(data.getCodigo()).stream().map(CodXCodYDTO::new).toList();
    if (!listaCompraItens.isEmpty()) {
      String resposta = "Não foi possível DELETAR a Compra de R$" + valorCompra + " feita em '" + fornecedorCompra + "', pois há ITENS vinculados.";
      Integer statusCode = 422;
      CustomErrorMessage customErrorMessage = new CustomErrorMessage(HttpStatus.UNPROCESSABLE_ENTITY, statusCode, resposta);
      return new ResponseEntity<>(customErrorMessage, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    compraRepository.delete(data);
    return new ResponseEntity<>("Compra DELETADA com sucesso.", HttpStatus.OK);
  }
}
