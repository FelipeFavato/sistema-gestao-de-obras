export const brazilDate = (data) => {

  if (data === null) {
    return null
  }

  let partes = data.split("-");

  if (partes.length === 3) {
      return `${partes[2]}-${partes[1]}-${partes[0]}`;
  } else {
      return null;
  }
}

export default brazilDate;
