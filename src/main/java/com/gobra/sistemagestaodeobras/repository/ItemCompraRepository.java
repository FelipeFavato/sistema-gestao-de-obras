package com.gobra.sistemagestaodeobras.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.gobra.sistemagestaodeobras.dashboard.projection.ItemLocalProjection;
import com.gobra.sistemagestaodeobras.model.ItemCompra;

// SELECT  t2.nome_local_uso_obra, SUM (t1.valor_total) as total
// FROM public.item_compra as t1
// LEFT JOIN public.localuso as t2
// ON t1.id_local_uso = t2.codigo_local_uso_obra
// GROUP BY t2.nome_local_uso_obra

public interface ItemCompraRepository extends JpaRepository<ItemCompra, Integer> {
  
  // Buscar os Gastos Totais de cada Local de Uso.
  @Query(
    nativeQuery = true,
    value = "SELECT  t2.nome_local_uso_obra as nomeLocalUsoObra, SUM (t1.valor_total) as valorTotal "
      + "FROM public.item_compra as t1 "
      + "LEFT JOIN public.localuso as t2 ON t1.id_local_uso = t2.codigo_local_uso_obra "
      + "GROUP BY t2.nome_local_uso_obra"
  )
  List<ItemLocalProjection> obterSomaValorPorLocalUso();

}
