package com.gobra.sistemagestaodeobras.dashboard.dto;

import com.gobra.sistemagestaodeobras.dashboard.projection.DescontoProjection;

public class DescontoDTO {

  private double desconto;

  public DescontoDTO() {

  }

  public DescontoDTO(double desc) {
    this.desconto = desc;
  }

  public DescontoDTO(DescontoProjection projection) {
    desconto = projection.getDesconto();
  }

  // Getters e Setters
  public double getDesconto () {
    return desconto;
  }

  public void setDesconto (double desc) {
    this.desconto = desc;
  }
}
