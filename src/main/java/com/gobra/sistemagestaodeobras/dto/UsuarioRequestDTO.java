package com.gobra.sistemagestaodeobras.dto;

import com.gobra.sistemagestaodeobras.utils.TipoPerfilEnum;

// import com.gobra.sistemagestaodeobras.model.Perfil;

public record UsuarioRequestDTO (

  Integer codigo,

  String nome,

  String email,

  String senha,

  TipoPerfilEnum role

) { }
