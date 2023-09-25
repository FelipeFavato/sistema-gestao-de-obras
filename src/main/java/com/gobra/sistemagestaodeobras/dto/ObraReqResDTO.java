package com.gobra.sistemagestaodeobras.dto;

import java.util.Date;

import com.gobra.sistemagestaodeobras.model.Obra;


public record ObraReqResDTO (

  int codigo,

  String nome,

  String endereco,

  Date dataInicio,

  Date dataPrevistaFim,

  Date dataRealFim,

  Integer custoPrevisto

) {

  public ObraReqResDTO(Obra obraModel) {
    this(
      obraModel.getCodigo(),
      obraModel.getNome(),
      obraModel.getEndereco(),
      obraModel.getDataInicio(),
      obraModel.getDataPrevistaFim(),
      obraModel.getDataRealFim(),
      obraModel.getCustoPrevisto()
    );
  }
}
