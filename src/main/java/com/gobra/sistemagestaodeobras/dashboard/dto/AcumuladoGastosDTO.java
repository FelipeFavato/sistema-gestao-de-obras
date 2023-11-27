package com.gobra.sistemagestaodeobras.dashboard.dto;

import java.util.Date;

import com.gobra.sistemagestaodeobras.dashboard.projection.AcumuladoGastosProjection;

public class AcumuladoGastosDTO {
  
  private Date dataVencimento;

  private double gastoMaoDeObra;

  private double gastos;

  private double gastoTotal;

  public AcumuladoGastosDTO() {

  }

  public AcumuladoGastosDTO(Date dataVencimento, double gastoMDO, double gastos, double gastoTotal) {
    this.dataVencimento = dataVencimento;
    this.gastoMaoDeObra = gastoMDO;
    this.gastos = gastos;
    this.gastoTotal = gastoTotal;
  }

  public AcumuladoGastosDTO(AcumuladoGastosProjection projection) {
    dataVencimento = projection.getDataVencimento();
    gastoMaoDeObra = projection.getGastoMaoDeObra();
    gastos = projection.getGastos();
    gastoTotal = projection.getGastoTotal();
  }

  // Getters e Setters
  public Date getDataVencimento() {
    return dataVencimento; 
  }

  public void setDataVencimento (Date dataVencimento) {
    this.dataVencimento = dataVencimento;
  }

  public double getGastoMaoDeObra () {
    return gastoMaoDeObra;
  }

  public void setGastoMaoDeObra (double gastoMaoDeObra) {
    this.gastoMaoDeObra = gastoMaoDeObra;
  }

  public double getGastos () {
    return gastos;
  }

  public void setGastos (double gastos) {
    this.gastos = gastos;
  }

  public double getGastoTotal () {
    return gastoTotal;
  }

  public void setGastoTotal (double gastoTotal) {
    this.gastoTotal = gastoTotal;
  }

}
