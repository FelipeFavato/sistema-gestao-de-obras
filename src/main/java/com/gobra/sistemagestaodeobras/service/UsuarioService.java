package com.gobra.sistemagestaodeobras.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.gobra.sistemagestaodeobras.dto.UsuarioRequestDTO;
import com.gobra.sistemagestaodeobras.dto.UsuarioResponseDTO;
import com.gobra.sistemagestaodeobras.model.Usuario;
import com.gobra.sistemagestaodeobras.repository.UsuarioRepository;

import jakarta.persistence.EntityNotFoundException;


@Service
public class UsuarioService {

  @Autowired
  private UsuarioRepository usuarioRepository;


  // GET
  public List<UsuarioResponseDTO> getAllUsuarios () {
    return usuarioRepository.findAll().stream().map(UsuarioResponseDTO::new).toList();
  }

  // INSERT: AuthenticationController.java
  public ResponseEntity<Usuario> updateUsuario(@RequestBody UsuarioRequestDTO data) {
    Optional<Usuario> optionalUsuario = usuarioRepository.findById(data.codigo());

    if (optionalUsuario.isPresent()) {
      Usuario usuario = optionalUsuario.get();
      usuario.setNome(data.nome());
      usuario.setEmail(data.email());
      usuario.setSenha(data.senha());
      usuario.setRole(data.role());
      // usuario.setPerfil(data.perfil());
    
      return ResponseEntity.ok(usuario);
    } else {
      throw new EntityNotFoundException();
    }
  }
  // UPDATE

  // DELETE
  public ResponseEntity<?> deletaUsuario (@RequestBody Usuario data) {
    Usuario usuario = usuarioRepository.getReferenceById(data.getCodigo());
    String nomeUsuario = usuario.getNome();

    usuarioRepository.delete(data);
    return new ResponseEntity<>("Usuário(a): '" + nomeUsuario + "'DELETADO(A) com sucesso.", HttpStatus.OK);
  }

}
