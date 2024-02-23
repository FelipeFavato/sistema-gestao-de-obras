<script>

import axios from 'axios';
import { useRouter } from 'vue-router';

export default {
  data () {
    return {
      usuariosInfo: [],
      localStorageToken: null,
      localStorageEmail: null,
      sessionUser: {},
      useRouter: useRouter(),
      perfilGestor: false
    }
  },

  methods: {
    // Método para buscar o 'this.localStorageToken'.
    getLocalStorageToken () {
      this.localStorageToken = localStorage.getItem('token');
    },
    // Método para recuperar o 'this.localStorageEmail'.
    getLocalStorageEmail () {
      this.localStorageEmail = localStorage.getItem('email');
    },
    // Recupera o usuario que logou.
    getSessionUser () {
      for (let user of this.usuariosInfo) {
        if (user.email === this.localStorageEmail) {
          this.sessionUser = user;
        }
      }
    },
    // Define as renderizações para cada tipo de perfil.
    setUserRole () {
      this.sessionUser.role === 'ADMIN' ? this.perfilGestor = true : this.perfilGestor = false;
    },
    // Método para redirecionar para a tela de LOGIN após clicar em 'Sair'.
    redirectToLogin () {
      this.useRouter.push('/login');
    },
    // Método que exclue o token e redireciona para a tela de LOGIN.
    logout () {
      localStorage.removeItem('token');
      this.redirectToLogin();
    },
    fetchUsuarios (callback) {
      axios.get('/api/usuario',
      {
        headers: {
          Authorization: this.localStorageToken
        }
      }).then(res => {
        this.usuariosInfo = res.data;
        if (callback) callback();
      }).catch((error) => {
        console.log(`Restrição (código ${error.response.status}).`)
      })
    }
  },

  mounted () {
    this.getLocalStorageToken();
    this.getLocalStorageEmail();
    this.fetchUsuarios(() => {
      this.getSessionUser();
      this.setUserRole();
    });
    this.$emit('menuHeight', this.$el.clientHeight);
  }
}

</script>

<template>
  <nav class="navbar navbar-expand-lg bg-body-tertiary margin-bottom" data-bs-theme="dark">
    <div class="container-fluid">

      <!-- Logo/Nome => link clicável para Home -->
      <a class="navbar-brand" href="#">
        <img class="logo" src="../assets/imagens/capacete-branco.png" alt="Logo">
      </a>
      <a class="navbar-brand" href="#">
        HUB 
        <p class="constr">Construções</p>
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
            <a class="nav-link" href="#compra">Custos</a>
          </li>

          <li class="nav-item">
            <a class="nav-link" href="#dashboard">Dashboard</a>
          </li>

          <li class="nav-item">
            <a class="nav-link" href="#relatorio">Relatórios</a>
          </li>

          <li class="nav-item dropdown">
            <a class="nav-link dropdown-toggle" href="#" role="button" data-bs-toggle="dropdown" aria-expanded="false">
              Apoio
            </a>
            <ul class="dropdown-menu">
              <li><a class="dropdown-item" href="#arquivo">Arquivos</a></li>
              <li><a class="dropdown-item" href="#fornecedor">Fornecedores</a></li>
              <li><a class="dropdown-item" href="#localuso">Local de uso</a></li>
              <li><a class="dropdown-item" href="#obra">Obras</a></li>
              <li><a class="dropdown-item" href="#produto">Produtos</a></li>
              <li><a class="dropdown-item" href="#marca">Marcas</a></li>
              <li><a class="dropdown-item" href="#socio">Sócios</a></li>
              <li><a class="dropdown-item" href="#unidademedida">Unidades de medida</a></li>
              <li v-if="perfilGestor"><hr class="dropdown-divider"></li>
              <li v-if="perfilGestor"><a class="dropdown-item" href="#usuario">Usuários</a></li>
            </ul>
          </li>

        </ul>
        <div class="d-flex separate">

          <!-- Link - Perfil -->
          <a class="nav-link small" href="#perfil" title="Perfil"><img src="../assets/imagens/perfil.png" alt="Perfil"></a>

          <!-- Link - Configurações -->
          <a class="nav-link small" href="#configuracoes" title="Configurações"><img src="../assets/imagens/engrenagem.png" alt="Engrenagem"></a>

          <!-- Botão - Sair -->
          <button
            type="button"
            class="nav-link small"
            title="Sair"
            @click="logout"
          ><img src="../assets/imagens/logout.png" alt="Sair">
          </button>
        
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

.constr {
  font-size: small;
  margin-bottom: 0.1px;
}
</style>