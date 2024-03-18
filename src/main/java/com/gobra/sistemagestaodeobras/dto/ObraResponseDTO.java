package com.gobra.sistemagestaodeobras.dto;

import java.util.Date;
import java.util.List;
import java.util.Set;

import com.gobra.sistemagestaodeobras.model.Compra;
import com.gobra.sistemagestaodeobras.model.Obra;
import com.gobra.sistemagestaodeobras.model.PrevisaoCompra;
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

  Set<Socio> socios,

  List<PrevisaoCompra> previsoes

  // List<ObraArquivo> arquivos

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
      obraModel.getSocios(),
      obraModel.getPrevisoes()
      // obraModel.getArquivos()
    );
  }

}
