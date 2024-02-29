package com.gobra.sistemagestaodeobras.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gobra.sistemagestaodeobras.dto.UsuarioRequestDTO;
import com.gobra.sistemagestaodeobras.dto.UsuarioResponseDTO;
import com.gobra.sistemagestaodeobras.model.Usuario;
import com.gobra.sistemagestaodeobras.service.UsuarioService;

import jakarta.transaction.Transactional;

@RestController
@RequestMapping("/api/usuario")
public class UsuarioController {
  
  @Autowired
  private UsuarioService usuarioService;

  @GetMapping
  public List<UsuarioResponseDTO> getAll () {
    return usuarioService.getAllUsuarios();
  }

  // POST - AuthenticationController.java

  @PutMapping
  @Transactional
  public ResponseEntity<Usuario> updateOne (@RequestBody UsuarioRequestDTO data) {
    return usuarioService.updateUsuario(data);
  }

  @DeleteMapping
  public ResponseEntity<?> deletaOne (@RequestBody Usuario usuario) {
    return usuarioService.deletaUsuario(usuario);
  }
}
