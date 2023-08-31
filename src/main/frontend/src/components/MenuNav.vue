<script setup>

import { RouterView, useRouter } from 'vue-router';

const router = useRouter();

const redirect = (path) => {
  router.push(path)
}

</script>

<template>

  <main>
    <div class="botao-inicio">
      <button
        type="button"
        class="btn btn-dark"
        @click="redirect('/')"
      >Início</button>
    </div>

    <div class="menu-options">
      <button
        type="button"
        class="btn btn-ligth"
        @click="redirect('/obra')"
      >Obra</button>

      <button
        type="button"
        class="btn btn-ligth"
        @click="redirect('/obracusto')"
      >ObraCusto</button>

      <button
        type="button"
        class="btn btn-ligth"
        @click="redirect('/localuso')"
      >LocalUso</button>

      <button
        type="button"
        class="btn btn-ligth"
        @click="redirect('/fornecedor')"
      >Fornecedores</button>

      <button
        type="button"
        class="btn btn-ligth"
        @click="redirect('/produtoservico')"
      >ProdutoServico</button>
    </div>

    <div class="botao-perfil">
      <button
        type="button"
        class="btn btn-dark"
        @click="redirect('/perfil')"
      >👤</button>
    </div>

  </main>

  <RouterView />

</template>

<style scoped>
main {
  margin: 25px 5px 40px 5px;
  display: flex;
  align-content: space-around;
  justify-content: space-between;
}

/* .botao-inicio {
} */

.menu-options {
  display: flex;
  width: 900px;
  justify-content: space-between;
}

/* .botao-perfil {
} */


</style>