package com.gobra.sistemagestaodeobras.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.gobra.sistemagestaodeobras.model.Produto;
import com.gobra.sistemagestaodeobras.jasper.projection.JasperProdutoProjection;

public interface ProdutoRepository extends JpaRepository<Produto, Integer> {
  
  @Query(
    nativeQuery = true,
    value = "SELECT "
      + "codigo as codigoProduto, "
      + "nome as nomeProduto "
      + "FROM public.produto"
  )
  List<JasperProdutoProjection>obterProdutoNomeCod();

}
