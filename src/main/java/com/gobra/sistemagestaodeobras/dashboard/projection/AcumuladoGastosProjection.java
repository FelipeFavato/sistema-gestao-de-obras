package com.gobra.sistemagestaodeobras.dashboard.projection;

import java.util.Date;

public interface AcumuladoGastosProjection {
  
  Date getDataVencimento();

  double getGastoMaoDeObra();

  double getGastos();

  double getGastoTotal();

}
