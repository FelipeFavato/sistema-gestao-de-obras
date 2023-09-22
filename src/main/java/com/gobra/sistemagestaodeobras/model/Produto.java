package com.gobra.sistemagestaodeobras.model;

import com.gobra.sistemagestaodeobras.dto.ProdutoRequestDTO;
import com.gobra.sistemagestaodeobras.utils.TipoProdutoEnum;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "produto")
@Entity(name = "produto")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "codigo")
public class Produto {
  
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_generator_produto")
  @SequenceGenerator(name = "seq_generator_produto", sequenceName = "SEQUENCIA_PRODUTO", initialValue = 1, allocationSize = 1)  
  private Integer codigo;

  @Column(name = "nome", length = 30, unique = true)
  private String nome;

  @Enumerated(EnumType.STRING)
  @Column(name = "tipo_produto", length = 10)
  private TipoProdutoEnum tipoProduto;

  public Produto(ProdutoRequestDTO data) {
    this.nome = data.nome();
    this.tipoProduto = data.tipoProduto();
  }
}