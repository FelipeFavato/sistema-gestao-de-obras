package com.gobra.sistemagestaodeobras.relatorio.service;

import java.io.FileNotFoundException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gobra.sistemagestaodeobras.relatorio.dto.RelatorioProdutoDTO;
import com.gobra.sistemagestaodeobras.relatorio.utils.PDFReport;
import com.gobra.sistemagestaodeobras.repository.ProdutoRepository;

import net.sf.jasperreports.engine.JRException;


// Apache poi => Biblioteca da APACHE, poi é um projeto para lidar com ferramentas da Microsoft
// Retornando o CSV => Retorna uma String com os registros em linha e as colunas separadas por virgula 
@Service
public class RelatorioService {

  @Autowired
  private PDFReport pdfReport;

  @Autowired
  private ProdutoRepository produtoRepository;

  // Produto PDF ------------------------------------------------------------------\
  public byte[] exportPDFProdutoReport (String categoria, String marca) throws FileNotFoundException, JRException {
    List<RelatorioProdutoDTO> produtos = produtoRepository.obterRelatorioProduto(categoria, marca).stream().map(RelatorioProdutoDTO::new).toList();

    byte[] pdfBytes = pdfReport.exportPDFReport("/jasperproduto.jrxml", produtos);

    return pdfBytes;
  }
  //////////////////////////////////////////////////////////////////////////////////

}
