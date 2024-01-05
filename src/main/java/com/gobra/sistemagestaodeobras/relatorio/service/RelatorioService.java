package com.gobra.sistemagestaodeobras.relatorio.service;

import java.io.FileNotFoundException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gobra.sistemagestaodeobras.relatorio.dto.JasperProdutoDTO;
import com.gobra.sistemagestaodeobras.relatorio.utils.ExportPDFReport;
import com.gobra.sistemagestaodeobras.repository.ProdutoRepository;

import net.sf.jasperreports.engine.JRException;


// Apache poi => Biblioteca da APACHE, poi é um projeto para lidar com ferramentas da Microsoft
// Retornando o CSV => Retorna uma String com os registros em linha e as colunas separadas por virgula 
@Service
public class RelatorioService {

  @Autowired
  private ExportPDFReport exportPDFReport;

  @Autowired
  private ProdutoRepository produtoRepository;

  public byte[] exportProdutoReport (String categoria, String marca) throws FileNotFoundException, JRException {
    // String userHome = System.getProperty("user.home");
    // String path = userHome + "/Downloads";
    List<JasperProdutoDTO> produtos = produtoRepository.obterRelatorioProduto(categoria, marca).stream().map(JasperProdutoDTO::new).toList();

    System.out.println(produtos);

    // if ("pdf".equalsIgnoreCase(formato)) {
      byte[] pdfBytes = exportPDFReport.exportPDFReport("/jasperproduto.jrxml", produtos);

      return pdfBytes;

    // } else if ("csv".equalsIgnoreCase(formato)) {
      // lógica para gerar o arquivo CSV

    // }
  }

}
