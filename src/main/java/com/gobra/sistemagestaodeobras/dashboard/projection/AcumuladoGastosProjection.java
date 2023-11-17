package com.gobra.sistemagestaodeobras.dashboard.projection;

import java.util.Date;

public interface AcumuladoGastosProjection {
  
  Integer getIdObra();

  Date getDataVencimento();

  double getValorFinal();

}
