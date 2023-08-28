import './assets/main.css'

import { createApp } from 'vue'
import App from './App.vue'

createApp(App).mount('#app')


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