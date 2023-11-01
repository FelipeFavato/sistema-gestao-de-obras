package com.gobra.sistemagestaodeobras.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gobra.sistemagestaodeobras.model.Socio;

public interface SocioRepository extends JpaRepository<Socio, Integer> {

  List<Socio> findByNomeContaining(String nome);

}
