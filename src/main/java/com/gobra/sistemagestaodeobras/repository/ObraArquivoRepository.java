package com.gobra.sistemagestaodeobras.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gobra.sistemagestaodeobras.model.ObraArquivo;
import java.util.List;
import java.util.Optional;


public interface ObraArquivoRepository extends JpaRepository<ObraArquivo, Integer>{
  
  Optional<List<ObraArquivo>> findByHashArquivo(String hashArquivo);

}
