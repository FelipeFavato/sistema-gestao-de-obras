package com.gobra.sistemagestaodeobras.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.gobra.sistemagestaodeobras.exceptionHandler.projection.CodXCodYProjection;
import com.gobra.sistemagestaodeobras.model.Marca;
import java.util.List;


public interface MarcaRepository extends JpaRepository<Marca, Integer> {
 
  Optional<List<Marca>> findByNome(String nome);

  @Query(nativeQuery = true,
    value = ""
      + "SELECT "
      + "T1.codigo as codX, "
      + "T2.codigo as codY "
      + "FROM public.produto as t1 "
      + "LEFT JOIN public.marca as t2 "
      + "ON t1.id_marca = t2.codigo "
      + "WHERE t2.codigo = :codigo"
  )
  List<CodXCodYProjection>obterListaMarcasComProdutosVinculados(@Param("codigo") Integer codigo);

}
