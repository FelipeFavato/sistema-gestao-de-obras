// Classe da entidade => Classe que representa a
// tabela no banco de dados relacional (Postgres)

package com.gobra.sistemagestaodeobras.model;

import java.util.Date;

import com.gobra.sistemagestaodeobras.dto.LocalUsoRequestDTO;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;




@Table(name = "localuso")  // Essa notação indica que essa classe é uma tabela
@Entity(name = "localuso")  // Essa notação da um nome à entidade
@Getter // Lombok gera todos os métodos de get de cada atributo
@Setter // Lombok gera todos os métodos de set de cada atributo
@NoArgsConstructor // Lombok declara um constructor que nao recebe nenhum argumento
@AllArgsConstructor // Lombok declara um constructor que recebe todos os argumentos
@EqualsAndHashCode(of = "codigoLocalUsoObra") // O Id é a representação unica
public class LocalUso {
  // @column(name = "nome_da_coluna", size/len)
  // @Id => PrimaryKey || AUTO => Gera automaticamente ordenadamente
  // @GeneratedValue(strategy = GenerationType.AUTO) // Toda vez que reinicio a aplicação, adiciona 50 na geração (AllocationSize)
  @Id
  @Column(name = "codigo_local_uso_obra")
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sua_seq_generator")
  @SequenceGenerator(name = "sua_seq_generator", sequenceName = "NOME_DA_SUA_SEQUENCIA", initialValue = 1, allocationSize = 1)
  private Integer codigoLocalUsoObra;

  // @column => Especifica o nome da coluna na tabela
  @Column(name = "nome_local_uso_obra", length = 30, unique = true)
  private String nomeLocalUsoObra;

  @Column(name = "data_desativacao", length = 30)
  @Temporal(TemporalType.DATE) // TemporalType.DATE armazena apenas a data, sem informações de HORA
  private Date dataDesativacao;

  public LocalUso(LocalUsoRequestDTO data) {
    this.nomeLocalUsoObra = data.nomeLocalUsoObra();
    this.dataDesativacao = data.dataDesativacao();
  }
}
