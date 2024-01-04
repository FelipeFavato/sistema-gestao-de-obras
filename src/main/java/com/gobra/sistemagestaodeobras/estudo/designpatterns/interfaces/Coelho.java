package com.gobra.sistemagestaodeobras.estudo.designpatterns.interfaces;


// Para implementar uma Interface em uma Classe:
// 1. public class CLASS implements INTERFACE { ... }
// 2. Sobrescrever os métodos da INTERFACE dentro da CLASSE, nesse caso temos
//    o método 'fugir()'. Para sobrescrever o método, precisa colocar a notação
//    @Override em cima do método que se deseja sobrecrever.

public class Coelho implements IPresa {

  @Override
  public String fugir() {
    return "*O Coelho esta fugindo!*";
  }
  
}
