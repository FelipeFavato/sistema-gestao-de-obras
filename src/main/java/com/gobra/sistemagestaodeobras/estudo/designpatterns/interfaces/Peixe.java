package com.gobra.sistemagestaodeobras.estudo.designpatterns.interfaces;


// Para aplicar mais de uma interface em uma classe, basta fazer dessa maneira
// usando virgula entre as Interfaces.
public class Peixe implements IPresa, IPredador {

  @Override
  public String cacar() {
    return "O peixe está caçando peixes menores!";
  }

  @Override
  public String fugir() {
    return "O Peixe esta sendo caçado por peixes maiores!";
  }
  
}
