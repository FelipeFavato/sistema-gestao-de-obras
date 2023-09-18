const correctDate = (data) => {
  data < 10 ? data = `0${data}` : data;
  return data;
}

export default correctDate;