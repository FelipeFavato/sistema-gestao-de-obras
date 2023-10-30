<script>

import { useRouter } from 'vue-router';
import axios from 'axios';

export default {
  data () {
    return {
      usuarios: [],
      useRouter: useRouter(),
      httpStatus: '',
      localStorageToken: ''
    }
  },

  methods: {
    redirectToLogin () {
      this.useRouter.push('/login')
    },
    setHttpStatusCode (successError) {
      this.httpStatus = successError
    },
    getLocalStorageToken () {
      this.localStorageToken = localStorage.getItem('token');
    },
    validateLogin () {
      !this.localStorageToken ? this.redirectToLogin() : null;
    },
    validateHttpStatus (status) {
      this.setHttpStatusCode(status);
      this.httpStatus === 403 ? this.redirectToLogin(): null;
    },
    fetchUsuarioInfoDB () {
      axios.get('/api/usuario',
      {
        headers: {
          Authorization: this.localStorageToken
        }
      }).then(({ data, status }) => {
          this.usuarios = data;
          this.setHttpStatusCode(status);
        }).catch(error => {
          this.validateHttpStatus(error.response.status);
        })
    },
  },

  mounted () {
    this.getLocalStorageToken();
    this.validateLogin();
    this.fetchUsuarioInfoDB ();
  }
}


</script>

<template>
  <h2>Home</h2>
  <p>Home</p>

  <!-- Tabela -->
  <!-- <main class="middle-margin table-responsive">
    <table class="table table-hover">
      <thead>
        <tr>
          <th scope="col">ID</th>
          <th scope="col">Nome</th>
          <th scope="col">Email</th>
          <th scope="col">Senha</th>
          <th scope="col">Perfil</th>
          <th></th>
          <th></th>
          <th></th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="(usuario, i) in usuarios" :key="i">
          <th scope="row">{{ usuario.codigo }}</th>
          <td>{{ usuario.nome }}</td>
          <td>{{ usuario.email }}</td>
          <td>{{ usuario.senha }}</td>
          <td>{{ usuario.tipoPerfil }}</td>
          <td></td>
          <td></td>
          <td class="editar-excluir">
            <button
              type="button"
              class="btn btn-light btn-sm small"
              title="Editar"
              data-bs-toggle="modal"
              data-bs-target="#updateModal"
              @click=""
            ><img src="../assets/imagens/editar.png" alt="lata de lixo"></button>
            <button
              type="button"
              class="btn btn-light btn-sm small"
              title="Excluir"
              data-bs-toggle="modal"
              data-bs-target="#deleteModal"
              @click=""
            ><img src="../assets/imagens/lata-de-lixo.png" alt="lata de lixo"></button>
          </td>
        </tr>
      </tbody>
    </table>
  </main> -->
</template>

<setup>
</setup>