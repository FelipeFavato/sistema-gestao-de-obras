const correctData = (data) => {
  data.length === 1 ? data = `0${data}` : data;
  return data;
};

export const generateCorrectData = () => {
  let dataAtual = new Date();
  let dia = String(dataAtual.getDate());
  let mes = String(dataAtual.getMonth() + 1);
  let ano = dataAtual.getFullYear();
  let horas = String(dataAtual.getHours());
  let minutos = String(dataAtual.getMinutes());
  let segundos = String(dataAtual.getSeconds());

  dia = correctData(dia);
  mes = correctData(mes);
  horas = correctData(horas);
  minutos = correctData(minutos);
  segundos = correctData(segundos);

  let dataFormatada = `${dia}/${mes}/${ano} (${horas}:${minutos}:${segundos})`;
  return dataFormatada;
};
