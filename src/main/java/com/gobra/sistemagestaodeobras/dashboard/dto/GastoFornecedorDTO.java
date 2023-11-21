package com.gobra.sistemagestaodeobras.dashboard.dto;

import com.gobra.sistemagestaodeobras.dashboard.projection.GastoPorFornecedorProjection;

public class GastoFornecedorDTO {
  
  private String nomeFornecedor;

  private double valorFinal;

  public GastoFornecedorDTO() {

  }

  public GastoFornecedorDTO(String nome, double valor) {
    this.nomeFornecedor = nome;
    this.valorFinal = valor;
  }

  public GastoFornecedorDTO(GastoPorFornecedorProjection projection) {
    nomeFornecedor = projection.getNomeFornecedor();
    valorFinal = projection.getValorFinal();
  }

  // Getter e setters
  public String getNomeFornecedor () {
    return nomeFornecedor;
  }

  public void setNomeFornecedor (String nome) {
    this.nomeFornecedor = nome;
  }

  public double getValorFinal () {
    return valorFinal;
  }

  public void setValorFinal (double valor) {
    this.valorFinal = valor;
  }

}
