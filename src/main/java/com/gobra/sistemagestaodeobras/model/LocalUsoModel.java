// Classe da entidade => Classe que representa a
// tabela no banco de dados relacional (Postgres)

package com.gobra.sistemagestaodeobras.model;

import com.gobra.sistemagestaodeobras.dto.LocalUsoRequestDTO;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Table(name = "localuso")  // Essa notação indica que essa classe é uma tabela
@Entity(name = "localuso")  // Essa notação da um nome à entidade
@Getter // Lombok gera todos os métodos de get de cada atributo
@NoArgsConstructor // Lombok declara um constructor que nao recebe nenhum argumento
@AllArgsConstructor // Lombok declara um constructor que recebe todos os argumentos
@EqualsAndHashCode(of = "codigoLocalUsoObra") // O Id é a representação unica
public class LocalUsoModel {
  // PK - codigo - Representa o id
  @Id @GeneratedValue(strategy = GenerationType.AUTO)// -> Gera automaticamente
  private Integer codigoLocalUsoObra;
  // nome
  private String nomeLocalUsoObra;

  public LocalUsoModel(LocalUsoRequestDTO data) {
    this.nomeLocalUsoObra = data.nomeLocalUsoObra();
  }
}
