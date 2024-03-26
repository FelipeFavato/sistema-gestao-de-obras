package com.gobra.sistemagestaodeobras.relatorio.dto;

import com.gobra.sistemagestaodeobras.relatorio.projection.RelatorioProdutoProjection;

public class RelatorioProdutoDTO {
  
  private String nomeLocalUso;

  private Integer codigoProduto;
  
  private String nomeProduto;

  private String tipoProduto;

  private String nomeMarca;


  public RelatorioProdutoDTO() {

  }

  public RelatorioProdutoDTO(
    String nomeLU, Integer codProd, String nomeProd,
    String tipoProd, String nomeMarca
  ) {
    this.nomeLocalUso = nomeLU;
    this.codigoProduto = codProd;
    this.nomeProduto = nomeProd;
    this.tipoProduto = tipoProd;
    this.nomeMarca = nomeMarca;
  }

  public RelatorioProdutoDTO(RelatorioProdutoProjection projection) {
    nomeLocalUso = projection.getNomeLocalUso();
    codigoProduto = projection.getCodigoProduto();
    nomeProduto = projection.getNomeProduto();
    tipoProduto = projection.getTipoProduto();
    nomeMarca = projection.getNomeMarca();
  }

  public String toString () {
    return nomeLocalUso + codigoProduto + nomeProduto + tipoProduto + nomeMarca;
  }


  // Getters e Setters
  public String getNomeLocalUso () {
    return nomeLocalUso;
  }

  public void setNomeLocalUso (String nomeLU) {
    this.nomeLocalUso = nomeLU;
  }

  public Integer getCodigoProduto () {
    return codigoProduto;
  }

  public void setCodigoProduto (Integer codProd) {
    this.codigoProduto = codProd;
  }

  public String getNomeProduto () {
    return nomeProduto;
  }

  public void setNomeProduto (String nomeProduto) {
    this.nomeProduto = nomeProduto;
  }

  public String getTipoProduto () {
    return tipoProduto;
  }

  public void setTipoProduto (String tipoProd) {
    this.tipoProduto = tipoProd;
  }

  public String getNomeMarca () {
    return nomeMarca;
  }

  public void setNomeMarca (String NomeMarca) {
    this.nomeMarca = NomeMarca;
  }

}
