package com.gobra.sistemagestaodeobras.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.gobra.sistemagestaodeobras.dashboard.projection.ObraCodNomeProjection;
import com.gobra.sistemagestaodeobras.model.Obra;


public interface ObraRepository extends JpaRepository<Obra, Integer> {


  @Query(
    nativeQuery = true,
    value = "SELECT "
      + "codigo as codigo, "
      + "nome as nome "
      + "FROM public.obra"
  )
  List<ObraCodNomeProjection>obterCodigoNomeObras();

}
