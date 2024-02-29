package com.gobra.sistemagestaodeobras.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.gobra.sistemagestaodeobras.bot.projection.GastoSocioBotProjection;
import com.gobra.sistemagestaodeobras.bot.projection.SocioTelegramIDBotProjection;
import com.gobra.sistemagestaodeobras.dashboard.projection.GastoPorSocioProjection;
import com.gobra.sistemagestaodeobras.exceptionHandler.projection.CodXCodYProjection;
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

  Optional<List<Socio>> findByNome(String nome);

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

  @Query(
    nativeQuery = true,
    value = "SELECT "
      + "nome as nomeSocio, "
      + "telegram_id as telegramID "
      + "FROM public.socio "
      + "WHERE telegram_id IS NOT NULL"
  )
  List<SocioTelegramIDBotProjection> obterNomeSocioTelegramID();

  @Query(nativeQuery = true,
    value = "SELECT "
      + "id_socio as codX, "
      + "id_obra as codY "
      + "FROM public.socio_obra "
      + "WHERE id_socio = :codigo"
  )
  List<CodXCodYProjection> obterSocioQueEstejaAtribuidoAAlgumaObra(@Param("codigo") Integer codigo);

  @Query(nativeQuery = true,
    value = "SELECT "
    + "t1.codigo as codX, "
    + "t2.codigo as codY "
    + "FROM public.compra as t1 "
    + "LEFT JOIN public.socio as t2 "
    + "ON t1.id_socio = t2.codigo "
    + "WHERE t2.codigo = :codigo"
  )
  List<CodXCodYProjection> obterSocioQueTenhaComprasVinculadas(@Param("codigo") Integer codigo);

}
