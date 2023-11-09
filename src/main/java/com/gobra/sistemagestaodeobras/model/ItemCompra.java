package com.gobra.sistemagestaodeobras.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gobra.sistemagestaodeobras.dto.ItemCompraRequestDTO;

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

@Table(name = "item_compra")
@Entity(name = "item_compra")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "codigo")
public class ItemCompra {
  
  @Id
  @Column(name = "codigo")
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "item_compra_seq_gen")
  @SequenceGenerator(name = "item_compra_seq_gen", sequenceName = "ITEM_COMPRA_SEQU", initialValue = 1, allocationSize = 1)
  private Integer codigo;

  // 1. ManyToOne: Muitos itens pertencem a 1 compra.
  @JsonIgnore
  @ManyToOne
  @JoinColumn(name = "id_compra", referencedColumnName = "codigo")
  private Compra compra;

  @JsonIgnore
  @ManyToOne
  @JoinColumn(name = "id_produto", referencedColumnName = "codigo")
  private Produto produto;

  @JsonIgnore
  @ManyToOne
  @JoinColumn(name = "id_local_uso", referencedColumnName = "codigo_local_uso_obra")
  private LocalUso localUso;

  // Muitos itens podem ter 1 tipo de unidade de medida.
  @JsonIgnore
  @ManyToOne
  @JoinColumn(name = "id_unidade_medida", referencedColumnName = "codigo")
  private UnidadeMedida unidadeMedida;

  @Column(name = "quantidade", length = 30)
  private int quantidade;

  @Column(name = "valor_unitario", length = 30)
  private double valorUnitario;

  @Column(name = "valor_total", length = 30)
  private double valorTotal;

  public ItemCompra(ItemCompraRequestDTO data) {
    this.compra = data.compra();
    this.produto = data.produto();
    this.localUso = data.localUso();
    this.unidadeMedida = data.unidadeMedida();
    this.quantidade = data.quantidade();
    this.valorUnitario = data.valorUnitario();
    this.valorTotal = data.valorTotal();
  }
}
