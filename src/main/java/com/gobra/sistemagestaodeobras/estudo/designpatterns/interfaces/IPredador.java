package com.gobra.sistemagestaodeobras.estudo.designpatterns.interfaces;


// Template: Modelo.

// Interface:
// 1. Um Template que pode ser aplicado a uma classe.
// 2. Similar a Herança, mas especifica oque uma classe tem/deve fazer.
// 3. Classes podem aplicar mais do que 1 Interface, já Herança é limitada a 1 super classe.
// 4. Pode declarar variáveis normalmente e pode declarar métodos e não precisa criar um corpo para 
// os métodos.
public interface IPredador {

  public String cacar();

}
