package com.gobra.sistemagestaodeobras.model;

import com.gobra.sistemagestaodeobras.dto.FornecedorRequestDTO;
import com.gobra.sistemagestaodeobras.utils.TipoFornecedorEnum;

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

@Table(name = "fornecedor")
@Entity(name = "fornecedor")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "codigo")
public class Fornecedor {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_generator_fornecedor")
  @SequenceGenerator(name = "seq_generator_fornecedor", sequenceName = "SEQUENCIA_FORNECEDOR", initialValue = 1, allocationSize = 1)
  private Integer codigo;

  @Column(name = "nome", length = 30, unique = true)
  private String nome;

  @Column(name = "telefone", length = 20, unique = true) 
  private String telefone;

  @Column(name = "endereço", length = 100, unique = true)
  private String endereco;

  @Enumerated(EnumType.STRING)
  @Column(name = "tipo_fornecedor", length = 10)
  private TipoFornecedorEnum tipoFornecedor;

  public Fornecedor(FornecedorRequestDTO data) {
    this.nome = data.nome();
    this.telefone = data.telefone();
    this.endereco = data.endereco();
    this.tipoFornecedor = data.tipoFornecedor();
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