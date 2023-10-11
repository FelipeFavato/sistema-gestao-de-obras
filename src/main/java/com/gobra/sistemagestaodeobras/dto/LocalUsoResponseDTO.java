// DTO => Data Transfer Object
// Boa prática, teoricamente

package com.gobra.sistemagestaodeobras.dto;

import java.util.Date;
import java.util.List;

import com.gobra.sistemagestaodeobras.model.ItemCompra;
import com.gobra.sistemagestaodeobras.model.LocalUso;

public record LocalUsoResponseDTO(

  Integer codigoLocalUsoObra,
  
  String nomeLocalUsoObra,

  Date dataDesativacao,

  List<ItemCompra> itensCompra

  ) {

  public LocalUsoResponseDTO(LocalUso localUsoModel) {
    // O Lombok gerou os getters e setters dos atributos
    this(
      localUsoModel.getCodigoLocalUsoObra(),
      localUsoModel.getNomeLocalUsoObra(),
      localUsoModel.getDataDesativacao(),
      localUsoModel.getItensCompra()
    );
  }
}
