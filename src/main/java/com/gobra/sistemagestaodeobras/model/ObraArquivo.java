package com.gobra.sistemagestaodeobras.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;




@Table(name = "obra_arquivos")
@Entity(name = "obra_arquivos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "codigo")
public class ObraArquivo {
  
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_gen_obra_arquivo")
  @SequenceGenerator(name = "seq_gen_obra_arquivo", sequenceName = "SEQ_OBRA_ARQUIVO", initialValue = 1, allocationSize = 1)
  private Integer codigo;

  // @Lob = large objects, Campo binário ou campos muito grandes
  @Lob()
  @Column(name = "conteudo_arquivo")
  private byte[] conteudoArquivo;

  @Column(name = "descricao", length = 180)
  private String descricao;

  @Column(name = "nome_arquivo", length = 80)
  private String nomeArquivo;

  @Column(name = "hash_arquivo", unique = true)
  private String hashArquivo;

  @JsonIgnore
  @ManyToOne  // MUITAS fotos para UMA obra.
  @JoinColumn(name = "id_obra", referencedColumnName = "codigo")
  private Obra idObra;

  public ObraArquivo (byte[] conteudoArquivo, String descricao, String nomeArquivo, String hashArquivo, Obra idObra) {
    this.conteudoArquivo = conteudoArquivo;
    this.descricao = descricao;
    this.nomeArquivo = nomeArquivo;
    this.hashArquivo = hashArquivo;
    this.idObra = idObra;
  }
}
