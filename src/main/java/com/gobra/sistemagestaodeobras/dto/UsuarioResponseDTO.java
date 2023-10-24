package com.gobra.sistemagestaodeobras.dto;

import com.gobra.sistemagestaodeobras.model.Perfil;
import com.gobra.sistemagestaodeobras.model.Usuario;

public record UsuarioResponseDTO (

  Integer codigo,

  String nome,

  String email,

  String senha,

  Perfil perfil

) {

  public UsuarioResponseDTO(Usuario usuarioModel) {
    this(
      usuarioModel.getCodigo(),
      usuarioModel.getNome(),
      usuarioModel.getEmail(),
      usuarioModel.getSenha(),
      usuarioModel.getPerfil()
    );
  }
}
