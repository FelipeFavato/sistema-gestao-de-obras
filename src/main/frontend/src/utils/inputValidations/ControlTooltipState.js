export function controlToolTipState (showOrHide) {
  if (showOrHide === 'show') {
    const tooltipTriggerList = document.querySelectorAll('[data-bs-toggle="tooltip"]');
    [...tooltipTriggerList].map(tooltipTriggerEl => {
      new bootstrap.Tooltip(tooltipTriggerEl).show();
      console.log(tooltipTriggerEl);
    });
  } else if (showOrHide === 'hide') {
    const tooltipTriggerList = document.querySelectorAll('[data-bs-toggle="tooltip"]');
    [...tooltipTriggerList].map(tooltipTriggerEl => tooltipTriggerEl.removeAttribute('data-bs-title'));
  } else if (showOrHide === 'get') {
    const tooltipTriggerList = document.querySelectorAll('[data-bs-toggle="tooltip"]');
    [...tooltipTriggerList].map(tooltipTriggerEl => new bootstrap.Tooltip(tooltipTriggerEl));
  }
}