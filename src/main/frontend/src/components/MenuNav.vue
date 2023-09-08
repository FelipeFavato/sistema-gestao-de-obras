<script setup>

import { useRouter } from 'vue-router';

const router = useRouter();

const redirect = (path) => {
  router.push(path)
}

</script>

<template>

  <nav class="navbar bg-dark border-bottom border-body" data-bs-theme="dark">

    <div class="menu-options">
      <img
        class="logo"
        src="../assets/capacete.png"
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
      >ProdutoServiço</button>
    </div>

    <div class="perfil-configuracoes">
      <button
        type="button"
        class="btn btn-light"
        @click="redirect('/perfil')"
        title="Perfil"
        ><img src="../assets/perfil.png" alt="Perfil" class="">
      </button>

      <button
        type="button"
        class="btn btn-light"
        @click="redirect('/configuracoes')"
        title="Configurações"

        ><img src="../assets/engrenagem.png" alt="Engrenagem">
      </button>

      <button
        type="button"
        class="btn btn-light"
        title="Sair"
        ><img src="../assets/logout.png" alt="Sair">
      </button>
    </div>
    
  </nav>

</template>

<style scoped>
nav {
  height: 58px;
}

.logo {
  height: 50px;
  width: 50px;
}

.menu-options {
  display: flex;
  width: 700px;
  justify-content: space-between;
  margin: -17px 0px 0px 5px;
}

.menu-options button {
  background-color: transparent;
  border: none;
  margin-top: 10px;
}
.menu-options button:hover {
  color: white;
  font-size: 1.005em;
}

.perfil-configuracoes {
  display: flex;
  justify-content: flex-end;
  width: 80px;
  margin-bottom: 10px;
}

.perfil-configuracoes button {
  display: flex;
  background-color: transparent;
  border: none;
  height: 40px;
  width: 100px;
  margin-top: 10px;
  margin-right: 5px;
}

.perfil-configuracoes button img {
  height: 20px;
  width: 20px;
}
</style>