package com.gobra.sistemagestaodeobras.model;

import java.util.Date;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gobra.sistemagestaodeobras.dto.ObraRequestDTO;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
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

@Table(name = "obra")
@Entity(name = "obra")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "codigo")
public class Obra {
  
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_gen_obra")
  @SequenceGenerator(name = "seq_gen_obra", sequenceName = "SEQ_OBRA", initialValue = 1, allocationSize = 1)
  private Integer codigo;

  @Column(name = "nome", length = 70, unique = true)
  private String nome;

  @Column(name = "endereco", length = 150)
  private String endereco;

  @Column(name = "data_inicio", length = 30)
  @Temporal(TemporalType.DATE)
  private Date dataInicio;

  @Column(name = "data_prevista_fim", length = 30)
  @Temporal(TemporalType.DATE)
  private Date dataPrevistaFim;

  @Column(name = "data_real_fim", length = 30)
  @Temporal(TemporalType.DATE)
  private Date dataRealFim;

  @Column(name = "custo_mao_de_obra", length = 50)
  private Integer custoMaoDeObra;

  @Column(name = "custo_previsto", length = 50)
  private Integer custoPrevisto;

  // Uma Obra pode ter muitas Compras.
  @JsonIgnore
  @OneToMany(mappedBy = "obra")
  private List<Compra> compras;

  // Uma Obra pode ter muitos Socios.
  // Um Socio pode participar de muitas Obras.
  @JsonIgnore
  @ManyToMany(mappedBy = "obras")
  private Set<Socio> socios;

  // Uma OBRA pode ter muitas FOTOS.
  @JsonIgnore
  @OneToMany(mappedBy = "idObra")
  private List<ObraArquivo> arquivos;

  // UMA Obra pode ter MUITAS previsões.
  @JsonIgnore
  @OneToMany(mappedBy = "codigoObra")
  private List<PrevisaoCompra> previsoes;

  public Obra(ObraRequestDTO data) {
    this.nome = data.nome();
    this.endereco = data.endereco();
    this.dataInicio = data.dataInicio();
    this.dataPrevistaFim = data.dataPrevistaFim();
    this.dataRealFim = data.dataRealFim();
    this.custoMaoDeObra = data.custoMaoDeObra();
    this.custoPrevisto = data.custoPrevisto();
    this.compras = data.compras();
    this.socios = data.socios();
    this.previsoes = data.previsoes();
    // this.arquivos = data.arquivos();
  }
}
