package com.gobra.sistemagestaodeobras.utils;

import java.text.NumberFormat;
import java.util.Locale;

public class FormatadorMoeda {

  public static String formatarMoeda(double numero) {
    // Cria um NumberFormat para o formato de moeda brasileira
    NumberFormat formatoMoeda = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
 
    // Formata o número como moeda brasileira
    String numeroFormatado = formatoMoeda.format(numero);
 
    return numeroFormatado;
  }

}

