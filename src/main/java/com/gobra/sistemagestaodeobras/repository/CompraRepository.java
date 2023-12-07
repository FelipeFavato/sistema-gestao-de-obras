package com.gobra.sistemagestaodeobras.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.gobra.sistemagestaodeobras.bot.projection.OrcamentoBotProjection;
import com.gobra.sistemagestaodeobras.dashboard.projection.AcumuladoGastosProjection;
import com.gobra.sistemagestaodeobras.dashboard.projection.GastoPorFornecedorProjection;
import com.gobra.sistemagestaodeobras.dashboard.projection.MDOGastoComprasOrcamentoProjection;
import com.gobra.sistemagestaodeobras.model.Compra;

// SELECT
// 	DATE(date_trunc('month', data_vencimento)) as dataVencimento,
// 	SUM(CASE WHEN id_fornecedor = 43
// 		THEN valor_final ELSE 0 END) as gastoMaoDeObra,
// 	SUM(CASE WHEN id_fornecedor != 43
// 		THEN valor_final ELSE 0 END) as gastos,
// 	SUM (valor_final) as gastoTotal
// FROM public.compra
// WHERE id_obra = 1
// GROUP BY DATE(date_trunc('month', data_vencimento))
// ORDER BY DATE(date_trunc('month', data_vencimento))

// SELECT
// 	t2.nome as nomeFornecedor,
// 	t4.nome as nomeProduto,
// 	SUM(t3.valor_total) as valorTotal
// FROM public.compra as t1
// LEFT JOIN public.fornecedor as t2
// ON t1.id_fornecedor = t2.codigo
// LEFT JOIN public.item_compra as t3
// ON t1.codigo = t3.id_compra
// LEFT JOIN public.produto as t4
// ON t3.id_produto = t4.codigo
// WHERE t1.id_obra = 1
// GROUP BY t2.nome, t4.nome
// ORDER BY t2.nome ASC


public interface CompraRepository extends JpaRepository<Compra, Integer> {

  @Query(
    nativeQuery = true,
    value = "SELECT "
      + "DATE(date_trunc('month', data_vencimento)) as dataVencimento, "
      + "SUM(CASE WHEN id_fornecedor = 43 "
      + "THEN valor_final ELSE 0 END) as gastoMaoDeObra, "
      + "SUM(CASE WHEN id_fornecedor != 43 "
      + "THEN valor_final ELSE 0 END) as gastos, "
      + "SUM (valor_final) as gastoTotal "
      + "FROM public.compra "
      + "WHERE id_obra = :codigo "
      + "GROUP BY DATE(date_trunc('month', data_vencimento)) "
      + "ORDER BY DATE(date_trunc('month', data_vencimento))"
  )
  List<AcumuladoGastosProjection> obterValorAcumuladoGastos(@Param("codigo") Integer codigo);

  @Query(
    nativeQuery = true,
    value = "SELECT "
      + "t2.nome as nomeFornecedor, "
      + "t4.nome as nomeProduto, "
      + "SUM(t3.valor_total) as valorTotal "
      + "FROM public.compra as t1 "
      + "LEFT JOIN public.fornecedor as t2 "
      + "ON t1.id_fornecedor = t2.codigo "
      + "LEFT JOIN public.item_compra as t3 "
      + "ON t1.codigo = t3.id_compra "
      + "LEFT JOIN public.produto as t4 "
      + "ON t3.id_produto = t4.codigo "
      + "WHERE t1.id_obra = :codigo "
      + "GROUP BY t2.nome, t4.nome "
      + "ORDER BY t2.nome ASC"
  )
  List<GastoPorFornecedorProjection> obterValorGastoPorFornecedor(@Param("codigo") Integer codigo);

  @Query(
    nativeQuery = true,
    value = "SELECT "
      + "t2.custo_mao_de_obra as custoMaoDeObra, "
      + "t2.custo_previsto as custoPrevisto, "
      + "SUM(CASE WHEN t1.id_fornecedor = 43 "
      + "THEN t1.valor_final END) as pagoMaoDeObra, "
      + "SUM(CASE WHEN t1.id_fornecedor != 43 "
      + "THEN t1.valor_desconto END) as valorDesconto, "
      + "SUM(CASE WHEN t1.id_fornecedor != 43 "
      + "THEN t1.valor_final END) as valorGastos "
      + "FROM public.compra as t1 "
      + "LEFT JOIN public.obra as t2 "
      + "ON t1.id_obra = t2.codigo "
      + "WHERE t1.id_obra = :codigo "
      + "GROUP BY t2.custo_mao_de_obra, t2.custo_previsto"
  )
  List<MDOGastoComprasOrcamentoProjection> obterMDOGastoComprasOrcamento (@Param("codigo") Integer codigo);

  @Query(
    nativeQuery = true,
    value = "SELECT "
      + "t2.nome as nomeObra, "
      + "t2.custo_mao_de_obra as custoMaoDeObra, "
      + "t2.custo_previsto as custoPrevisto, "
      + "SUM(CASE WHEN t1.id_fornecedor = 43 "
      + "THEN t1.valor_final END) as pagoMaoDeObra, "
      + "SUM(CASE WHEN t1.id_fornecedor != 43 "
      + "THEN t1.valor_desconto END) as valorDesconto, "
      + "SUM(CASE WHEN t1.id_fornecedor != 43 "
      + "THEN t1.valor_final END) as valorGastos "
      + "FROM public.compra as t1 "
      + "LEFT JOIN public.obra as t2 "
      + "ON t1.id_obra = t2.codigo "
      + "WHERE t1.id_obra = :codigo "
      + "GROUP BY t2.nome, t2.custo_mao_de_obra, t2.custo_previsto"
  )
  List<OrcamentoBotProjection> obterOrcamentoBot (@Param("codigo") Integer codigo);

}
