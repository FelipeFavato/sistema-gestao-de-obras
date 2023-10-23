package com.gobra.sistemagestaodeobras.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gobra.sistemagestaodeobras.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
  
}
