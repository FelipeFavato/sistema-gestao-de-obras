package com.gobra.sistemagestaodeobras.dto;

import com.gobra.sistemagestaodeobras.utils.TipoPerfilEnum;

public record UsuarioRequestDTO (

  Integer codigo,

  String nome,

  String email,

  TipoPerfilEnum tipoPerfil

) { }
