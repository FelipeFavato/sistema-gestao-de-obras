package com.gobra.sistemagestaodeobras.model;

import java.sql.Date;

public class Obra {
  // private: Variável só pode ser acessada dentro da própria classe.
  // static: Variável pertence a classe em vez de a instâncias individuais.
  // final: O valor não pode ser alterado depois de ser atribuido.
  // Long: Tipo de dado de numeros inteiros longos.
  // serialVersionUID: Usada para controlar a versão da classe ao serializar
  //      e desserializar objetos.
  // 1L: O valor atribuido a variavel é 1L (um literal longo).
  // private static final Long serialVersionUID = 1L;

  private Integer codigo;

  private String nome;

  private String endereco;

  private Date dataInicio;

  private Date dataPrevistaFim;

  private Date dataRealFim;

  private Integer custoPrevisto;

  // Constructor => é necessário?
  // Teoricamente o codigo será gerado pelo Banco de Dados, então
  // não preciso receber no construtor. 
  // public Obra(
  //   // Integer codigo,
  //   String nome,
  //   String endereco,
  //   Date dataInicio,
  //   Date dataPrevistaFim,
  //   Date dataRealFim,
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
  public Integer getCodigo() {
    return codigo;
  }

  public void setCodigo(Integer codigo) {
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
  public Date getDataInicio() {
    return dataInicio;
  }

  public void setDataInicio(Date dataInicio) {
    this.dataInicio = dataInicio;
  }

  // dataPrevistaFim => Getter e Setter
  public Date getDataPrevistaFim() {
    return dataPrevistaFim;
  }

  public void setDataPrevistaFim(Date dataPrevistaFim) {
    this.dataPrevistaFim = dataPrevistaFim;
  }
 
  // dataRealFim => Getter e Setter
  public Date getDataRealFim() {
    return dataRealFim;
  }

  public void setDataRealFim(Date dataRealFim) {
    this.dataRealFim = dataRealFim;
  }

  // custoPrevisto => Getter e Setter
  public Integer getCustoPrevisto() {
    return custoPrevisto;
  }

  public void setCustoPrevisto(Integer custoPrevisto) {
    this.custoPrevisto = custoPrevisto;
  }

  // Método que fornece uma representação em formato de string legível
  // para objetos dessa classe
  @Override  // Sobrecarga de método => Reescreve um método existente da superclasse
	public String toString(){
		return "{Codigo="+codigo+",Nome="+nome+",Endereço="+endereco+"}";
	}

}
