package com.gobra.sistemagestaodeobras.model;


import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


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
public class Usuario implements UserDetails {
  
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_gener_usuario")
  @SequenceGenerator(name = "seq_gener_usuario", sequenceName = "SEQ_USUARIO", initialValue = 1, allocationSize = 1)
  private Integer codigo;

  @Column(name = "nome", length = 100, unique = true)
  private String nome;

  @Column(name = "email", length = 70, unique = true)
  private String email;

  @Column(name = "senha", length = 155)
  private String senha;

  @Enumerated(EnumType.STRING)
  @Column(name = "role", length = 15)
  private TipoPerfilEnum role;

  public Usuario(String nome, String email, String senha, TipoPerfilEnum role) {
    this.nome = nome;
    this.email = email;
    this.senha = senha;
    this.role = role;
  }

  // 1. public Collection<? extends GrantedAuthority> getAuthorities(): O método getAuthorities () retorna
  //    uma coleção de objetos que estendem a classe 'GrantedAuthority'. Esta coleção representa as
  //    autorizações associadas a um usuario ou objeto.
  // 2. if (this.tipoPerfil == TipoPerfilEnum.Gestor): Caso o tipoPerfil seja de 'Gestor':
  // 2.1 return List.of(new SimpleGrantedAuthority("Gestor"), new SimpleGrantedAuthority("Operacional")):
  //     Crie uma lista (coleção) de objetos 'SimpleGrantedAuthority'. Ou seja, se o perfil for 'Gestor',
  //      dê a esse perfil as autorizações de 'Gestor' e 'Operacional'.
  // 3. return List.of(new SimpleGrantedAuthority("Operacional")): Caso o tipoPerfil não seja 'Gestor',
  //    dê a ele a 'SimpleGrantedAuthority' apenas de 'Operacional'.
  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    if (this.role == TipoPerfilEnum.ADMIN) {
      return List.of(new SimpleGrantedAuthority("ROLE_ADMIN"), new SimpleGrantedAuthority("ROLE_USER"));
    } else {
      return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }
  }

  // Método para retornar a string senha (usada como password).
  @Override
  public String getPassword() {
    // return this.senha; ===> Porque não é 'this.senha'?
    return senha;
  }

  // Método para retornar a string email (usada como username).
  @Override
  public String getUsername() {
    // return this.email; ===> Porque não é 'this.email'?
    return email;
  }

  // Todos esses métodos são validações mais avançadas, por enquanto todas retornarão
  // 'true'. Ou seja, não bloquearei o acesso sob nenhuma condição após feito o login.

  // Checa se a conta não esta expirada.
  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  // Checa se a conta não esta bloqueada.
  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  // Checa se a credencial não esta expirada.
  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  // Checa se o Usuário esta habilitado.
  @Override
  public boolean isEnabled() {
    return true;
  }

}
