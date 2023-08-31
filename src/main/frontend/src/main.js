// src/main/frontend => npm run dev

// import './assets/main.css'

import { createApp } from 'vue'
import App from './App.vue'
import router from './routes';

const app = createApp(App)

app.use(router);
app.mount('#app');


// Baseado em componentes
// Construído em cima de HTML, CSS e JS

// Exemplo
// function cadastrar() {
//   fetch('http://localhost:8080/cadastrar',
//     {
//       headers: {
//         'Accept': 'application/json',
//         'Content-Type': 'application/json'
//       },
//       method: 'Post',
//       body: JSON.stringify({ a: 1, b: 2 }) => Formato dos dados
//     })
//     .then(function (res) { console.log(res) })
//     .catch(function (res) { console.log(res) })
// }
