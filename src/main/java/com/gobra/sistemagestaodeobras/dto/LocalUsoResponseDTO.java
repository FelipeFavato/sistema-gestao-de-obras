// DTO => Data Transfer Object
// Boa prática, teoricamente
package com.gobra.sistemagestaodeobras.dto;

import com.gobra.sistemagestaodeobras.model.LocalUsoModel;

public record LocalUsoResponseDTO(
  long id,
  String nomeLocalUsoObra
) {

  public LocalUsoResponseDTO(LocalUsoModel localUso) {
    // O Lombok gerou os getters dos atributos
    this(localUso.getCodigoLocalUsoObra(), localUso.getNomeLocalUsoObra());
  }
}
