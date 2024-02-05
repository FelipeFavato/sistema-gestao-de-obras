package com.gobra.sistemagestaodeobras.relatorio.utils;


import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import com.gobra.sistemagestaodeobras.relatorio.dto.RelatorioProdutoDTO;
import com.opencsv.CSVWriter;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;

import jakarta.servlet.http.HttpServletResponse;


@Service
public class CSVReport {

  // Todos -----------------------------------------------------------------------------------------\
  public void formatCSVReport (HttpServletResponse response, String nomeArquivo) throws Exception {
    // Testar com ISO-8859-1 / UTF-8
    // HttpServletResponse formattedResponse = response;

    response.setCharacterEncoding("UTF-8");
    response.setContentType("text/csv;charset=UTF-8");
    response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + nomeArquivo);

    // Escrevendo a linha de configuração para o Excel ler corretamente o CSV em UTF-8
    // PrintWriter writer = response.getWriter();
    // writer.println("sep=");

    // Fechando o escritor para garantir que os dados sejam escritos após a linha de configuração
    // writer.flush();
    // writer.close();
  }

  ///////////////////////////////////////////////////////////////////////////////////////////////////

  // Produto ---------------------------------------------------------------------------------------\
  public StatefulBeanToCsv<RelatorioProdutoDTO> createProdutoCSVWriter(HttpServletResponse response) throws Exception {
  StatefulBeanToCsv<RelatorioProdutoDTO> writer = new StatefulBeanToCsvBuilder<RelatorioProdutoDTO>(response.getWriter())
        .withOrderedResults(false)
        .withMappingStrategy(this.mapeiaColunasProduto())
        .withQuotechar(CSVWriter.NO_QUOTE_CHARACTER) // Desativa o uso de aspas
        .withSeparator(';') // Define o ponto e vírgula como separador
        .build();

  return writer;
  }

  public ColumnPositionMappingStrategy<RelatorioProdutoDTO> mapeiaColunasProduto() {
  ColumnPositionMappingStrategy<RelatorioProdutoDTO> strategy = new ColumnPositionMappingStrategy<>();
  strategy.setType(RelatorioProdutoDTO.class);

  // Mapeia explicitamente as colunas na ordem desejada
  String[] colunas = {"codigo", "nome", "categoria", "marca"};
  strategy.setColumnMapping(colunas);

  return strategy;
  }

  public void setHeadersProduto (HttpServletResponse response) throws Exception {
    response.getWriter().write("CODIGO;NOME;CATEGORIA;MARCA\n");
  }
  ///////////////////////////////////////////////////////////////////////////////////////////////////

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

  // Produto TESTE ---------------------------------------------------------------------------------\
  
  ///////////////////////////////////////////////////////////////////////////////////////////////////

}
