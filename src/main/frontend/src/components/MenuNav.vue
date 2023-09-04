<script setup>

import { useRouter } from 'vue-router';

const router = useRouter();

const redirect = (path) => {
  router.push(path)
}

</script>

<template>

  <main>

    <div class="menu-options">
      <img
        class="logo"
        src="../assets/construction-logo.webp"
        alt="Logo"
      >

      <button
        type="button"
        class="btn btn-ligth"
        @click="redirect('/')"
      >Início</button>

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

    <div class="perfil-configuracoes">
      <button
        type="button"
        class="btn btn-light"
        @click="redirect('/perfil')"
        ><img src="../assets/perfil.png" alt="Engrenagem">
      </button>

      <button
        type="button"
        class="btn btn-light"
        @click="redirect('/configuracoes')"
        ><img src="../assets/engrenagem.png" alt="Engrenagem">
      </button>
    </div>

  </main>

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
  width: 500px;
  justify-content: space-between;
}

.menu-options button {
  display: flex;
  background-color: transparent;
  color:#C0C0C0;
  border: none;
  height: 30px;
  margin-top: 15px;
}

.perfil-configuracoes{
  display: flex;
  justify-content: flex-end;
  width: 80px;
}

.perfil-configuracoes button {
  display: flex;
  background-color: transparent;
  color:white;
  border: none;
  height: 40px;
  width: 100px;
  margin-top: 15px;
}

.perfil-configuracoes button img {
  height: 25px;
  width: 25px;
}

.logo {
  height: 70px;
  width: 70px;
}


</style>