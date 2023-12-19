package com.gobra.sistemagestaodeobras.jasper.dto;

import com.gobra.sistemagestaodeobras.jasper.projection.JasperProdutoProjection;

public class JasperProdutoDTO {
  
  private Integer codigo;
  
  private String nome;

  private String categoria;

  private String marca;


  public JasperProdutoDTO() {

  }

  public JasperProdutoDTO(Integer codigo, String nome, String categoria, String marca) {
    this.codigo = codigo;
    this.nome = nome;
    this.categoria = categoria;
    this.marca = marca;
  }

  public JasperProdutoDTO(JasperProdutoProjection projection) {
    codigo = projection.getCodigo();
    nome = projection.getNome();
    categoria = projection.getCategoria();
    marca = projection.getMarca();
  }

  // Getters e Setters
  public Integer getCodigo () {
    return codigo;
  }

  public void setCodigo (Integer codigo) {
    this.codigo = codigo;
  }

  public String getNome () {
    return nome;
  }

  public void setNome (String nome) {
    this.nome = nome;
  }

  public String getCategoria () {
    return categoria;
  }

  public void setCategoria (String categoria) {
    this.categoria = categoria;
  }

  public String getMarca () {
    return marca;
  }

  public void setMarca (String marca) {
    this.marca = marca;
  }

}
