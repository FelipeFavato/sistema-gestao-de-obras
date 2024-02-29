package com.gobra.sistemagestaodeobras.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.gobra.sistemagestaodeobras.exceptionHandler.projection.CodXCodYProjection;
import com.gobra.sistemagestaodeobras.model.UnidadeMedida;

public interface UnidadeMedidaRepository extends JpaRepository<UnidadeMedida, Integer> {
  
  Optional<List<UnidadeMedida>> findByUnidade(String unidade);

  @Query(nativeQuery = true,
    value = "SELECT "
      + "t1.codigo as codX, "
      + "t2.codigo as codY "
      + "FROM public.item_compra as t1 "
      + "LEFT JOIN public.unidade_medida as t2 "
      + "ON t1.id_unidade_medida = t2.codigo "
      + "WHERE t2.codigo = :codigo "
  )
  List<CodXCodYProjection> obterMarcasQueTenhamProdutosVinculados(@Param("codigo") Integer codigo);

}
