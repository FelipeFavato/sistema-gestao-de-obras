package com.gobra.sistemagestaodeobras.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.gobra.sistemagestaodeobras.dashboard.projection.AcumuladoGastosProjection;
import com.gobra.sistemagestaodeobras.dashboard.projection.DescontoProjection;
import com.gobra.sistemagestaodeobras.dashboard.projection.GastoPorFornecedorProjection;
import com.gobra.sistemagestaodeobras.model.Compra;

// SELECT
// 	SUM(CASE WHEN t2.codigo = 43
// 		THEN t1.valor_original END) as valorOMDO,
// 	SUM(CASE WHEN t2.codigo != 43
// 		THEN t1.valor_original END) as valorOSemMDO,
// 	SUM(CASE WHEN t2.codigo != 43
// 		THEN t1.valor_desconto END) as valorDSemDMDO,
// 	SUM(CASE WHEN t2.codigo != 43
// 		THEN t1.valor_final END) as valorFSemDMDO
// FROM public.compra as t1
// LEFT JOIN public.fornecedor as t2
// ON t1.id_fornecedor = t2.codigo


public interface CompraRepository extends JpaRepository<Compra, Integer> {

  @Query(
    nativeQuery = true,
    value = "SELECT "
      + "DATE(date_trunc('month', t1.data_vencimento)) as dataVencimento, "
      + "SUM(CASE WHEN t2.id_local_uso = 38 "
      + "THEN t1.valor_final ELSE 0 END) as gastoMaoDeObra, "
      + "SUM(CASE WHEN t2.id_local_uso != 38 "
      + "THEN t1.valor_final ELSE 0 END) as gastos, "
      + "SUM (t1.valor_final) as gastoTotal "
      + "FROM public.compra as t1 "
      + "LEFT JOIN public.item_compra as t2 "
      + " ON t1.codigo = t2.id_compra "
      + "WHERE id_obra = :codigo "
      + "GROUP BY DATE(date_trunc('month', data_vencimento)) "
      + "ORDER BY DATE(date_trunc('month', data_vencimento))"
  )
  List<AcumuladoGastosProjection> obterValorAcumuladoGastos(@Param("codigo") Integer codigo);

  @Query(
    nativeQuery = true,
    value = "SELECT t2.nome as nomeFornecedor, SUM(t1.valor_final) as valorFinal "
      + "FROM public.compra as t1 "
      + "LEFT JOIN public.fornecedor as t2 "
      + "ON t1.id_fornecedor = t2.codigo "
      + "WHERE t1.id_obra = :codigo "
      + "GROUP BY t2.nome"
  )
  List<GastoPorFornecedorProjection> obterValorGastoPorFornecedor(@Param("codigo") Integer codigo);

  @Query(
    nativeQuery = true,
    value = "SELECT "
      + "SUM(valor_desconto) as desconto "
      + "FROM public.compra "
      + "WHERE id_obra = :codigo"
  )
  List<DescontoProjection> obterSomaDescontos(@Param("codigo") Integer codigo);
}
