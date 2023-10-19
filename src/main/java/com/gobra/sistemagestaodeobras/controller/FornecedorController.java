package com.gobra.sistemagestaodeobras.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gobra.sistemagestaodeobras.dto.FornecedorRequestDTO;
import com.gobra.sistemagestaodeobras.dto.FornecedorResponseDTO;
import com.gobra.sistemagestaodeobras.model.Fornecedor;
import com.gobra.sistemagestaodeobras.repository.FornecedorRepository;

import jakarta.persistence.EntityNotFoundException;



@RestController
@RequestMapping("/api/fornecedor")
public class FornecedorController {

  @Autowired
  private FornecedorRepository repository;

  @PostMapping
  public void saveFornecedor(@RequestBody FornecedorRequestDTO data) {
    Fornecedor fornecedorData = new Fornecedor(data);
    repository.save(fornecedorData);
  }

  @GetMapping
  public List<FornecedorResponseDTO> getAll() {
    List<FornecedorResponseDTO> FornecedorList = repository.findAll().stream().map(FornecedorResponseDTO::new).toList();
    return FornecedorList;
  }

  @PutMapping
  @Transactional
  public ResponseEntity<Fornecedor> updateFornecedor(@RequestBody FornecedorRequestDTO data) {
    Optional<Fornecedor> optionalFornecedor = repository.findById(data.codigo());

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

  // O RequestBody pode conter só o ID, ou um Fornecedor inteiro
  @DeleteMapping
  public void deletaFornecedor(@RequestBody Fornecedor fornecedor) {
    repository.delete(fornecedor);
  }
}
