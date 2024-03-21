package com.gobra.sistemagestaodeobras.dashboard.projection;

public interface MDOGastoComprasOrcamentoProjection {
  
  Integer getCustoMaoDeObra();

  Integer getCustoPrevisto();

  double getPagoMaoDeObra();

  double getValorDesconto();

  double getValorGastos();

  double getValorTotalPrevisto();

}
