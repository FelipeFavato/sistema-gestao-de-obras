package com.gobra.sistemagestaodeobras.dto;

import java.util.Date;
import java.util.List;

import com.gobra.sistemagestaodeobras.model.Compra;
import com.gobra.sistemagestaodeobras.model.Obra;
import com.gobra.sistemagestaodeobras.model.Socio;


public record ObraResponseDTO (

  Integer codigo,

  String nome,

  String endereco,

  Date dataInicio,

  Date dataPrevistaFim,

  Date dataRealFim,

  Integer custoMaoDeObra,

  Integer custoPrevisto,

  List<Compra> compras,

  List<Socio> socios

) {

  public ObraResponseDTO(Obra obraModel) {
    this(
      obraModel.getCodigo(),
      obraModel.getNome(),
      obraModel.getEndereco(),
      obraModel.getDataInicio(),
      obraModel.getDataPrevistaFim(),
      obraModel.getDataRealFim(),
      obraModel.getCustoMaoDeObra(),
      obraModel.getCustoPrevisto(),
      obraModel.getCompras(),
      obraModel.getSocios()
    );
  }

}
