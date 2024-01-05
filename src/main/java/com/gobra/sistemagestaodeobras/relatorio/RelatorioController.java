package com.gobra.sistemagestaodeobras.relatorio;

import java.io.FileNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gobra.sistemagestaodeobras.relatorio.dto.RelatorioProdutoDTO;
import com.gobra.sistemagestaodeobras.relatorio.service.RelatorioService;
import com.gobra.sistemagestaodeobras.relatorio.utils.CSVReport;
import com.gobra.sistemagestaodeobras.repository.ProdutoRepository;
import com.opencsv.bean.StatefulBeanToCsv;

import jakarta.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.JRException;



@RestController
@RequestMapping("/api/relatorio")
public class RelatorioController {

  @Autowired
  private RelatorioService relatorioService;

  @Autowired
  private ProdutoRepository produtoRepository;

  @Autowired
  private CSVReport csvReport;

  // Produto PDF/CSV --------------------------------------------------
  @GetMapping("/produto/pdf/{categoria}/{marca}")
  public byte[] generatePDFProdutoReport (@PathVariable String categoria, @PathVariable String marca) throws FileNotFoundException, JRException {
    return relatorioService.exportPDFProdutoReport(categoria, marca);
  }

  @GetMapping("/produto/csv/{categoria}/{marca}")
  public void generateCSVProdutoReport (HttpServletResponse response, @PathVariable String categoria, @PathVariable String marca) throws Exception {
    // Define o nome do arquivo e o conteúdo
    String nomeArquivo = "produtos.csv";

    csvReport.formatCSVReport(response, nomeArquivo);

    StatefulBeanToCsv<RelatorioProdutoDTO> writer = csvReport.createProdutoCSVWriter(response);
    
    // Seta os headers do CSV de Produtos
    csvReport.setHeadersProduto(response);
    
    // Escreve os dados no arquivo
    writer.write(produtoRepository.obterRelatorioProduto(categoria, marca).stream().map(RelatorioProdutoDTO::new).toList());
    
  }


  // ------------------------------------------------------------------

}
