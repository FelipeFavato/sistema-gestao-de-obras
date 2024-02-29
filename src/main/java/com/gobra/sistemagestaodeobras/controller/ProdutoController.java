package com.gobra.sistemagestaodeobras.controller;

import java.util.List;

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
import com.gobra.sistemagestaodeobras.service.ProdutoService;



@RestController
@RequestMapping("/api/produto")
public class ProdutoController {

  @Autowired
  private ProdutoService produtoService;


  @GetMapping
  public List<ProdutoResponseDTO> getAll () {
    return produtoService.getAllProduto();
  }

  @PostMapping
  public ResponseEntity<?> saveOne (@RequestBody ProdutoRequestDTO data) {
    return produtoService.saveProduto(data);
  }

  @PutMapping
  @Transactional
  public ResponseEntity<Produto> updateOne (@RequestBody ProdutoRequestDTO data) {
    return produtoService.updateProduto(data);
  }

  @DeleteMapping
  public ResponseEntity<?> deleteOne(@RequestBody Produto produto) {
    return produtoService.deleteProduto(produto);
  }
}
