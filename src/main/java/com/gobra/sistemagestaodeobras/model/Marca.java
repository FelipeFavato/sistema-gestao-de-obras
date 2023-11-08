package com.gobra.sistemagestaodeobras.model;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gobra.sistemagestaodeobras.dto.MarcaRequestDTO;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;



@Table(name = "marca")
@Entity(name = "marca")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "codigo")
public class Marca {
  
  @Id
  @Column(name = "codigo")
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_gen_marca")
  @SequenceGenerator(name = "seq_gen_marca", sequenceName = "SEQ_MARCA", initialValue = 1, allocationSize = 1)  
  private Integer codigo;

  @Column(name = "nome", length = 80, unique = true)
  private String nome;

  @Column(name = "data_desativacao", length = 30)
  @Temporal(TemporalType.DATE)
  private Date dataDesativacao;

  // Uma marca para varios produtos.
  @JsonIgnore
  @OneToMany(mappedBy = "marca")
  private List<Produto> produtos;

  public Marca(MarcaRequestDTO data) {
    this.nome = data.nome();
    this.dataDesativacao = data.dataDesativacao();
    this.produtos = data.produtos();
  }
}
