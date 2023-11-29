package com.gobra.sistemagestaodeobras.dashboard.dto;

import com.gobra.sistemagestaodeobras.dashboard.projection.GastoPorFornecedorProjection;

public class GastoFornecedorDTO {
  
  private String nomeFornecedor;

  private String nomeProduto;

  private double valorTotal;

  public GastoFornecedorDTO() {

  }

  public GastoFornecedorDTO(String nomeF, String nomeP, double valor) {
    this.nomeFornecedor = nomeF;
    this.nomeProduto = nomeP;
    this.valorTotal = valor;
  }

  public GastoFornecedorDTO(GastoPorFornecedorProjection projection) {
    nomeFornecedor = projection.getNomeFornecedor();
    nomeProduto = projection.getNomeProduto();
    valorTotal = projection.getValorTotal();
  }

  // Getter e setters
  public String getNomeFornecedor () {
    return nomeFornecedor;
  }

  public void setNomeFornecedor (String nome) {
    this.nomeFornecedor = nome;
  }

  public String getNomeProduto () {
    return nomeProduto;
  }

  public void setNomeProduto (String nome) {
    this.nomeProduto = nome;
  }

  public double getValorTotal () {
    return valorTotal;
  }

  public void setValorTotal (double valor) {
    this.valorTotal = valor;
  }

}
