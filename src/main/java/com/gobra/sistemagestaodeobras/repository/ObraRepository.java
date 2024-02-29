package com.gobra.sistemagestaodeobras.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.gobra.sistemagestaodeobras.bot.projection.TelegramIDBotProjection;
import com.gobra.sistemagestaodeobras.dashboard.projection.ObraCodNomeProjection;
import com.gobra.sistemagestaodeobras.model.Obra;


public interface ObraRepository extends JpaRepository<Obra, Integer> {

  Optional<List<Obra>> findByNome(String nome);

  @Query(
    nativeQuery = true,
    value = "SELECT "
      + "codigo as codigo, "
      + "nome as nome "
      + "FROM public.obra"
  )
  List<ObraCodNomeProjection>obterCodigoNomeObras();

  @Query(nativeQuery = true,
  value = "SELECT "
    + "COALESCE(t3.telegram_id, 0) as telegramID "
    + "FROM public.obra as t1 "
    + "LEFT JOIN public.socio_obra as t2 "
    + "ON t1.codigo = t2.id_obra "
    + "LEFT JOIN public.socio as t3 "
    + "ON t2.id_socio = t3.codigo "
    + "WHERE t1.codigo = :codigo"
  )
  List<TelegramIDBotProjection> obterTelegramIDDosSociosDaObra(@Param("codigo") Integer codigo);

}
