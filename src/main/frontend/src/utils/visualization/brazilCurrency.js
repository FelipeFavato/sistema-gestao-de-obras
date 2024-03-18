export function brazilCurrency (dinheiroDouble) {
  if (dinheiroDouble === null) {
    return null
  } else {
    const valorFormatado = dinheiroDouble.toLocaleString('pt-BR', {
      style: 'currency',
      currency: 'BRL'
    });

    return valorFormatado;
  }
}