package com.gobra.sistemagestaodeobras.jasper.dto;

import com.gobra.sistemagestaodeobras.jasper.projection.JasperProdutoProjection;

public class JasperProdutoDTO {
  
  private Integer codigoProduto;
  
  private String nomeProduto;


  public JasperProdutoDTO() {

  }

  public JasperProdutoDTO(Integer codigo, String nome) {
    this.codigoProduto = codigo;
    this.nomeProduto = nome;
  }

  public JasperProdutoDTO(JasperProdutoProjection projection) {
    codigoProduto = projection.getCodigoProduto();
    nomeProduto = projection.getNomeProduto();
  }

  // Getters e Setters
  public Integer getCodigoProduto () {
    return codigoProduto;
  }

  public void setCodigoProduto (Integer codigo) {
    this.codigoProduto = codigo;
  }

  public String getNomeProduto () {
    return nomeProduto;
  }

  public void setNomeProduto (String nome) {
    this.nomeProduto = nome;
  }

}
