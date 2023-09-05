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