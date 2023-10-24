package com.gobra.sistemagestaodeobras.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gobra.sistemagestaodeobras.utils.TipoPerfilEnum;

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

@Table(name = "perfil")
@Entity(name = "perfil")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "codigo")
public class Perfil {
  
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_generator_perfil")
  @SequenceGenerator(name = "seq_generator_perfil", sequenceName = "SEQUENCIA_PERFIL", initialValue = 1, allocationSize = 1)
  private Integer codigo;

  @Enumerated(EnumType.STRING)
  @Column(name = "tipo_perfil", length = 15)
  private TipoPerfilEnum tipoPerfil;
  
  // Cada perfil terá uma lista de usuários atrelada a ele
  @JsonIgnore
  @OneToMany(mappedBy = "perfil")
  private List<Usuario> usuarios;

}
