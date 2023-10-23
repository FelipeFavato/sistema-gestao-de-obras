package com.gobra.sistemagestaodeobras.model;

import com.gobra.sistemagestaodeobras.dto.UsuarioRequestDTO;
import com.gobra.sistemagestaodeobras.utils.TipoPerfilEnum;

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

@Table(name = "usuario")
@Entity(name = "usuario")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "codigo")
public class Usuario {
  
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_generator_usuario")
  @SequenceGenerator(name = "seq_generator_usuario", sequenceName = "SEQUENCIA_USUARIO", initialValue = 1, allocationSize = 1)
  private Integer codigo;

  @Column(name = "nome", length = 100, unique = true)
  private String nome;

  @Column(name = "email", length = 70, unique = true)
  private String email;

  @Enumerated(EnumType.STRING)
  @Column(name = "tipo_perfil", length = 15)
  private TipoPerfilEnum tipoPerfil;

  public Usuario(UsuarioRequestDTO data) {
    this.nome = data.nome();
    this.email = data.email();
    this.tipoPerfil = data.tipoPerfil();
  }

}
