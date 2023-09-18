import correctDate from "./correctDate";

const transformDate = (data) => {
  let partes = data.split("-");

  if (partes.length === 3) {
      let dia = Number(partes[2]) + 1;
      let novaData = `${partes[0]}-${partes[1]}-${correctDate(dia)}`;
      return novaData;
  } else {
      return null;
  }
}

export default transformDate;
