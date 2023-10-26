<script>
// Requisições de login => http://localhost:8080/api/auth/login
// {
//   "email": "matheusfavato@gmail.com",
//   "senha": "159753"
// }

// {
//   "email": "felipelopesfavato@gmail.com",
//   "senha": "123456"
// }
import axios from 'axios';
import { useRouter } from 'vue-router';

export default {
  data () {
    return {
      router: useRouter(),
      email: '',
      senha: '',
      httpStatusCode: ''
    }
  },

  methods: {
    // Método para redirecionar para a 'HOME' em caso de sucesso.
    redirectToHome () {
      this.router.push('/');
    },
    // Método para guardar o token gerado no LocalStorage.
    saveTokenLocalStorage (token) {
      localStorage.setItem('token', token);
    },
    // Método para setar o 'this.httpStatusCode' com os casos de sucesso e erro.
    setHttpStatusCode (succesError) {
      this.httpStatusCode = succesError;
    },
    // Método que recebe a requisição de login do usuário e trata o erro em caso de erro.
    login (callback) {
      axios.post('/api/auth/login',
      {
        email: this.email,
        senha: this.senha
      }).then((res) => {
        this.saveTokenLocalStorage(res['data']['token']);
        this.redirectToHome();
      }).catch((error) => {
        this.setHttpStatusCode(error.response.status);
      })
    },
  },

  mounted () {

  },

}

</script>

<template>
  <h2>login</h2>

  <form action="POST">

    <input
      type="text"
      placeholder="usuario@gmail.com"
      v-model="email"
    >

    <input
      type="text"
      placeholder="Sua senha"
      v-model="senha"
    >
    
    <button
      id="login-button"
      type="button"
      @click="login"
    >Login
    </button>

  </form>

  <p v-if="httpStatusCode === 403">Usuário e/ou senha inválidos.</p>

</template>

<style>
</style>
