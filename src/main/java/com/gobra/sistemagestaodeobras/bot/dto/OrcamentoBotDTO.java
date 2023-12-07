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
    return "Obra: " + nomeObra + "\n" +
           "\n" +
           "Custo total previsto da obra: " + FormatadorMoeda.formatarMoeda(custoPrevisto) + "\n" +
           "Custo previsto mão de obra: " + FormatadorMoeda.formatarMoeda(custoMaoDeObra) + "\n" +
           "Saldo para investimento: " + FormatadorMoeda.formatarMoeda(custoPrevisto - (custoMaoDeObra + valorGastos)) + "\n" +
           "\n" +
           "Gasto atual - Material/Serviço: " + FormatadorMoeda.formatarMoeda(valorGastos) + "\n" +
           "Gasto atual - Descontos obtidos: " + FormatadorMoeda.formatarMoeda(valorDesconto) + "\n" +
           "Gasto atual - Mão de obra: " + FormatadorMoeda.formatarMoeda(pagoMaoDeObra);
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
