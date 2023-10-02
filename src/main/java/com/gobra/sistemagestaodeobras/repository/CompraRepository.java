package com.gobra.sistemagestaodeobras.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gobra.sistemagestaodeobras.model.Compra;

public interface CompraRepository extends JpaRepository<Compra, Integer> {
  
}
