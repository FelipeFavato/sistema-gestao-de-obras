package com.gobra.sistemagestaodeobras.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gobra.sistemagestaodeobras.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

  Optional<Usuario> findByEmail(String email);

}
