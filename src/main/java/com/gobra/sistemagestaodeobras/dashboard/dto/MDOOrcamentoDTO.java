package com.gobra.sistemagestaodeobras.dashboard.dto;

import com.gobra.sistemagestaodeobras.dashboard.projection.MDOGastoComprasOrcamentoProjection;
import com.gobra.sistemagestaodeobras.utils.FormatadorMoeda;

public class MDOOrcamentoDTO {
  
  private Integer custoMaoDeObra;

  private Integer custoPrevisto;

  private double pagoMaoDeObra;

  private double valorDesconto;

  private double valorGastos;

  private double valorTotalPrevisto;


  public MDOOrcamentoDTO() {

  }

  public MDOOrcamentoDTO(Integer custoMDO, Integer custoP, double pagoMDO,
  double valorD, double valorG, double vTotalP) {
    this.custoMaoDeObra = custoMDO;
    this.custoPrevisto = custoP;
    this.pagoMaoDeObra = pagoMDO;
    this.valorDesconto = valorD;
    this.valorGastos = valorG;
    this.valorTotalPrevisto = vTotalP;
  }

  public MDOOrcamentoDTO(MDOGastoComprasOrcamentoProjection projection) {
    custoMaoDeObra = projection.getCustoMaoDeObra();
    custoPrevisto = projection.getCustoPrevisto();
    pagoMaoDeObra = projection.getPagoMaoDeObra();
    valorDesconto = projection.getValorDesconto();
    valorGastos = projection.getValorGastos();
    valorTotalPrevisto = projection.getValorTotalPrevisto();
  }

  public String toString() {
    return  "Custo total previsto: " + FormatadorMoeda.formatarMoeda(custoPrevisto) +
            "\n Comprometido mão de obra: " + FormatadorMoeda.formatarMoeda(custoMaoDeObra - pagoMaoDeObra) +
            "\n Gasto atual - Material/Serviço: " + FormatadorMoeda.formatarMoeda(valorGastos) +
            "\n Gasto atual - Mão de obra: " + FormatadorMoeda.formatarMoeda(pagoMaoDeObra) +
            "\n Total de descontos: " + FormatadorMoeda.formatarMoeda(valorDesconto) +
            "\n Saldo para investimento: " + FormatadorMoeda.formatarMoeda(custoPrevisto - (custoMaoDeObra + valorGastos)) +
            "\n Valor total de compras previstas: " + FormatadorMoeda.formatarMoeda(valorTotalPrevisto);
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

  public double getValorTotalPrevisto () {
    return valorTotalPrevisto;
  }

  public void setValorTotalPrevisto (double vTotalP) {
    this.valorTotalPrevisto = vTotalP;
  }

}
