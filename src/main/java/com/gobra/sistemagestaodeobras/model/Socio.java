package com.gobra.sistemagestaodeobras.model;

import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gobra.sistemagestaodeobras.dto.SocioRequestDTO;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;



@Table(name = "socio")
@Entity(name = "socio")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "codigo")
public class Socio {
  
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_generator_socio")
  @SequenceGenerator(name = "seq_generator_socio", sequenceName = "SEQUENCIA_SOCIO", initialValue = 1, allocationSize = 1)
  private Integer codigo;

  @Column(name = "nome", length = 100, unique = true)
  private String nome;

  // Cada Sócio tera uma lista de compras atrelada a ele.
  @JsonIgnore
  @OneToMany(mappedBy = "socio")
  private List<Compra> compras;

  // Um sócio poderá participar de muitas obras.
  // Uma obra poderá ter muitos sócios participando.
  @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinTable(name = "socios_obras",
  joinColumns = {
    @JoinColumn(name = "id_socio", referencedColumnName = "codigo")
  },
  inverseJoinColumns = {
    @JoinColumn(name = "id_obra", referencedColumnName = "codigo")
  })
  @JsonIgnore
  private Set<Obra> obras;

  public Socio(SocioRequestDTO data) {
    this.nome = data.nome();
    this.compras = data.compras();
  }
}
