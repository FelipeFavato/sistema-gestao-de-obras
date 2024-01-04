package com.gobra.sistemagestaodeobras.estudo.designpatterns.interfaces;


// Para aplicar mais de uma interface em uma classe, basta fazer dessa maneira
// usando virgula entre as Interfaces.
public class Peixe implements IPresa, IPredador {

  @Override
  public String cacar() {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'cacar'");
  }

  @Override
  public String fugir() {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'fugir'");
  }
  
}
