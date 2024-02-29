package com.gobra.sistemagestaodeobras.exceptionHandler;

import org.springframework.http.HttpStatus;

public class CustomErrorMessage {
  HttpStatus status;
  Integer statusCode;
  String resposta;

  public CustomErrorMessage (HttpStatus status, Integer statusCode, String resp) {
    this.status = status;
    this.statusCode = statusCode;
    this.resposta = resp;
  }

  // Getters e Setters
  public HttpStatus getStatus () {
    return status;
  }

  public void setStatus (HttpStatus status) {
    this.status = status;
  }

  public Integer getStatusCode () {
    return statusCode;
  }

  public void setStatusCode (Integer statusCode) {
    this.statusCode = statusCode;
  }

  public String getResposta () {
    return resposta;
  }

  public void setResposta (String resp) {
    this.resposta = resp;
  }
}
