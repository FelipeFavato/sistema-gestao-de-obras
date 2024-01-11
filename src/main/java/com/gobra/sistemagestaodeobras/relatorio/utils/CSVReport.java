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

	// Todos -------------------------------------------------------------------------------------------
	public void formatCSVReport (HttpServletResponse response, String nomeArquivo) throws Exception {
    response.setCharacterEncoding("UTF-8");
    response.setContentType("text/csv;charset=UTF-8"); // 
    response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + nomeArquivo);
	}

	// --------------------------------------------------------------------------------------------------
	
	// Produto -----------------------------------------------------------------------------------------
	public StatefulBeanToCsv<RelatorioProdutoDTO> createProdutoCSVWriter(HttpServletResponse response) throws Exception {
    StatefulBeanToCsv<RelatorioProdutoDTO> writer = new StatefulBeanToCsvBuilder<RelatorioProdutoDTO>(response.getWriter())
            .withOrderedResults(true)
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
        response.getWriter().write("\uFEFF"); 
		response.getWriter().write("CODIGO;NOME;CATEGORIA;MARCA\n");
	}
	// --------------------------------------------------------------------------------------------------

}
