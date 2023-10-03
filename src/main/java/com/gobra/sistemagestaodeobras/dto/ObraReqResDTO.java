package com.gobra.sistemagestaodeobras.dto;

import java.time.LocalDate;
import java.util.List;

import com.gobra.sistemagestaodeobras.model.Compra;
import com.gobra.sistemagestaodeobras.model.Obra;


public record ObraReqResDTO (

  int codigo,

  String nome,

  String endereco,

  LocalDate dataInicio,

  LocalDate dataPrevistaFim,

  LocalDate dataRealFim,

  Integer custoPrevisto,

  List<Compra> compras

) {

  public ObraReqResDTO(Obra obraModel) {
    this(
      obraModel.getCodigo(),
      obraModel.getNome(),
      obraModel.getEndereco(),
      obraModel.getDataInicio(),
      obraModel.getDataPrevistaFim(),
      obraModel.getDataRealFim(),
      obraModel.getCustoPrevisto(),
      obraModel.getCompras()
    );
  }
}
