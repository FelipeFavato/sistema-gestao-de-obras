package com.gobra.sistemagestaodeobras.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.gobra.sistemagestaodeobras.dto.ProdutoRequestDTO;
import com.gobra.sistemagestaodeobras.dto.ProdutoResponseDTO;
import com.gobra.sistemagestaodeobras.exceptionHandler.CustomErrorMessage;
import com.gobra.sistemagestaodeobras.exceptionHandler.dto.CodXCodYDTO;
import com.gobra.sistemagestaodeobras.model.Produto;
import com.gobra.sistemagestaodeobras.repository.ItemCompraRepository;
import com.gobra.sistemagestaodeobras.repository.ProdutoRepository;

import jakarta.persistence.EntityNotFoundException;


@Service
public class ProdutoService {
  
  @Autowired
  ProdutoRepository produtoRepository;

  @Autowired
  ItemCompraRepository itemCompraRepository;

  // GET
  public List<ProdutoResponseDTO> getAllProduto () {
    return produtoRepository.findAll().stream().map(ProdutoResponseDTO::new).toList();
  }

  // INSERT
  public ResponseEntity<?> saveProduto (@RequestBody ProdutoRequestDTO data) {

    Optional<List<Produto>> optionalProduto = produtoRepository.findByNome(data.nome());
    if (!optionalProduto.get().isEmpty()) {
      String resposta = "Não foi possível INSERIR, pois o Produto: '" + data.nome() + "' já existe!";
      Integer statusCode = 409;
      CustomErrorMessage customErrorMessage = new CustomErrorMessage(HttpStatus.CONFLICT, statusCode, resposta);
      return new ResponseEntity<>(customErrorMessage, HttpStatus.CONFLICT);
    }

    Produto produto = new Produto(data);
    produtoRepository.save(produto);
    return new ResponseEntity<Produto>(produto, HttpStatus.CREATED);
  }

  // UPDATE
  public ResponseEntity<Produto> updateProduto (@RequestBody ProdutoRequestDTO data) {
    Optional<Produto> optionalProduto = produtoRepository.findById(data.codigo());

    if (optionalProduto.isPresent()) {
      Produto produto = optionalProduto.get();
      produto.setNome(data.nome());
      produto.setTipoProduto(data.tipoProduto());
      produto.setMarca(data.marca());

      return ResponseEntity.ok(produto);
    } else {
      throw new EntityNotFoundException();
    }
  }

  // DELETE
  public ResponseEntity<?> deleteProduto (@RequestBody Produto data) {
    Produto produto = produtoRepository.getReferenceById(data.getCodigo());
    String nomeProduto = produto.getNome();

    List<CodXCodYDTO> listaProdutos = produtoRepository.obterListaProdutosComItensVinculados(data.getCodigo()).stream().map(CodXCodYDTO::new).toList();
    if (!listaProdutos.isEmpty()) {
      String resposta = "Não foi possível DELETAR '" + nomeProduto + "', pois há ITENS DE COMPRA vinculados!";
      Integer statusCode = 422;
      CustomErrorMessage customErrorMessage = new CustomErrorMessage(HttpStatus.UNPROCESSABLE_ENTITY, statusCode, resposta);
      return new ResponseEntity<>(customErrorMessage, HttpStatus.UNPROCESSABLE_ENTITY);
    }
  
    produtoRepository.delete(data);
    return new ResponseEntity<>("Produto: '" + nomeProduto + "' DELETADO com sucesso.", HttpStatus.OK);
  }
}
