package com.gobra.sistemagestaodeobras.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashByteArray {

  public static String hashByteArray(byte[] byteArray) {
    try {
      // Especifica o algoritmo de hash desejado, por exemplo, SHA-256
      MessageDigest md = MessageDigest.getInstance("SHA-256");

      // Atualiza o digest com os bytes do array
      md.update(byteArray);

      // Converte o digest para uma representação hexadecimal
      byte[] hashedBytes = md.digest();

      // Converte o array de bytes para uma representação de string
      StringBuilder sb = new StringBuilder();
      for (byte hashedByte : hashedBytes) {
          sb.append(String.format("%02x", hashedByte));
      }

      return sb.toString();
    } catch (NoSuchAlgorithmException e) {
      // Lida com a exceção se o algoritmo de hash não for suportado
      e.printStackTrace();
      return null;
    }
  }

}
