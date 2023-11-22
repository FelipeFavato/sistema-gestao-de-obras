package com.gobra.sistemagestaodeobras.dashboard.projection;

public interface MDOGastoComprasOrcamentoProjection {
  
  Integer getIdObra();

  String getNomeObra();

  double getCustoMaoDeObra();

  Integer getOrcamentoMaoDeObra();

  double getValorTotal();

  Integer getCustoPrevisto();

}
