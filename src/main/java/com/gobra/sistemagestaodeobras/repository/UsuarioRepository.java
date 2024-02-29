package com.gobra.sistemagestaodeobras.repository;

// import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import com.gobra.sistemagestaodeobras.model.Usuario;
import java.util.List;
import java.util.Optional;


public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

  // 1. UserDetails: Importante que retorne um UserDetails, pois será usado pelo
  //    Spring Security.
  // 2. findByEmail(): Ao escrever findByParametro, o JPA ve oque ta escrito após
  //    findBy'...' e ja faz a consulta usando esse parametro. No caso 'email'.
  UserDetails findByEmail(String email);

  Optional<List<Usuario>> findByNome(String nome);
}
