package com.gobra.sistemagestaodeobras.dashboard.dto;

import java.util.Date;

import com.gobra.sistemagestaodeobras.dashboard.projection.AcumuladoGastosProjection;

public class AcumuladoGastosDTO {
  
  private Integer idObra;

  private Date dataVencimento;

  private double valorFinal;

  public AcumuladoGastosDTO() {

  }

  public AcumuladoGastosDTO(Integer idObra, Date dataVencimento, double valorFinal) {
    this.idObra = idObra;
    this.dataVencimento = dataVencimento;
    this.valorFinal = valorFinal;
  }

  public AcumuladoGastosDTO(AcumuladoGastosProjection projection) {
    idObra = projection.getIdObra();
    dataVencimento = projection.getDataVencimento();
    valorFinal = projection.getValorFinal();
  }

  // Getters e Setters
  public Integer getIdObra() {
    return idObra;
  }

  public void setIdObra(Integer idObra) {
    this.idObra = idObra;
  }

  public Date getDataVencimento() {
    return dataVencimento; 
  }

  public void setDataVencimento (Date dataVencimento) {
    this.dataVencimento = dataVencimento;
  }

  public double getValorFinal () {
    return valorFinal;
  }

  public void setValorFinal (double valorFinal) {
    this.valorFinal = valorFinal;
  }

}
