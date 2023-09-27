package com.gobra.sistemagestaodeobras.dto;

import java.time.LocalDate;

import com.gobra.sistemagestaodeobras.model.Obra;


public record ObraReqResDTO (

  int codigo,

  String nome,

  String endereco,

  LocalDate dataInicio,

  LocalDate dataPrevistaFim,

  LocalDate dataRealFim,

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
