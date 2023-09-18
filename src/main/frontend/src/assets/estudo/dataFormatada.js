export const correctData = (data) => {
  data.length === 1 ? data = `0${data}` : data;
  return data;
};

const generateCorrectTimeStamp = () => {
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

const generateCorrectDate = () => {
  let dataAtual = new Date();
  let dia = String(dataAtual.getDate());
  let mes = String(dataAtual.getMonth() + 1);
  let ano = dataAtual.getFullYear();

  dia = correctData(dia);
  mes = correctData(mes);

  let dataFormatada = `${ano}/${mes}/${dia}`;
  return dataFormatada;
}

export const transformDate = (data) => {
  // Divide a data em dia, mês e ano usando "/"
  let partes = data.split("/");

  // Verifica se há três partes (dia, mês, ano)
  if (partes.length === 3) {
      // Reorganiza as partes no formato "yyyy/mm/dd"
      var novaData = partes[2] + "/" + partes[1] + "/" + partes[0];
      return novaData;
  } else {
      // Se a data não estiver no formato esperado, retorna null ou uma mensagem de erro
      return null;
  }
}

export default {}