package com.gobra.sistemagestaodeobras.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.gobra.sistemagestaodeobras.dashboard.projection.GastoPorSocioProjection;
import com.gobra.sistemagestaodeobras.model.Socio;

// SELECT
// 	t1.nome as nomeSocio,
// 	SUM (t2.valor_final) as valorFinal
// FROM public.socio as t1
// LEFT JOIN public.compra as t2
// ON t2.id_socio = t1.codigo
// GROUP BY t1.nome;

public interface SocioRepository extends JpaRepository<Socio, Integer> {

  List<Socio> findByNomeContaining(String nome);

  @Query(
    nativeQuery = true,
    value = "SELECT t1.nome as nomeSocio, SUM (t2.valor_final) as valorFinal "
      + "FROM public.socio as t1 "
      + "LEFT JOIN public.compra as t2 "
      + "ON t2.id_socio = t1.codigo "
      + "GROUP BY t1.nome"
  )
  List<GastoPorSocioProjection> obterValorGastoPorSocio();

}
