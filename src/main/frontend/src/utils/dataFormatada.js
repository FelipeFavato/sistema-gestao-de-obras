export const generateCorrectData = () => {
  let dataAtual = new Date();
  let dia = String(dataAtual.getDate());
  let mes = String(dataAtual.getMonth() + 1);
  let ano = dataAtual.getFullYear();
  let horas = dataAtual.getHours();
  let minutos = dataAtual.getMinutes();
  let segundos = dataAtual.getSeconds();

  if (dia.length === 1) {
    dia = `0${dia}`
  }

  if (mes.length === 1) {
    mes = `0${mes}`
  }

  let dataFormatada = `${dia}/${mes}/${ano} (${horas}:${minutos}:${segundos})`;
  return dataFormatada;
}
