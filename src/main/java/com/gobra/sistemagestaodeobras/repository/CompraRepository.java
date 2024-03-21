package com.gobra.sistemagestaodeobras.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.gobra.sistemagestaodeobras.bot.projection.OrcamentoBotProjection;
import com.gobra.sistemagestaodeobras.dashboard.projection.AcumuladoGastosProjection;
import com.gobra.sistemagestaodeobras.dashboard.projection.GastoPorFornecedorProjection;
import com.gobra.sistemagestaodeobras.dashboard.projection.MDOGastoComprasOrcamentoProjection;
import com.gobra.sistemagestaodeobras.exceptionHandler.projection.CodXCodYProjection;
import com.gobra.sistemagestaodeobras.model.Compra;
 



public interface CompraRepository extends JpaRepository<Compra, Integer> {

  @Query(
    nativeQuery = true,
    value = "SELECT "
      + "DATE(date_trunc('month', data_pagamento)) as dataVencimento, "
      + "SUM(CASE WHEN id_fornecedor = 43 "
      + "THEN valor_final ELSE 0 END) as gastoMaoDeObra, "
      + "SUM(CASE WHEN id_fornecedor != 43 "
      + "THEN valor_final ELSE 0 END) as gastos, "
      + "SUM (valor_final) as gastoTotal "
      + "FROM public.compra "
      + "WHERE id_obra = :codigo "
      + "GROUP BY DATE(date_trunc('month', data_pagamento)) "
      + "ORDER BY DATE(date_trunc('month', data_pagamento))"
  )
  List<AcumuladoGastosProjection> obterValorAcumuladoGastos(@Param("codigo") Integer codigo);

  @Query(
    nativeQuery = true,
    value = "SELECT "
      + "t2.nome as nomeFornecedor, "
      + "COALESCE(t4.nome, 'N/I') as nomeProduto, "
      + "COALESCE(SUM(t3.valor_total), 0) as valorTotal "
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
      + "t1.custo_mao_de_obra as custoMaoDeObra, "
      + "t1.custo_previsto as custoPrevisto, "
      + "t3.valorGastos as valorGastos, "
      + "t3.valorDesconto as valorDesconto, "
      + "t3.pagoMaoDeObra as pagoMaoDeObra, "
      + "COALESCE(SUM(t2.valor_total_previsto), 0) as valorTotalPrevisto "
      + "FROM public.obra as t1 "
      + "LEFT JOIN public.previsao_compra as t2 "
      + "ON t1.codigo = t2.id_obra "
      + "AND t2.compra_prevista_realizada = false "
      + "LEFT JOIN ( "
      + "SELECT "
      + "id_obra, "
      + "SUM(CASE WHEN id_fornecedor = 43 "
      + "THEN valor_final END) as pagoMaoDeObra, "
      + "SUM(CASE WHEN id_fornecedor != 43 "
      + "THEN valor_desconto END) as valorDesconto, "
      + "SUM(CASE WHEN id_fornecedor != 43 "
      + "THEN valor_final END) as valorGastos "
      + "FROM public.compra "
      + "GROUP BY id_obra "
      + ") as t3 "
      + "ON t1.codigo = t3.id_obra "
      + "WHERE t1.codigo = :codigo "
      + "GROUP BY t1.custo_mao_de_obra, t1.custo_previsto, t3.valorGastos, t3.pagoMaoDeObra, t3.valorDesconto "
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

  @Query(nativeQuery = true,
    value = "SELECT "
      + "t1.codigo as codX, "
      + "t2.codigo as codY "
      + "FROM public.compra as t1 "
      + "LEFT JOIN public.fornecedor as t2 "
      + "ON t1.id_fornecedor = t2.codigo "
      + "WHERE t2.codigo = :codigo"
  )
  List<CodXCodYProjection> obterListaComprasComFornecedoresVinculados(@Param("codigo") Integer codigo);

  @Query(nativeQuery = true,
    value = "SELECT "
    + "t1.codigo as codX, "
    + "t2.codigo as codY "
    + "FROM public.compra as t1 "
    + "LEFT JOIN public.obra as t2 "
    + "ON t1.id_obra = t2.codigo "
    + "WHERE t2.codigo = :codigo"
  )
  List<CodXCodYProjection> obterObraComComprasCadastradas(@Param("codigo") Integer codigo);

  @Query(nativeQuery = true,
    value = "SELECT "
      + "t1.codigo as codX, "
      + "t2.codigo as codY "
      + "FROM public.compra as t1 "
      + "LEFT JOIN public.socio as t2 "
      + "ON t1.id_socio = t2.codigo "
      + "WHERE t2.codigo = :codigo "
  )
  List<CodXCodYProjection> obterSociosQueJaTenhamFeitoAlgumaCompra(@Param("codigo") Integer codigo);

  @Query(nativeQuery = true,
  value = "SELECT "
    + "t1.codigo as codX, "
    + "t2.codigo as codY "
    + "FROM public.item_compra as t1 "
    + "LEFT JOIN public.compra as t2 "
    + "ON t1.id_compra = t2.codigo "
    + "WHERE t2.codigo = :codigo "
  )
  List<CodXCodYProjection> obterListaDeCompraComItens(@Param("codigo") Integer codigo);
}
