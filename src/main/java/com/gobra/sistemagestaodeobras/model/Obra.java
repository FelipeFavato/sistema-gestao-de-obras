package com.gobra.sistemagestaodeobras.model;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gobra.sistemagestaodeobras.dto.ObraRequestDTO;

// import jakarta.persistence.CascadeType;
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
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_generator_obra")
  @SequenceGenerator(name = "seq_generator_obra", sequenceName = "SEQUENCIA_OBRA", initialValue = 1, allocationSize = 1)
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

  @Column(name = "custo_previsto", length = 50)
  private Integer custoPrevisto;

  // Uma Obra pode ter muitas Compras.
  @JsonIgnore
  @OneToMany(mappedBy = "obra")
  private List<Compra> compras;

  // Uma Obra pode ter muitos Socios.
  // Um Socio pode participar de muitas Obras.
  // Talvez alguma alteração aqui?
  // @JsonIgnore
  // @ManyToMany(mappedBy = "obras", fetch = FetchType.LAZY)
  // private List<Socio> socios;
  // , cascade = CascadeType.ALL

  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(name = "socios_obras",
  joinColumns = {
    @JoinColumn(name = "id_obra", referencedColumnName = "codigo")
  },
  inverseJoinColumns = {
    @JoinColumn(name = "id_socio", referencedColumnName = "codigo")
  })
  @JsonIgnore
  private List<Socio> socios;


  public Obra(ObraRequestDTO data) {
    this.nome = data.nome();
    this.endereco = data.endereco();
    this.dataInicio = data.dataInicio();
    this.dataPrevistaFim = data.dataPrevistaFim();
    this.dataRealFim = data.dataRealFim();
    this.custoPrevisto = data.custoPrevisto();
    this.compras = data.compras();
    this.socios = data.socios();
  }
}
