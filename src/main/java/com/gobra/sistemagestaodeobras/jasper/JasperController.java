package com.gobra.sistemagestaodeobras.jasper;

import java.io.FileNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.sf.jasperreports.engine.JRException;

import com.gobra.sistemagestaodeobras.jasper.service.JasperService;



@RestController
@RequestMapping("/api/jasper")
public class JasperController {

  @Autowired
  private JasperService jasperService;


  // @GetMapping("/api/jasper/produto/{categoria}/{marca}")
  // public List<JasperProdutoDTO> getAllProduto (@PathVariable String categoria, @PathVariable String marca) {
  //   List<JasperProdutoDTO> listaProdutos = produtoRepository.obterRelatorioJasperProduto(categoria, marca).stream().map(JasperProdutoDTO::new).toList();
  //   return listaProdutos;
  // }

  @GetMapping("/produto/{categoria}/{marca}")
  public String generateReport (@PathVariable String categoria, @PathVariable String marca) throws FileNotFoundException, JRException {
    return jasperService.exportReport(categoria, marca);
  }

}
