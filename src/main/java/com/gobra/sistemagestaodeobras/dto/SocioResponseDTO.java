package com.gobra.sistemagestaodeobras.dto;

import java.util.Date;
import java.util.List;

import com.gobra.sistemagestaodeobras.model.Compra;
import com.gobra.sistemagestaodeobras.model.Obra;
import com.gobra.sistemagestaodeobras.model.Socio;

public record SocioResponseDTO (

  Integer codigo,

  String nome,

  Date dataDesativacao,

  Integer telegramID,

  List<Compra> compras,

  List<Obra> obras

) {

  public SocioResponseDTO(Socio socioModel) {
    this(
      socioModel.getCodigo(),
      socioModel.getNome(),
      socioModel.getDataDesativacao(),
      socioModel.getTelegramID(),
      socioModel.getCompras(),
      socioModel.getObras()
    );
  }

}
