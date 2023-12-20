<script>
// /api/jasper/produto/{categoria}/{marca}
import axios from 'axios';
import { useRouter } from 'vue-router';


export default {
  data () {
    return {
      localStorageToken: null,
      useRouter: useRouter(),
      retorno: ''
    }
  },

  methods: {
    // Método para redirecionar para a página de login.
    redirectToLogin () {
      this.useRouter.push('/login');
    },
    // Método para recuperar o token do localStorage e preencher 'this.localStorageToken'.
    getLocalStorageToken () {
      this.localStorageToken = localStorage.getItem('token');
    },
    // Método para setar o 'this.httpStatus' com os casos de sucesso e erro.
    setHttpStatusCode (succesError) {
      this.httpStatus = succesError;
    },
    // Método para validar o login baseado no token.
    validateLogin () {
      !this.localStorageToken ? this.redirectToLogin() : null;
    },
    // Método para validar o StatusHttp da requisição. Casos de token expirado.
    validateHttpStatus (status) {
      this.setHttpStatusCode(status);
      this.httpStatus === 403 ? this.redirectToLogin(): null;
    },
    gerarPDF (callback) {
      axios.get('/api/jasper/produto/todos/todos',
      {
        headers: {
          Authorization: this.localStorageToken
        }
      }).then(res => {
        this.retorno = res;
        if (callback) callback();
      }).catch(error => {
        this.retorno = error;
        if (callback) callback();
      })
    }
  },

  mounted () {
    this.getLocalStorageToken();
  }
}


</script>


<template>
  <h2>Relatórios</h2>
  <button
    @click="gerarPDF(console.log(retorno))"
  >
    Gerar
  </button>
</template>


<style>
</style>