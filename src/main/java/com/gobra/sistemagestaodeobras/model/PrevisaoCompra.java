package com.gobra.sistemagestaodeobras.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gobra.sistemagestaodeobras.dto.PrevisaoCompraRequestDTO;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;



@Table(name= "previsao_compra")
@Entity(name = "previsao_compra")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "codigo")
public class PrevisaoCompra {
  
  @Id
  @Column(name = "codigo")
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "previsao_compra_seq_gen")
  @SequenceGenerator(name = "previsao_compra_seq_gen", sequenceName = "PREVISAO_COMPRA_SEQ", initialValue = 1, allocationSize = 1)
  private Integer codigo;

  // ManyToOne: Serão MUITAS previsões de compra para UMA Obra.
  @JsonIgnore
  @ManyToOne
  @JoinColumn(name = "id_obra", referencedColumnName = "codigo")
  private Obra codigoObra;

  @Column(name = "descricao_produto_servico", length = 100)
  private String descricaoProdutoServico;

  @Column(name = "quantidade", length = 20)
  private Integer quantidade;

  @Column(name = "valor_unitario", length = 15)
  private double valorUnitario;

  @Column(name = "valor_total_previsto", length = 15)
  private double valorTotalPrevisto;

  @Column(name = "compra_prevista_realizada", length = 15)
  private boolean compraPrevistaRealizada;

  public PrevisaoCompra (PrevisaoCompraRequestDTO data) {
    this.codigoObra = data.codigoObra();
    this.descricaoProdutoServico = data.descricaoProdutoServico();
    this.quantidade = data.quantidade();
    this.valorUnitario = data.valorUnitario();
    this.valorTotalPrevisto = data.valorTotalPrevisto();
    this.compraPrevistaRealizada = data.compraPrevistaRealizada();
  }
}
