package com.gobra.sistemagestaodeobras.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gobra.sistemagestaodeobras.dto.FornecedorRequestDTO;
import com.gobra.sistemagestaodeobras.utils.TipoFornecedorEnum;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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

@Table(name = "fornecedor")
@Entity(name = "fornecedor")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "codigo")
public class Fornecedor {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_gen_fornecedor")
  @SequenceGenerator(name = "seq_gen_fornecedor", sequenceName = "SEQ_FORNECEDOR", initialValue = 1, allocationSize = 1)
  private Integer codigo;

  @Column(name = "nome", length = 70, unique = true)
  private String nome;

  @Column(name = "telefone", length = 20)
  private String telefone;

  @Column(name = "endereço", length = 150)
  private String endereco;

  @Column(name = "email", length = 100)
  private String email;

  @Enumerated(EnumType.STRING)
  @Column(name = "tipo_fornecedor", length = 10)
  private TipoFornecedorEnum tipoFornecedor;

  // 1. OneToMany: Cada Fornecedor terá uma lista de compras atrelada a ele.
  @JsonIgnore
  @OneToMany(mappedBy = "fornecedor")
  private List<Compra> compras;

  public Fornecedor(FornecedorRequestDTO data) {
    this.nome = data.nome();
    this.telefone = data.telefone();
    this.endereco = data.endereco();
    this.email = data.email();
    this.tipoFornecedor = data.tipoFornecedor();
    this.compras = data.compras();
  }
}

// @Size(min = tamanho_minimo, max = tamanho_maximo):
// Essa anotação permite definir limites mínimo e máximo
// para uma coleção, como uma lista ou conjunto.
// Isso pode ser usado para restringir o número de
// elementos em uma coleção.

// @Digits(integer = digitos_inteiros, fraction = digitos_decimais):
// Essa anotação é usada para campos numéricos, permitindo que você
// especifique o número de dígitos na parte inteira e decimal do valor.

// @NotNull: Esta anotação indica que o campo não pode ser nulo.
// O JPA lançará uma exceção se você tentar salvar uma entidade
// com um valor nulo no campo anotado.

// @Column(name = "nome_coluna", length = tamanho_maximo):
// Esta anotação permite que você especifique o nome da coluna
// no banco de dados e o comprimento máximo dos dados na coluna. 