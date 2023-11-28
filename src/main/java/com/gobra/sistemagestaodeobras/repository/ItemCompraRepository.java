package com.gobra.sistemagestaodeobras.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.gobra.sistemagestaodeobras.dashboard.projection.ItemLocalProjection;
import com.gobra.sistemagestaodeobras.dashboard.projection.MDOGastoComprasOrcamentoProjection;
import com.gobra.sistemagestaodeobras.model.ItemCompra;

// SELECT
// 	t2.nome_local_uso_obra as nomeLocalUsoObra,
// 	t4.nome as nomeFornecedor,
// 	SUM (t1.valor_total) as valorTotal
// FROM public.item_compra as t1
// LEFT JOIN public.localuso as t2
// ON t1.id_local_uso = t2.codigo_local_uso_obra
// LEFT JOIN public.compra as t3
// ON t1.id_compra = t3.codigo
// LEFT JOIN public.fornecedor as t4
// ON t3.id_fornecedor = t4.codigo
// WHERE t3.id_obra = :codigo
// GROUP BY t2.nome_local_uso_obra, t4.nome

public interface ItemCompraRepository extends JpaRepository<ItemCompra, Integer> {
  
  // Buscar os Gastos Totais de cada Local de Uso.
  @Query(
    nativeQuery = true,
    value = "SELECT "
      + "t2.nome_local_uso_obra as nomeLocalUsoObra, "
      + "t4.nome as nomeFornecedor, "
      + "SUM (t1.valor_total) as valorTotal "
      + "FROM public.item_compra as t1 "
      + "LEFT JOIN public.localuso as t2 "
      + "ON t1.id_local_uso = t2.codigo_local_uso_obra "
      + "LEFT JOIN public.compra as t3 "
      + "ON t1.id_compra = t3.codigo "
      + "LEFT JOIN public.fornecedor as t4 "
      + "ON t3.id_fornecedor = t4.codigo "
      + "WHERE t3.id_obra = :codigo "
      + "GROUP BY t2.nome_local_uso_obra, t4.nome "
      + "ORDER BY t2.nome_local_uso_obra"
  )
  List<ItemLocalProjection> obterSomaValorPorLocalUso(@Param("codigo") Integer codigo);

  @Query(
    nativeQuery = true,
    value = "SELECT "
      + "t3.id_obra as idObra, "
      + "t4.nome as nomeObra, "
      + "SUM(CASE WHEN t2.codigo_local_uso_obra = 38 "
      + "THEN t1.valor_total END) as custoMaoDeObra, "
      + "t4.custo_mao_de_obra as orcamentoMaoDeObra, "
      + "SUM(CASE WHEN t2.codigo_local_uso_obra != 38 "
      + "THEN t1.valor_total END) as valorTotal, "
      + "t4.custo_previsto as custoPrevisto "
      + "FROM public.item_compra as t1 "
      + "LEFT JOIN public.localuso as t2 "
      + "on t1.id_local_uso = t2.codigo_local_uso_obra "
      + "LEFT JOIN public.compra as t3 "
      + "ON t1.id_compra = t3.codigo "
      + "LEFT JOIN public.obra as t4 "
      + "ON t3.id_obra = t4.codigo "
      + "WHERE t3.id_obra = :codigo "
      + "GROUP BY t3.id_obra, "
      + "t4.nome, t4.custo_mao_de_obra, t4.custo_previsto"
  )
  List<MDOGastoComprasOrcamentoProjection> obterMDOGastoComprasOrcamento (@Param("codigo") Integer codigo);

}
