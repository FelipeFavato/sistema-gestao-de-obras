package com.gobra.sistemagestaodeobras.utils;

public class ManipuladorStrings {

  public static String extrairPrimeiroUltimoNome(String nomeCompleto) {
    String[] partes = nomeCompleto.trim().split("\\s+");

    if (partes.length < 2) {
        return "";
    }

    String primeiroNome = partes[0];
    String ultimoSobrenome = partes[partes.length - 1];

    return primeiroNome + " " + ultimoSobrenome;
  }

}
