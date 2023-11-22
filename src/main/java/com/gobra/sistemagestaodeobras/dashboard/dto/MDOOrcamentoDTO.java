package com.gobra.sistemagestaodeobras.dashboard.dto;

import com.gobra.sistemagestaodeobras.dashboard.projection.MDOGastoComprasOrcamentoProjection;

public class MDOOrcamentoDTO {
  
  private Integer idObra;

  private String nomeObra;

  private double custoMaoDeObra;

  private Integer orcamentoMaoDeObra;

  private double valorTotal;

  private Integer custoPrevisto;


  public MDOOrcamentoDTO() {

  }

  public MDOOrcamentoDTO(Integer idObra, String nomeObra, double custoMaoDeObra,
  Integer orcamentoMaoDeObra, double valorTotal, Integer custoPrevisto) {
    this.idObra = idObra;
    this.nomeObra = nomeObra;
    this.custoMaoDeObra = custoMaoDeObra;
    this.orcamentoMaoDeObra = orcamentoMaoDeObra;
    this.valorTotal = valorTotal;
    this.custoPrevisto = custoPrevisto;
  }

  public MDOOrcamentoDTO(MDOGastoComprasOrcamentoProjection projection) {
    idObra = projection.getIdObra();
    nomeObra = projection.getNomeObra();
    custoMaoDeObra = projection.getCustoMaoDeObra();
    orcamentoMaoDeObra = projection.getOrcamentoMaoDeObra();
    valorTotal = projection.getValorTotal();
    custoPrevisto = projection.getCustoPrevisto();
  }

  // Getters e Setters
  public Integer getIdObra () {
    return idObra;
  }

  public void setIdObra (Integer idObra) {
    this.idObra = idObra;
  }

  public String getNomeObra () {
    return nomeObra;
  }

  public void setNomeObra (String nomeObra) {
    this.nomeObra = nomeObra;
  }

  public double getCustoMaoDeObra () {
    return custoMaoDeObra;
  }

  public void setCustoMaoDeObra (double custoMaoDeObra) {
    this.custoMaoDeObra = custoMaoDeObra;
  }

  public Integer getOrcamentoMaoDeObra () {
    return orcamentoMaoDeObra;
  }

  public void setOrcamentoMaoDeObra (Integer orcamentoMaoDeObra) {
    this.orcamentoMaoDeObra = orcamentoMaoDeObra;
  }

  public double getValorTotal () {
    return valorTotal;
  }

  public void getValorTotal (double valorTotal) {
    this.valorTotal = valorTotal;
  }

  public Integer getCustoPrevisto () {
    return custoPrevisto;
  }

  public void setCustoPrevisto (Integer custoPrevisto) {
    this.custoPrevisto = custoPrevisto;
  }

}
