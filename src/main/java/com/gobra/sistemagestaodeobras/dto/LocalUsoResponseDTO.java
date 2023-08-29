// DTO => Data Transfer Object
// Boa prática, teoricamente

package com.gobra.sistemagestaodeobras.dto;

import com.gobra.sistemagestaodeobras.model.LocalUsoModel;

public record LocalUsoResponseDTO(
  long codigoLocalUsoObra,
  String nomeLocalUsoObra
) {

  public LocalUsoResponseDTO(LocalUsoModel localUsoModel) {
    // O Lombok gerou os getters e setters dos atributos
    this(localUsoModel.getCodigoLocalUsoObra(), localUsoModel.getNomeLocalUsoObra());
  }
}
