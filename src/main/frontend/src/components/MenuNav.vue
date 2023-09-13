<script setup>

import { useRouter } from 'vue-router';

const router = useRouter();

const redirect = (path) => {
  router.push(path)
}

</script>

<template>
  <nav class="navbar navbar-expand-lg bg-body-tertiary" data-bs-theme="dark">
    <div class="container-fluid">

      <!-- Logo => link clicável para Home -->
      <a
        class="navbar-brand"
        href="#"
      >
        <img
          class="logo"
          src="../assets/imagens/capacete.png"
          alt="Logo"
        >
      </a>


      <!-- Ativa => Menu navegação Celular -->
      <button
        class="navbar-toggler"
        type="button"
        data-bs-toggle="collapse"
        data-bs-target="#navbarSupportedContent"
        aria-controls="navbarSupportedContent"
        aria-expanded="false"
        aria-label="Toggle navigation"
      ><span class="navbar-toggler-icon"></span>
      </button>

      <!-- Menu navegação - Rotas -->
      <div class="collapse navbar-collapse" id="navbarSupportedContent">
        <ul class="navbar-nav me-auto mb-2 mb-lg-0">

          <li class="nav-item">
            <a class="nav-link" href="#obra">Obra</a>
          </li>

          <li class="nav-item">
            <a class="nav-link" href="#obracusto">ObraCusto</a>
          </li>

          <li class="nav-item">
            <a class="nav-link" href="#localuso">Local de uso</a>
          </li>

          <li class="nav-item">
            <a class="nav-link" href="#fornecedor">Fornecedores</a>
          </li>

          <li class="nav-item">
            <a class="nav-link" href="#produtoservico">Produto/Serviço</a>
          </li>

        </ul>
        <div class="d-flex separate">

          <a class="nav-link small" href="#perfil" title="Perfil"><img src="../assets/imagens/perfil.png" alt="Perfil"></a>

          <a class="nav-link small" href="#configuracoes" title="Configurações"><img src="../assets/imagens/engrenagem.png" alt="Engrenagem"></a>

          <a class="nav-link small" href="#perfil" title="Sair"><img src="../assets/imagens/logout.png" alt="Sair"></a>
        </div>
      </div>
    </div>

  </nav>
</template>

<style scoped>
.small img {
  height: 25px;
  width: 25px;
}

.logo {
  height: 50px;
  width: 50px;
}

.separate {
  display: flex;
  justify-content: space-around;
  width: 150px;
}
</style>