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
    value = "SELECT DISTINCT "
      + "t4.nome_local_uso_obra as nomeLocalUso, "
      + "t5.codigo as codigoProduto, "
      + "t5.nome as nomeProduto, "
      + "t5.tipo_produto as tipoProduto, "
      + "t6.nome as nomeMarca "
      + "FROM public.compra as t1 "
      + "LEFT JOIN public.obra as t2 "
      + "ON t1.id_obra = t2.codigo "
      + "LEFT JOIN public.item_compra as t3 "
      + "ON t1.codigo = t3.id_compra "
      + "LEFT JOIN public.localuso as t4 "
      + "ON t3.id_local_uso = t4.codigo_local_uso_obra "
      + "LEFT JOIN public.produto as t5 "
      + "ON t3.id_produto = t5.codigo "
      + "LEFT JOIN public.marca as t6 "
      + "ON t5.id_marca = t6.codigo "
      + "WHERE t2.codigo = :codigo "
      + "AND (:categoria = 'todos' OR t5.tipo_produto = :categoria) "
      + "AND (:marca = 'todos' OR t6.nome = :marca) "
      + "ORDER BY t4.nome_local_uso_obra, t5.nome, t6.nome ASC"
    )
  List<RelatorioProdutoProjection>obterRelatorioProduto(@Param("codigo") Integer codigo, @Param("categoria") String categoria, @Param("marca") String marca);

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
