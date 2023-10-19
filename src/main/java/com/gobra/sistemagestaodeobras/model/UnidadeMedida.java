package com.gobra.sistemagestaodeobras.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gobra.sistemagestaodeobras.dto.UnidadeMedidaRequestDTO;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "unidade_medida")
@Entity(name = "unidade_medida")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "codigo")
public class UnidadeMedida {
  
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_generator_unidade_medida")
  @SequenceGenerator(name = "seq_generator_unidade_medida", sequenceName = "SEQUENCIA_UNIDADE_MEDIDA", initialValue = 1, allocationSize = 1)
  private Integer codigo;

  @Column(name = "unidade", length = 30, unique = true)
  private String unidade;

  // 1 tipo de medida por ser usado por muitos itens.
  @JsonIgnore
  @OneToMany(mappedBy = "unidadeMedida")
  private List<ItemCompra> itensCompra;

  public UnidadeMedida(UnidadeMedidaRequestDTO data) {
    this.unidade = data.unidade();
    this.itensCompra = data.itensCompra();
  }
}
