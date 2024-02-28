package com.gobra.sistemagestaodeobras.exceptionHandler.dto;

import com.gobra.sistemagestaodeobras.exceptionHandler.projection.CodItemCodNomeLocalProjection;

public class CodItemCodNomeLocalDTO {

  Integer codigoItem;
  Integer codigoLocalUsoObra;


  public CodItemCodNomeLocalDTO() {

  }

  public CodItemCodNomeLocalDTO(Integer codItem, Integer codLocal) {
    this.codigoItem = codItem;
    this.codigoLocalUsoObra = codLocal;
  }

  public CodItemCodNomeLocalDTO (CodItemCodNomeLocalProjection projection) {
    codigoItem = projection.getCodigoItem();
    codigoLocalUsoObra = projection.getCodigoLocalUsoObra();
  }

  // Getter e Setters.
  public Integer getCodigoItem () {
    return codigoItem;
  }

  public void setCodigoItem (Integer codItem) {
    this.codigoItem = codItem;
  }

  public Integer getCodigoLocalUsoObra () {
    return codigoLocalUsoObra;
  }

  public void setCodigoLocalUsoObra (Integer codLocal) {
    this.codigoLocalUsoObra = codLocal;
  }

}
