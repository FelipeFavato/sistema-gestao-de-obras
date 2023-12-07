package com.gobra.sistemagestaodeobras.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.gobra.sistemagestaodeobras.bot.projection.GastoSocioBotProjection;
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
      + "INNER JOIN public.compra as t2 "
      + "ON t2.id_socio = t1.codigo "
      + "WHERE t2.id_obra = :codigo "
      + "GROUP BY t1.nome"
  )
  List<GastoPorSocioProjection> obterValorGastoPorSocio(@Param("codigo") Integer codigo);

  @Query(
    nativeQuery = true,
    value = "SELECT "
      + "t3.nome as nomeObra, "
      + "t1.nome as nomeSocio, "
      + "SUM (t2.valor_final) as valorFinal "
      + "FROM public.socio as t1 "
      + "INNER JOIN public.compra as t2 "
      + "ON t2.id_socio = t1.codigo "
      + "LEFT JOIN public.obra as t3 "
      + "ON t2.id_obra = t3.codigo "
      + "WHERE t2.id_obra = :codigo "
      + "GROUP BY t1.nome, t3.nome"
  )
  List<GastoSocioBotProjection> obterValorGastoPorSocioBot(@Param("codigo") Integer codigo);


}
