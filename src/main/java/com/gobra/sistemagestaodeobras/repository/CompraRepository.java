package com.gobra.sistemagestaodeobras.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.gobra.sistemagestaodeobras.dashboard.projection.AcumuladoGastosProjection;
import com.gobra.sistemagestaodeobras.dashboard.projection.GastoPorFornecedorProjection;
import com.gobra.sistemagestaodeobras.model.Compra;

// SELECT
// 	date_trunc('month', t1.data_vencimento) as dataVencimento,
// 	SUM (t1.valor_final) as valorFinal,
// 	t1.id_obra as idObra,
// 	t2.custo_mao_de_obra as custoMaoDeObra
// FROM public.compra as t1
// LEFT JOIN public.obra as t2
// ON t1.id_obra = t2.codigo
// GROUP BY
// 	date_trunc('month', t1.data_vencimento),
// 	t1.id_obra,
// 	t2.custo_mao_de_obra;

// SELECT t2.nome as nomeFornecedor, SUM(t1.valor_final) as valorFinal	
// FROM public.compra as t1
// LEFT JOIN public.fornecedor as t2
// ON t1.id_fornecedor = t2.codigo
// GROUP BY t2.nome;


public interface CompraRepository extends JpaRepository<Compra, Integer> {

  @Query(
    nativeQuery = true,
    value = "SELECT date_trunc('month', data_vencimento) as dataVencimento, "
      + "id_obra as idObra, SUM(valor_final) as valorFinal "
      + "FROM public.compra "
      + "GROUP BY date_trunc('month', data_vencimento), id_obra"
  )
  List<AcumuladoGastosProjection> obterValorAcumuladoGastos ();

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

}
