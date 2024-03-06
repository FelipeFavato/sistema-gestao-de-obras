import { removeElementClass } from "./removeElementClass";
import { addElementClass } from "./addElementClass";

// Passar:
// inputValue => valor do campo: 'this.nomeExemplo';
// inputID => O Id do Input: 'insert-nome-exemplo-input';
export function checkInputValue (inputValue, inputID) {
  if (inputValue === '') {
    addElementClass(inputID, 'required-red-border');
  } else {
    removeElementClass(inputID, 'required-red-border');
  }
}