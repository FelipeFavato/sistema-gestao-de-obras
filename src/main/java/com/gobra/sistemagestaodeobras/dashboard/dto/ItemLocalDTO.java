package com.gobra.sistemagestaodeobras.dashboard.dto;

import com.gobra.sistemagestaodeobras.dashboard.projection.ItemLocalProjection;

public class ItemLocalDTO {
  
  private String nomeLocalUsoObra;

  private double valorTotal;

  public ItemLocalDTO() {

  }

  public ItemLocalDTO(String nomeLocalUsoObra, double valorTotal) {
    this.nomeLocalUsoObra = nomeLocalUsoObra;
    this.valorTotal = valorTotal;
  }

  public ItemLocalDTO(ItemLocalProjection projection) {
    nomeLocalUsoObra = projection.getNomeLocalUsoObra();
    valorTotal = projection.getValorTotal();
  }

  // Getter e Setters
  public String getNomeLocalUsoObra() {
    return nomeLocalUsoObra;
  }

  public void setNomeLocalUsoObra(String nome) {
    this.nomeLocalUsoObra = nome;
  }

  public double getValorTotal() {
    return valorTotal;
  }

  public void setValorTotal(double valor) {
    this.valorTotal = valor;
  }

}
