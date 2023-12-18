package com.gobra.sistemagestaodeobras.jasper.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import com.gobra.sistemagestaodeobras.jasper.dto.JasperProdutoDTO;
import com.gobra.sistemagestaodeobras.repository.ProdutoRepository;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;



@Service
public class JasperService {

  @Autowired
  private ProdutoRepository produtoRepository;

  public String exportReport (String reportFormat) throws FileNotFoundException, JRException {
    String path = "/home/felipe/ProjetoFF";
    List<JasperProdutoDTO> produtos = produtoRepository.obterProdutoNomeCod().stream().map(JasperProdutoDTO::new).toList();

    // Carrega e compila o arquivo
    File file = ResourceUtils.getFile("classpath:jasperproduto.jrxml");
    JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
    JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(produtos);

    Map<String, Object> parameters = new HashMap<>();
    parameters.put("createdBy", "Felipe");
    JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);

    if (reportFormat.equalsIgnoreCase("html")) {
      JasperExportManager.exportReportToHtmlFile(jasperPrint, path+"/produtosinfo.html");
    }

    if (reportFormat.equalsIgnoreCase("pdf")) {
      JasperExportManager.exportReportToPdfFile(jasperPrint, path+"/produtosinfo.pdf");
    }

    return "Relatório gerado no caminho: " + path;
  }

}
