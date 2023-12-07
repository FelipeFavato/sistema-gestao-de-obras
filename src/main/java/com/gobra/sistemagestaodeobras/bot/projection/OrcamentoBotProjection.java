package com.gobra.sistemagestaodeobras.bot.projection;

public interface OrcamentoBotProjection {

  String getNomeObra();

  Integer getCustoMaoDeObra();

  Integer getCustoPrevisto();

  double getPagoMaoDeObra();

  double getValorDesconto();

  double getValorGastos();
}
