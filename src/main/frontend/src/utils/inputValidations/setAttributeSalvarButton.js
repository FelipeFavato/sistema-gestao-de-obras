export function setAttributeSalvarButton (buttonID, openClose) {
  document.getElementById(buttonID).setAttribute('data-bs-dismiss', openClose);
}