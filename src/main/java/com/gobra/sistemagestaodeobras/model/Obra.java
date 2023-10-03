package com.gobra.sistemagestaodeobras.model;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity(name = "obra")
public class Obra {
  // private: Variável só pode ser acessada dentro da própria classe.
  // static: Variável pertence a classe em vez de a instâncias individuais.
  // final: O valor não pode ser alterado depois de ser atribuido.
  // Long: Tipo de dado de numeros inteiros longos.
  // serialVersionUID: Usada para controlar a versão da classe ao serializar
  //      e desserializar objetos.
  // 1L: O valor atribuido a variavel é 1L (um literal longo).
  // private static final Long serialVersionUID = 1L;

  @Id
  private int codigo;

  private String nome;

  private String endereco;

  private LocalDate dataInicio;

  private LocalDate dataPrevistaFim;

  private LocalDate dataRealFim;

  private Integer custoPrevisto;

  @JsonIgnore
  @OneToMany(mappedBy = "obra")
  private List<Compra> compras;

  // Constructor => é necessário?
  // Teoricamente o codigo será gerado pelo Banco de Dados, então
  // não preciso receber no construtor. 
  // public Obra(
  //   // Integer codigo,
  //   String nome,
  //   String endereco,
  //   LocalDate dataInicio,
  //   LocalDate dataPrevistaFim,
  //   LocalDate dataRealFim,
  //   Integer custoPrevisto
  //   ) {
  //   // this.codigo = codigo;
  //   this.nome = nome;
  //   this.endereco = endereco;
  //   this.dataInicio = dataInicio;
  //   this.dataPrevistaFim = dataPrevistaFim;
  //   this.dataRealFim = dataRealFim;
  //   this.custoPrevisto = custoPrevisto;
  // }

  // codigo => Getter e Setter
  public int getCodigo() {
    return codigo;
  }

  public void setCodigo(int codigo) {
    this.codigo = codigo;
  }

  // nome => Getter e Setter
  public String getNome() {
    return nome;
  }

  public void setNome(String nome) {
    this.nome = nome;
  }

  // endereco => Getter e Setter
  public String getEndereco() {
    return endereco;
  }

  public void setEndereco(String endereco) {
    this.endereco = endereco;
  }

  // dataInicio => Getter e Setter
  public LocalDate getDataInicio() {
    return dataInicio;
  }

  public void setDataInicio(LocalDate dataInicio) {
    this.dataInicio = dataInicio;
  }

  // dataPrevistaFim => Getter e Setter
  public LocalDate getDataPrevistaFim() {
    return dataPrevistaFim;
  }

  public void setDataPrevistaFim(LocalDate dataPrevistaFim) {
    this.dataPrevistaFim = dataPrevistaFim;
  }
 
  // dataRealFim => Getter e Setter
  public LocalDate getDataRealFim() {
    return dataRealFim;
  }

  public void setDataRealFim(LocalDate dataRealFim) {
    this.dataRealFim = dataRealFim;
  }

  // custoPrevisto => Getter e Setter
  public Integer getCustoPrevisto() {
    return custoPrevisto;
  }

  public void setCustoPrevisto(Integer custoPrevisto) {
    this.custoPrevisto = custoPrevisto;
  }

  // Lista de Obras => Getter e Setter
  public List<Compra> getCompras() {
    return compras;
  }

  public void setCompras(List<Compra> compras) {
    this.compras = compras;
  }

  // Método que fornece uma representação em formato de string legível
  // para objetos dessa classe
  @Override  // Sobrecarga de método => Reescreve um método existente da superclasse
	public String toString(){
		return "{Codigo="+codigo+",Nome="+nome+",Endereço="+endereco+"}";
	}

}