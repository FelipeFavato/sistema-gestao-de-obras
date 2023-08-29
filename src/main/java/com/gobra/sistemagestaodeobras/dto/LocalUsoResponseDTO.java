// DTO => Data Transfer Object
// Boa prática, teoricamente

package com.gobra.sistemagestaodeobras.dto;

import com.gobra.sistemagestaodeobras.model.LocalUso;

public record LocalUsoResponseDTO(
  long codigoLocalUsoObra,
  String nomeLocalUsoObra
) {

  public LocalUsoResponseDTO(LocalUso localUsoModel) {
    // O Lombok gerou os getters e setters dos atributos
    this(localUsoModel.getCodigoLocalUsoObra(), localUsoModel.getNomeLocalUsoObra());
  }
}
