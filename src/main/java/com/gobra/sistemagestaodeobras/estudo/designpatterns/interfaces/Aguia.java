package com.gobra.sistemagestaodeobras.estudo.designpatterns.interfaces;

public class Aguia implements IPredador {


  public String cacar() {
    return "A águia esta caçando!";
  }


}

// A partir do momento que a classe esta descrita, ela precisa ser instanciada
// em um novo objeto:

// Aguia aguia = new Aguia();
// aguia.cacar();
