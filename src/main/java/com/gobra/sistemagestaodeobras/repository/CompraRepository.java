package com.gobra.sistemagestaodeobras.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.gobra.sistemagestaodeobras.dashboard.projection.AcumuladoGastosProjection;
import com.gobra.sistemagestaodeobras.model.Compra;

// SELECT date_trunc('month', data_vencimento)::date as dataVencimento,
// 	id_obra as idObra, SUM(valor_final) as valorFinal
// FROM public.compra
// GROUP BY date_trunc('month', data_vencimento)::date, id_obra



public interface CompraRepository extends JpaRepository<Compra, Integer> {


  @Query(
    nativeQuery = true,
    value = "SELECT date_trunc('month', data_vencimento) as dataVencimento, "
      + "id_obra as idObra, SUM(valor_final) as valorFinal "
      + "FROM public.compra "
      + "GROUP BY date_trunc('month', data_vencimento), id_obra"
  )
  List<AcumuladoGastosProjection> obterValorAcumuladoGastos ();

}
