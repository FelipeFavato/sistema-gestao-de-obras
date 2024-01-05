package com.gobra.sistemagestaodeobras.relatorio;

import java.io.FileNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gobra.sistemagestaodeobras.relatorio.service.RelatorioService;

import net.sf.jasperreports.engine.JRException;



@RestController
@RequestMapping("/api/jasper")
public class RelatorioController {

  @Autowired
  private RelatorioService relatorioService;

  @GetMapping("/produto/{categoria}/{marca}")
  public byte[] generateProdutoReport (
    @PathVariable String categoria, @PathVariable String marca
  ) throws FileNotFoundException, JRException {
    return relatorioService.exportProdutoReport(categoria, marca);
  }

}
