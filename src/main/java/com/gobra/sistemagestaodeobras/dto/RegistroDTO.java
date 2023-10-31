package com.gobra.sistemagestaodeobras.dto;

import com.gobra.sistemagestaodeobras.utils.TipoPerfilEnum;

public record RegistroDTO (

  String nome,

  String email,

  String senha,

  TipoPerfilEnum role

) { }
