package com.gobra.sistemagestaodeobras.jasper;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gobra.sistemagestaodeobras.repository.ProdutoRepository;
import com.gobra.sistemagestaodeobras.jasper.dto.JasperProdutoDTO;



@RestController
@RequestMapping("/api/jasper")
public class JasperController {
  
  @Autowired
  private ProdutoRepository produtoRepository;


  @GetMapping("produto")
  public List<JasperProdutoDTO> getAllProduto () {
    List<JasperProdutoDTO> listaProdutos = produtoRepository.obterProdutoNomeCod().stream().map(JasperProdutoDTO::new).toList();
    return listaProdutos;
  }
}
