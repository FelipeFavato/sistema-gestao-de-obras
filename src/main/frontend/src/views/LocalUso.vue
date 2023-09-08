<!-- Proximos passos:
  1 - Transformar em OPTIONSAPI
  2 - Fazer o FRONTEND para a rota localuso igual foi feita na rota de fornecedores
  3 - Aprender como faz os métodos POST, DELETE, PATCH
  4 - Usar o CrossOrigins no BACKEND para todos os Métodos -->
<script>
import { defineComponent, onMounted, ref } from 'vue';
import API from '../utils/API.js'

export default defineComponent({
  setup() {
    const pokemons = ref([]);

    const fetchPokemons = () => {
      API.get("/localuso").then((response) => pokemons.value = response.data)
    };

    onMounted(fetchPokemons);

    return { pokemons }
  },
})

</script>

<template>
  <h2>LocalUso</h2>

  <div v-for="(poke, i) in pokemons" :key="i">{{ poke.nomeLocalUsoObra }}</div>
</template>

<style scope>
</style>