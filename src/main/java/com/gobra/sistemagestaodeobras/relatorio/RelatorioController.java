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

  // Produto PDF/CSV ------------------------------------------------------------\
  @GetMapping("/produto/pdf/{categoria}/{marca}")
  public byte[] generatePDFProdutoReport (@PathVariable String categoria, @PathVariable String marca) throws FileNotFoundException, JRException {
    return relatorioService.exportPDFProdutoReport(categoria, marca);
  }

  @GetMapping("/produto/csv/{categoria}/{marca}")
  public void generateCSVProdutoReport (HttpServletResponse response, @PathVariable String categoria, @PathVariable String marca) throws Exception {
    // Define o nome, conteúdo e formato do arquivo.
    String nomeArquivo = "produtos.csv";
    csvReport.formatCSVReport(response, nomeArquivo);
    // HttpServletResponse formattedResponse = csvReport.formatCSVReport(response, nomeArquivo);
    
    // Cria um 'escritor' que vai escrever as informações no arquivo.
    StatefulBeanToCsv<RelatorioProdutoDTO> writer = csvReport.createProdutoCSVWriter(response);
    
    // Seta os headers do CSV de Produtos.
    csvReport.setHeadersProduto(response);

    // Gera a lista de produtos a ser gravada.
    List<RelatorioProdutoDTO> listaProdutos = produtoRepository.obterRelatorioProduto(categoria, marca).stream().map(RelatorioProdutoDTO::new).toList();
    
    // Escreve os dados no arquivo.
    writer.write(listaProdutos);

  }

  ////////////////////////////////////////////////////////////////////////////////

  // CODIGO;NOME;CATEGORIA;MARCA
  // 193;Adaptador curto 32mmx1;Material;Não informada
  // 194;adaptador curto 50 mmx 1x1/2;Material;Não informada
  // 144;Adaptador Flange Sold Krona 25 x 3/4;Material;Krona
  // 115;Adesivo PVC Pote 175g com Pincel Tigre;Material;Tigre
  // 99;Adesivo PVC Pote 850G;Material;Tigre
  // 134;Adesivo PVC Pote 850G - Amanco ;Material;Amanco
  // 80;Aditivo plastificante Vedalit 18L Vedacit;Material;Vedacit
  // 234;Aditivo Plastificante Vedalit 3,6L Vedacit;Material;Vedacit
  // 62;Água;Serviço;Sabesp
  // 103;Amanco Cx sifão GR Vort Br Red DN100xDN100xDN50;Material;Amanco
  // 89;Amanco Esg cotovelo 45 100mm 4;Material;Amanco
  // 86;Amanco Esg cotovelo 45 40mm 11/2;Material;Amanco
  // 87;Amanco Esg cotovelo 45 50mm 2;Material;Amanco

  // Produto TESTE --------------------------------------------------------------\
  // @GetMapping("/produto/csv/{categoria}/{marca}")
  // public StringBuilder generateCSVProdutoReport (HttpServletResponse response, @PathVariable String categoria, @PathVariable String marca) throws Exception {
  //   String nomeArquivo = "produtos.csv";
  //   csvReport.formatCSVReport(response, nomeArquivo);

  //   // Cria o StringBuilder necessário para montar o arquivo.
  //   StringBuilder result = new StringBuilder();
  //   result.append("CODIGO;NOME;CATEGORIA;MARCA\n");   

  //   // Gera a lista de produtos a ser gravada.
  //   List<RelatorioProdutoDTO> listaProdutos = produtoRepository.obterRelatorioProduto(categoria, marca).stream().map(RelatorioProdutoDTO::new).toList();

  //   // Retorna as linhas e colunas do arquivo CSV.
  //   for (RelatorioProdutoDTO produto : listaProdutos) {
  //     result.append(produto.getCodigo()).append(";")
  //       .append(produto.getNome()).append(";")
  //       .append(produto.getCategoria()).append(";")
  //       .append(produto.getMarca()).append("\n");
  //   }

  //   // Retorna o StringBuilder.
  //   return result;
  // }

  ////////////////////////////////////////////////////////////////////////////////

}
