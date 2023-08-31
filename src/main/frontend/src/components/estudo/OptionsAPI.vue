<!--  -->

<script>
// Exportação de um objeto(JS) de configuração do componente com
// todas as suas OPÇÕES: 'data', 'methods', 'mounted'
export default {
  // data() => é uma opção do componente que define o estado do componente
  // A função data() retorna um objeto com a propriedades que eu deseje
  // que sejam reativas
  data() {
    return {
      count: 0
    }
  },

  // Aqui são definidos os métodos que podem ser chamados no componente
  methods: {
    increment() {
      this.count++
    }
  },

  // Gancho de ciclo de vida: A função mounted() é executada assim que o
  // componente é montado no DOM
  mounted() {
    console.log(`The initial count is ${this.count}.`)
  }
}
</script>

<template>
  <button @click="increment">Count is: {{ count }}</button>
</template>