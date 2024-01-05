package com.gobra.sistemagestaodeobras.relatorio.utils;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@Service
public class ExportPDFReport {
  
  public byte[] exportPDFReport (String caminhoArquivo, List<?> listaItens) throws FileNotFoundException, JRException {
    InputStream reportStream = getClass().getResourceAsStream(caminhoArquivo);
    JasperReport jasperReport = JasperCompileManager.compileReport(reportStream);
    JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(listaItens);

    Map<String, Object> parameters = new HashMap<>();
    parameters.put("createdBy", "Felipe");
    JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);

    byte[] pdfBytes = JasperExportManager.exportReportToPdf(jasperPrint);

    return pdfBytes;
  }
}
