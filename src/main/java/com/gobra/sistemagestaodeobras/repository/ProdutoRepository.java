package com.gobra.sistemagestaodeobras.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gobra.sistemagestaodeobras.model.Produto;

public interface ProdutoRepository extends JpaRepository<Produto, Integer> {
  
}
