import { addElementClass } from "./addElementClass";
import { removeElementClass } from "./removeElementClass";
import { setAttributeSalvarButton } from "./setAttributeSalvarButton";

export function  clickSavecheckRequiredInsertField (inputValue, inputID, labelID, salvarButtonID) {
  if (inputValue === '') {
    addElementClass(inputID, 'required-red-border');
    addElementClass(labelID, 'campo-obrigatorio-warning');
    setAttributeSalvarButton(salvarButtonID, 'no-closing-modal');
  } else {
    removeElementClass(inputID, 'required-red-border');
    removeElementClass(labelID, 'campo-obrigatorio-warning');
    setAttributeSalvarButton(salvarButtonID, 'modal');
  }
}