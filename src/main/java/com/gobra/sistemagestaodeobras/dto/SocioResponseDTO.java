package com.gobra.sistemagestaodeobras.dto;

import java.util.List;
import java.util.Set;

import com.gobra.sistemagestaodeobras.model.Compra;
import com.gobra.sistemagestaodeobras.model.Obra;
import com.gobra.sistemagestaodeobras.model.Socio;

public record SocioResponseDTO (

  Integer codigo,

  String nome,

  List<Compra> compras,

  Set<Obra> obras

) {

  public SocioResponseDTO(Socio socioModel) {
    this(
      socioModel.getCodigo(),
      socioModel.getNome(),
      socioModel.getCompras(),
      socioModel.getObras()
    );
  }

}
