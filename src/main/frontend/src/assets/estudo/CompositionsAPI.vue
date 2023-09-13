<!-- CompositionsAPI => A logica dos componentes é
definida usando funções API importadas -->
<!-- setup: Permite que o Vue, em tempo de compilação, realize transformações -->
<script setup>
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