package com.gobra.sistemagestaodeobras.controller;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gobra.sistemagestaodeobras.dto.CompraRequestDTO;
import com.gobra.sistemagestaodeobras.dto.CompraResponseDTO;
import com.gobra.sistemagestaodeobras.model.Compra;
import com.gobra.sistemagestaodeobras.repository.CompraRepository;

import jakarta.persistence.EntityNotFoundException;

@RestController
@RequestMapping("/api/compra")
public class CompraController {

  @Autowired
  private CompraRepository compraRepository;

  @PostMapping
  public void saveCompra(@RequestBody CompraRequestDTO data) {
    Compra compraData = new Compra(data);
    compraRepository.save(compraData);
  }

  @GetMapping
  public List<CompraResponseDTO> getAll() {
    List<CompraResponseDTO> comprasList = compraRepository.findAll().stream().map(CompraResponseDTO::new).toList();
    return comprasList;
  }

  @PutMapping
  @Transactional
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

  @DeleteMapping
  public void deletaCompra(@RequestBody Compra compra) {
    compraRepository.delete(compra);
  }

  @GetMapping("/teste")
  public String principal (Principal principal) {

    // System.out.println(principal);

    String retorno = principal.toString();

    return retorno;

  }
}
