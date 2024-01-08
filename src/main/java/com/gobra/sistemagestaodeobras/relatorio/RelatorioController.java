package com.gobra.sistemagestaodeobras.relatorio;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
    // Define o nome, conteúdo e formato do arquivo.
    String nomeArquivo = "produtos.csv";
    csvReport.formatCSVReport(response, nomeArquivo);
    
    // Cria um 'escritor' que vai escrever as informações no arquivo.
    StatefulBeanToCsv<RelatorioProdutoDTO> writer = csvReport.createProdutoCSVWriter(response);
    
    // Seta os headers do CSV de Produtos.
    csvReport.setHeadersProduto(response);

    // File downloadFile = new File("data/file_to_download.mp4");
    // InputStreamResource resource = new InputStreamResource(new FileInputStream(downloadFile));
    
    // Escreve os dados no arquivo.
    writer.write(produtoRepository.obterRelatorioProduto(categoria, marca).stream().map(RelatorioProdutoDTO::new).toList());

    // return ResponseEntity.ok()
    // .headers(header)
    // .contentLength(downloadFile.length())
    // .contentType(MediaType.parseMediaType("application/octet-stream"))
    // .body(resource);
  }

  // @GetMapping("/produto/csv/{categoria}/{marca}")
  // public ResponseEntity<InputStreamResource> generateCSVProdutoReport (HttpServletResponse response, @PathVariable String categoria, @PathVariable String marca) throws Exception {
  //   String nomeArquivo = "produtos.csv";
  //   ByteArrayInputStream fileData = this.load();
  //   InputStreamResource resource = new InputStreamResource(fileData);
  //   return ResponseEntity.ok()
  //           .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + nomeArquivo)
  //           .contentType(MediaType.parseMediaType("application/csv"))
  //           .body(resource);

  // }

  // private ByteArrayInputStream load() {
  //   List<Produto> produtos = produtoRepository.findAll();
  //   ByteArrayInputStream stream = this.produtosToCSV(produtos);
  //   return stream;
  // }

  // private ByteArrayInputStream produtosToCSV(List<Produto> produtos) {
  //   CSVFormat format = CSVFormat.DEFAULT.withQuoteMode(QuoteMode.MINIMAL);
  //   try(ByteArrayOutputStream out = new ByteArrayOutputStream();
  //     CSVPrinter csvPrinter = new CSVPrinter(new PrintWriter(out), format);
  //   ) {
  //     for (Produto produto : produtos) {
  //       List<String> data = Arrays.asList(String.valueOf(produto.getCodigo()), produto.getNome());
  //       csvPrinter.printRecords(data);
  //     }
  //   } catch (IOException e) {
  //   }

  //   return null;
  // }

  // ------------------------------------------------------------------

}
