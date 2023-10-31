package com.gobra.sistemagestaodeobras.dto;

// import com.gobra.sistemagestaodeobras.model.Perfil;
import com.gobra.sistemagestaodeobras.model.Usuario;
import com.gobra.sistemagestaodeobras.utils.TipoPerfilEnum;

public record UsuarioResponseDTO (

  Integer codigo,

  String nome,

  String email,

  String senha,

  TipoPerfilEnum role

  // Perfil perfil

) {

  public UsuarioResponseDTO(Usuario usuarioModel) {
    this(
      usuarioModel.getCodigo(),
      usuarioModel.getNome(),
      usuarioModel.getEmail(),
      usuarioModel.getSenha(),
      usuarioModel.getRole()
      // usuarioModel.getPerfil()
    );
  }
}
