package com.gobra.sistemagestaodeobras.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gobra.sistemagestaodeobras.model.Obra;

public interface ObraRepository extends JpaRepository<Obra, Integer> {
  
}
