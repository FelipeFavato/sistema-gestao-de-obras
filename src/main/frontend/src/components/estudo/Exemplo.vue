<!-- SFC => Single File Component -->

<!-- JavaScript para a logica do componente -->
<!-- <script setup> é uma sintaxe introduzida no Vue3 que define o bloco script -->
<script setup>
// ref => Função de reatividade. Cria uma variavel reativa = Atualizam a interface a cada mudança de valor
import { ref } from 'vue'
const count = ref(0)
</script>

<!-- HTML com diretivas Vue -->
<!-- <template> Seção que define a estrutura do componente que será renderizado na tela -->
<template>
  <!-- @click => Diretiva que escuta o evento de clique, quando o botão é clicado,
  o código dentro de @click é executado -->
  <button @click="count += 1">
    <!-- {{ count }}: Sintaxe usada para interpolação de dados dentro de um template.
    Cria uma conexão entre o código JS e o HTML renderizado -->
    Count is: {{ count }}
  </button>
</template>

<!-- CSS para estilização do componente -->
<!-- scoped: Palavra usada para criar estilos encapsulados que se aplicam
apenas aos elementos dentro do próprio componente -->
<style scoped>
button {
  font-weight: bold;
}
</style>