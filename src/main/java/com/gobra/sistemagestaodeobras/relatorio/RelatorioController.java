package com.gobra.sistemagestaodeobras.relatorio;

import java.io.FileNotFoundException;
import java.util.List;

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
  @GetMapping("/produto/pdf/{codigo}/{categoria}/{marca}")
  public byte[] generatePDFProdutoReport (@PathVariable Integer codigo, @PathVariable String categoria, @PathVariable String marca) throws FileNotFoundException, JRException {
    return relatorioService.exportPDFProdutoReport(codigo, categoria, marca);
  }

  @GetMapping("/produto/csv/{codigo}/{categoria}/{marca}")
  public void generateCSVProdutoReport (HttpServletResponse response, @PathVariable Integer codigo, @PathVariable String categoria, @PathVariable String marca) throws Exception {
    // Define o nome, conteúdo e formato do arquivo.
    String nomeArquivo = "produtos.csv";
    csvReport.formatCSVReport(response, nomeArquivo);
    
    // Cria um 'escritor' que vai escrever as informações no arquivo.
    StatefulBeanToCsv<RelatorioProdutoDTO> writer = csvReport.createProdutoCSVWriter(response);
    
    // Seta os headers do CSV de Produtos.
    csvReport.setHeadersProduto(response);

    // Gera a lista de produtos a ser gravada.
    List<RelatorioProdutoDTO> listaProdutos = produtoRepository.obterRelatorioProduto(codigo, categoria, marca).stream().map(RelatorioProdutoDTO::new).toList();

    // Escreve os dados no arquivo.
    writer.write(listaProdutos);

  }
  // ------------------------------------------------------------------

  
  // @GetMapping("/testeDownload")
  // public ResponseEntity<byte[]> testeDownload() {
  //   HttpHeaders header = new HttpHeaders();
  //   header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + "teste.csv");
  //   header.add("Cache-Control", "no-cache, no-store, must-revalidate");
  //   header.add("Pragma", "no-cache");
  //   header.add("Expires", "0");
  //   byte[] conteudo = "nome;idade;telefone\naaaa;23;1142653432\nbbbb;22;11253553452\n".getBytes();
  //   return ResponseEntity.ok()
  //       .headers(header)
  //       .contentLength(conteudo.length)
  //       .contentType(MediaType.parseMediaType("text/csv"))
  //       .body(conteudo);
  // }
}
