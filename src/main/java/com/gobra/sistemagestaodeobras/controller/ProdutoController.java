package com.gobra.sistemagestaodeobras.controller;

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

import com.gobra.sistemagestaodeobras.dto.ProdutoRequestDTO;
import com.gobra.sistemagestaodeobras.dto.ProdutoResponseDTO;
import com.gobra.sistemagestaodeobras.model.Produto;
import com.gobra.sistemagestaodeobras.repository.ProdutoRepository;

import jakarta.persistence.EntityNotFoundException;




@RestController
@RequestMapping("/api/produto")
public class ProdutoController {
  
  @Autowired
  private ProdutoRepository repository;

  @PostMapping
  public void saveProduto(@RequestBody ProdutoRequestDTO data) {
    Produto produtoData = new Produto(data);
    repository.save(produtoData);
  }

  @GetMapping
  public List<ProdutoResponseDTO> getAll() {
    List<ProdutoResponseDTO> produtoList = repository.findAll().stream().map(ProdutoResponseDTO::new).toList();
    return produtoList;
  }

  @PutMapping
  @Transactional
  public ResponseEntity<Produto> updateProduto(@RequestBody ProdutoRequestDTO data) {
    Optional<Produto> optionalProduto = repository.findById(data.codigo());

    if (optionalProduto.isPresent()) {
      Produto produto = optionalProduto.get();
      produto.setNome(data.nome());
      produto.setTipoProduto(data.tipoProduto());

      return ResponseEntity.ok(produto);
    } else {
      throw new EntityNotFoundException();
    }
  }

  @DeleteMapping
  public void deletaProduto(@RequestBody Produto produto) {
    repository.delete(produto);
  }
}
