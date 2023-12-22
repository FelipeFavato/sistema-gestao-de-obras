package com.gobra.sistemagestaodeobras.jasper.service;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

  public byte[] exportReport (String categoria, String marca) throws FileNotFoundException, JRException {
    // String userHome = System.getProperty("user.home");
    // String path = userHome + "/Downloads";
    List<JasperProdutoDTO> produtos = produtoRepository.obterRelatorioJasperProduto(categoria, marca).stream().map(JasperProdutoDTO::new).toList();

    // Carrega e compila o arquivo
    // File file = ResourceUtils.getFile("classpath:jasperproduto.jrxml");

    InputStream produtoReportStream = getClass().getResourceAsStream("/jasperproduto.jrxml");
    JasperReport jasperReport = JasperCompileManager.compileReport(produtoReportStream);
    // JRSaver.saveObject(jasperReport, "produtoReport.jasper");


    // FileInputStream fis = new FileInputStream(file);
    // JasperReport jasperReport = JasperCompileManager.compileReport(fis);
    JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(produtos);

    Map<String, Object> parameters = new HashMap<>();
    parameters.put("createdBy", "Felipe");
    JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);

    // https://www.whalebonebuildingsupplies.co.uk/uploads/1/8/0/0/18007699/icon-construction-manager_orig.png
    // "https://www.google.com/url?q=https%3A%2F%2Flogocapaceteprojeto.png&sa=D&sntz=1&usg=AOvVaw09nmWljOAdTl1sL2BiEicZ"
    // if (reportFormat.equalsIgnoreCase("html")) {
    //   JasperExportManager.exportReportToHtmlFile(jasperPrint, path+"/produtosinfo.html");
    // }

    // if (reportFormat.equalsIgnoreCase("pdf")) {
    //   JasperExportManager.exportReportToPdfFile(jasperPrint, path+"/produtosinfo.pdf");
    // }

    // Exporta o relatório para um array de bytes
    byte[] pdfBytes = JasperExportManager.exportReportToPdf(jasperPrint);

    return pdfBytes;
  }

}
