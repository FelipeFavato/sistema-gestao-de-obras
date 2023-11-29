package com.gobra.sistemagestaodeobras.dashboard.dto;

import com.gobra.sistemagestaodeobras.dashboard.projection.MDOGastoComprasOrcamentoProjection;

public class MDOOrcamentoDTO {
  
  private Integer custoMaoDeObra;

  private Integer custoPrevisto;

  private double pagoMaoDeObra;

  private double valorDesconto;

  private double valorGastos;


  public MDOOrcamentoDTO() {

  }

  public MDOOrcamentoDTO(Integer custoMDO, Integer custoP, double pagoMDO,
  double valorD, double valorG) {
    this.custoMaoDeObra = custoMDO;
    this.custoPrevisto = custoP;
    this.pagoMaoDeObra = pagoMDO;
    this.valorDesconto = valorD;
    this.valorGastos = valorG;
  }

  public MDOOrcamentoDTO(MDOGastoComprasOrcamentoProjection projection) {
    custoMaoDeObra = projection.getCustoMaoDeObra();
    custoPrevisto = projection.getCustoPrevisto();
    pagoMaoDeObra = projection.getPagoMaoDeObra();
    valorDesconto = projection.getValorDesconto();
    valorGastos = projection.getValorGastos();
  }

  // Getters e Setters
  public Integer getCustoMaoDeObra () {
    return custoMaoDeObra;
  }

  public void setCustoMaoDeObra(Integer custoP) {
    this.custoMaoDeObra = custoP;
  }

  public Integer getCustoPrevisto () {
    return custoPrevisto;
  }

  public void setCustoPrevisto(Integer custoP) {
    this.custoPrevisto = custoP;
  }

  public double getPagoMaoDeObra () {
    return pagoMaoDeObra;
  }

  public void setPagoMaoDeObra (double pagoMDO) {
    this.pagoMaoDeObra = pagoMDO;
  }

  public double getValorDesconto () {
    return valorDesconto;
  }

  public void setValorDesconto (double valorD) {
    this.valorDesconto = valorD;
  }

  public double getValorGastos () {
    return valorGastos;
  }

  public void setValorGastos (double valorG) {
    this.valorGastos = valorG;
  }

}
