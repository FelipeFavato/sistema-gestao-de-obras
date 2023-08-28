// Classe da entidade => Classe que representa a
// tabela no banco de dados relacional (Postgres)

package com.gobra.sistemagestaodeobras.Entidades;

import jakarta.persistence.Entity;
// import jakarta.persistence.GeneratedValue;
// import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;


@Table(name = "localuso")  // Essa notação indica que essa classe é uma tabela
@Entity(name = "localuso")  // Essa notação da um nome à entidade
public class LocalUso {
  // PK - codigo - Representa o id
  @Id  // @GeneratedValue(strategy = GenerationType.IDENTITY) -> Gera automaticamente
  private Integer codigoLocalUsoObra;
  // nome
  private String nomeLocalUsoObra;
}
