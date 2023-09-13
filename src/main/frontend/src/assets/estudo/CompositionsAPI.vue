<!-- CompositionsAPI => A logica dos componentes é
definida usando funções API importadas -->
<!-- setup: Permite que o Vue, em tempo de compilação, realize transformações -->
<script setup>
// import { defineComponent, onMounted, ref } from 'vue';
// import API from '../utils/API.js'
// export default defineComponent({
//   setup() {
//     const infoDB = ref([]);

//     const fetchInfoDB = () => {
//       API.get("/localuso").then((response) => infoDB.value = response.data)
//     };

//     onMounted(fetchInfoDB);

//     return { infoDB }
//   },
// })

import { ref, onMounted } from 'vue'

// Variáveis e funções declaradas aqui podem ser usadas
// diretamente no <template>
const count = ref(0)

function increment() {
  count.value++
}

onMounted(() => {
  console.log(`The initial count is ${count.value}.`)
})
</script>

<template>
  <button @click="increment">Count is: {{ count }}</button>
</template>

