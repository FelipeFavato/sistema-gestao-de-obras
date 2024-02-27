export function focusFirstModalInput (inputID) {
  setTimeout(() => {
    document.getElementById(inputID).focus();
  }, 500);
}