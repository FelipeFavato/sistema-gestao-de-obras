package com.gobra.sistemagestaodeobras.bot.dto;

import com.gobra.sistemagestaodeobras.bot.projection.OrcamentoBotProjection;
import com.gobra.sistemagestaodeobras.utils.FormatadorMoeda;

public class OrcamentoBotDTO {

  private String nomeObra;

  private Integer custoMaoDeObra;

  private Integer custoPrevisto;

  private double pagoMaoDeObra;

  private double valorDesconto;

  private double valorGastos;


  public OrcamentoBotDTO() {

  }

  public OrcamentoBotDTO(String nome, Integer custoMDO, Integer custoP, double pagoMDO,
  double valorD, double valorG) {
    this.nomeObra = nome;
    this.custoMaoDeObra = custoMDO;
    this.custoPrevisto = custoP;
    this.pagoMaoDeObra = pagoMDO;
    this.valorDesconto = valorD;
    this.valorGastos = valorG;
  }

  public OrcamentoBotDTO(OrcamentoBotProjection projection) {
    nomeObra = projection.getNomeObra();
    custoMaoDeObra = projection.getCustoMaoDeObra();
    custoPrevisto = projection.getCustoPrevisto();
    pagoMaoDeObra = projection.getPagoMaoDeObra();
    valorDesconto = projection.getValorDesconto();
    valorGastos = projection.getValorGastos();
  }

  public String toString() {
    return "Obra: " + "<b>" + nomeObra + "</b>" + "\n" +
           "\n" +
           "Custo total previsto da obra: " + "<b>" + FormatadorMoeda.formatarMoeda(custoPrevisto) + "</b>" + "\n" +
           "Custo previsto mão de obra: " + "<b>" + FormatadorMoeda.formatarMoeda(custoMaoDeObra) + "</b>" + "\n" +
           "\n" +
           "Gasto atual - Material/Serviço: " + "<b>" + FormatadorMoeda.formatarMoeda(valorGastos) + "</b>" + "\n" +
           "Gasto atual - Descontos obtidos: " + "<b>" + FormatadorMoeda.formatarMoeda(valorDesconto) + "</b>" + "\n" +
           "Gasto atual - Mão de obra: " + "<b>" + FormatadorMoeda.formatarMoeda(pagoMaoDeObra) + "</b>" + "\n" +
           "\n" +
           "<b>" + "Valor total investido: " + FormatadorMoeda.formatarMoeda(pagoMaoDeObra + valorGastos) + "</b>" + "\n" +
           "<b>" + "Saldo para investimento: " + FormatadorMoeda.formatarMoeda(custoPrevisto - (custoMaoDeObra + valorGastos)) + "</b>";
  }


  // Getters e Setters
  public String getNomeObra () {
    return nomeObra;
  }

  public void setNomeObra (String nome) {
    this.nomeObra = nome;
  }

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
