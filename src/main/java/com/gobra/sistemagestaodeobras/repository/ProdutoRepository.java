package com.gobra.sistemagestaodeobras.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.gobra.sistemagestaodeobras.exceptionHandler.projection.CodXCodYProjection;
import com.gobra.sistemagestaodeobras.model.Produto;
import com.gobra.sistemagestaodeobras.relatorio.projection.RelatorioProdutoProjection;

public interface ProdutoRepository extends JpaRepository<Produto, Integer> {
  
  Optional<List<Produto>> findByNome(String nome);

  @Query(
    nativeQuery = true,
    value = "SELECT "
      + "codigo as codigoProduto, "
      + "nome as nomeProduto "
      + "FROM public.produto"
  )
  List<RelatorioProdutoProjection>obterProdutoNomeCod();

  @Query(
    nativeQuery = true,
    value = "SELECT "
      + "t1.codigo as codigo, "
      + "t1.nome as nome, "
      + "t1.tipo_produto as categoria, "
      + "t2.nome as marca "
      + "FROM public.produto as t1 "
      + "LEFT JOIN public.marca as t2 "
      + "ON t1.id_marca = t2.codigo "
      + "WHERE (:categoria = 'todos' OR t1.tipo_produto = :categoria) "
      + "AND (:marca = 'todos' OR t2.nome = :marca) "
      + "ORDER BY t1.nome ASC"
    )
  List<RelatorioProdutoProjection>obterRelatorioProduto(@Param("categoria") String categoria, @Param("marca") String marca);

  @Query(nativeQuery = true,
    value = "SELECT "
      + "T1.codigo as codX, "
      + "T2.codigo as codY "
      + "FROM public.item_compra as t1 "
      + "LEFT JOIN public.produto as t2 "
      + "ON t1.id_produto = t2.codigo "
      + "WHERE t2.codigo = :codigo"    
	)
  List<CodXCodYProjection>obterListaProdutosComItensVinculados(@Param("codigo") Integer codigo);
}
