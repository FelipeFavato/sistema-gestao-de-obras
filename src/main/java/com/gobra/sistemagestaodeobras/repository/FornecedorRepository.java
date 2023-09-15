package com.gobra.sistemagestaodeobras.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gobra.sistemagestaodeobras.model.Fornecedor;

public interface FornecedorRepository extends JpaRepository<Fornecedor, Integer> {
  
}
