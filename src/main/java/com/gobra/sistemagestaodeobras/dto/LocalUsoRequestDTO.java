// DTO => Data Transfer Object

package com.gobra.sistemagestaodeobras.dto;
// import jakarta.validation.constraints.NotBlank;
// import jakarta.validation.constraints.NotNull;

import java.util.Date;
import java.util.List;

import com.gobra.sistemagestaodeobras.model.ItemCompra;

// Por ser um record, o Java ja faz automaticamente os metodos de GET
public record LocalUsoRequestDTO (

  Integer codigoLocalUsoObra,

  // @NotBlank  // => Validacao do requestBody
  // @NotNull  // => Validacao do requestBody
  String nomeLocalUsoObra,

  Date dataDesativacao,

  List<ItemCompra> itensCompra

) { }
