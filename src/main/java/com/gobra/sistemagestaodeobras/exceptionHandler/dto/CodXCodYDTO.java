package com.gobra.sistemagestaodeobras.exceptionHandler.dto;

import com.gobra.sistemagestaodeobras.exceptionHandler.projection.CodXCodYProjection;

public class CodXCodYDTO {
  
  Integer codX;
  Integer codY;

  public CodXCodYDTO() {
    
  }

  public CodXCodYDTO(Integer cX, Integer cY) {
    this.codX = cX;
    this.codY = cY;
  }

  public CodXCodYDTO(CodXCodYProjection projection) {
    codX = projection.getCodX();
    codY = projection.getCodY();
  }

  // Getters e Setters:
  public Integer getCodX () {
    return codX;
  }

  public void setCodX (Integer codX) {
    this.codX = codX;
  }

  public Integer getCodY () {
    return codY;
  }

  public void setCodY (Integer codY) {
    this.codY = codY;
  }
}
