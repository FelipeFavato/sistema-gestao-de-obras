package com.gobra.sistemagestaodeobras.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gobra.sistemagestaodeobras.model.Fornecedor;
import java.util.List;
import java.util.Optional;


public interface FornecedorRepository extends JpaRepository<Fornecedor, Integer> {

  Optional<List<Fornecedor>> findByNome(String nome);

}
