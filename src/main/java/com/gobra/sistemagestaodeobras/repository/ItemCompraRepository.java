package com.gobra.sistemagestaodeobras.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.gobra.sistemagestaodeobras.dashboard.projection.ItemLocalProjection;
import com.gobra.sistemagestaodeobras.exceptionHandler.projection.CodXCodYProjection;
import com.gobra.sistemagestaodeobras.model.ItemCompra;



public interface ItemCompraRepository extends JpaRepository<ItemCompra, Integer> {
  
  // Buscar os Gastos Totais de cada Local de Uso.
  @Query(nativeQuery = true,
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


  @Query(nativeQuery = true,
  value = "SELECT "
    + "t1.codigo as codX, "  
    + "t2.codigo_local_uso_obra as codY "  
    + "FROM public.item_compra as t1 "  
    + "LEFT JOIN public.localuso as t2 "  
    + "ON t1.id_local_uso = t2.codigo_local_uso_obra "  
    + "WHERE t2.codigo_local_uso_obra = :codigo"
    )
  List<CodXCodYProjection> obterListaDeLocaisDeUsoAssociadosAItens (@Param("codigo") Integer codigo);

}
