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

// const fixTimeZone = (data) => {
//   let date = data
//   const currentHour = new Date().getHours();
//   if (currentHour >= 12) {
//     return date
//   }
//   if (currentHour < 12) {
//     date = transformDate(data)
//     return date
//   }
// }

export default transformDate;
