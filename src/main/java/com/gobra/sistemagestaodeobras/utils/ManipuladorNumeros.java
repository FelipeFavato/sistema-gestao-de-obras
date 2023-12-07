package com.gobra.sistemagestaodeobras.utils;

public class ManipuladorNumeros {
  
  public static Integer extrairNumero (String info) {

    String[] partes = info.split(" ");

    String ultimoElemento = partes[partes.length - 1];

    try {

      return Integer.parseInt(ultimoElemento);
    
    } catch (NumberFormatException e) {

      System.err.println("Não foi possível converter para inteiro: " + ultimoElemento);
      return 0;
    
    }
  }

}
