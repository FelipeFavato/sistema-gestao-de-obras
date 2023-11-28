package com.gobra.sistemagestaodeobras.dashboard.dto;

import com.gobra.sistemagestaodeobras.dashboard.projection.ItemLocalProjection;

public class ItemLocalDTO {
  
  private String nomeLocalUsoObra;

  private String nomeFornecedor;

  private double valorTotal;

  public ItemLocalDTO() {

  }

  public ItemLocalDTO(String nomeLocalUsoObra, String nomeFornecedor, double valorTotal) {
    this.nomeLocalUsoObra = nomeLocalUsoObra;
    this.nomeFornecedor = nomeFornecedor;
    this.valorTotal = valorTotal;
  }

  public ItemLocalDTO(ItemLocalProjection projection) {
    nomeLocalUsoObra = projection.getNomeLocalUsoObra();
    nomeFornecedor = projection.getNomeFornecedor();
    valorTotal = projection.getValorTotal();
  }

  // Getter e Setters
  public String getNomeLocalUsoObra() {
    return nomeLocalUsoObra;
  }

  public void setNomeLocalUsoObra(String nome) {
    this.nomeLocalUsoObra = nome;
  }

  public String getNomeFornecedor () {
    return nomeFornecedor;
  }

  public void setNomeFornecedor(String nome) {
    this.nomeFornecedor = nome;
  }

  public double getValorTotal() {
    return valorTotal;
  }

  public void setValorTotal(double valor) {
    this.valorTotal = valor;
  }

}
