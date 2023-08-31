<script setup>

import { RouterView, useRouter } from 'vue-router';

const router = useRouter();

const redirect = (path) => {
  router.push(path)
}

</script>

<template>

  <button
    type="button"
    class="btn btn-primary"
    @click="redirect('/obra')"
  >Obra</button>

  <button
    type="button"
    class="btn btn-primary"
    @click="redirect('/obracusto')"
  >ObraCusto</button>

  <button
    type="button"
    class="btn btn-primary"
    @click="redirect('/localuso')"
  >LocalUso</button>

  <button
    type="button"
    class="btn btn-primary"
    @click="redirect('/fornecedor')"
  >Fornecedores</button>

  <button
    type="button"
    class="btn btn-primary"
    @click="redirect('/produtoservico')"
  >ProdutoServico</button>

  <button
    type="button"
    class="btn btn-primary"
    @click="redirect('/perfil')"
  >👤</button>

  <RouterView />

</template>

<style scoped>
</style>