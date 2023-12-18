package com.gobra.sistemagestaodeobras.jasper;

import java.io.FileNotFoundException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gobra.sistemagestaodeobras.repository.ProdutoRepository;

import net.sf.jasperreports.engine.JRException;

import com.gobra.sistemagestaodeobras.jasper.dto.JasperProdutoDTO;
import com.gobra.sistemagestaodeobras.jasper.service.JasperService;



@RestController
@RequestMapping("/api/jasper")
public class JasperController {
  
  @Autowired
  private ProdutoRepository produtoRepository;

  @Autowired
  private JasperService jasperService;


  @GetMapping("produto")
  public List<JasperProdutoDTO> getAllProduto () {
    List<JasperProdutoDTO> listaProdutos = produtoRepository.obterProdutoNomeCod().stream().map(JasperProdutoDTO::new).toList();
    return listaProdutos;
  }

  @GetMapping("/relatorio/{format}")
  public String generateReport (@PathVariable String format) throws FileNotFoundException, JRException {
    return jasperService.exportReport(format);
  }



}
