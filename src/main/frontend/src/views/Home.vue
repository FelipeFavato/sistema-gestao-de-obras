<script>

import { useRouter } from 'vue-router';
import axios from 'axios';

export default {
  data () {
    return {
      // Variáveis de autenticação/autorização: --\
      useRouter: useRouter(),
      httpStatus: '',
      localStorageToken: null,
      /////////////////////////////////////////////
      // Variáveis de comportamento: -------------\
      users: [],
      localStorageEmail: '',
      sessionUser: {},
      /////////////////////////////////////////////
    }
  },

  props: {
    alturaMenu: Number,
  },

  methods: {
    // Validações de login: ------------------------------------------------------\
    redirectToLogin () {
      this.useRouter.push('/login');
    },
    setHttpStatusCode (successError) {
      this.httpStatus = successError;
    },
    getLocalStorageToken (callback) {
      this.localStorageToken = localStorage.getItem('token');
      if (callback) callback();
    },
    validateLogin () {
      !this.localStorageToken ? this.redirectToLogin() : 'Usuário autenticado.';
    },
    validateHttpStatus (status) {
      this.setHttpStatusCode(status);
      this.httpStatus === 403 ? this.redirectToLogin(): 'Usuário autenticado.';
    },
    ///////////////////////////////////////////////////////////////////////////////

    // Métodos de busca - GET: ---------------------------------------------------\
    fetchUsuariosInfoDB () {
      axios.get('/api/usuario',
      {
        headers: {
          Authorization: this.localStorageToken
        }
      }).then(res => {
        this.users = res.data;
        this.validateHttpStatus(res.status);
        this.getSessionUser();
      }).catch(error => {
        this.validateHttpStatus(error.response.status)
      })
    },
    ///////////////////////////////////////////////////////////////////////////////

    // Métodos de comportamento: -------------------------------------------------\
    getLocalStorageEmail () {
      this.localStorageEmail = localStorage.getItem('email');
    },
    getSessionUser () {
      this.getLocalStorageEmail();
      for (let user of this.users) {
        if (this.localStorageEmail === user['email']) {
          this.sessionUser = user;
        }
      }
    },
    ///////////////////////////////////////////////////////////////////////////////
  },

  mounted () {
    this.getLocalStorageToken(() => {
      this.validateLogin();
    });
    this.fetchUsuariosInfoDB();
  }
}


</script>

<template>

  <!-- Atualizações -->
  <main class="flex">

  </main>
  
</template>

<style setup>

.title {
  display: flex;
  justify-content: center;
  font-weight: bold;
  font-family: monospace;
}

.border {
  border: 1px solid rgba(153, 153, 153, 1);
}

.margin-10 {
  margin: 10px;
}

.pad-10 {
  padding-left: 25px;
  padding-right: 25px;
  padding-top: 10px;
  padding-bottom: 10px;
}

.graph {
  border: 1px solid rgba(153, 153, 153, 1);
  width: 602px;
  /* border-radius: 20px; */
  margin-top: 15px;
  margin-left: 15px;
}

@media only screen and (max-width: 600px) {
  .graphics {
    width: 300px;
    height: 200px;
  }
}

.flex {
  display: flex;
}

.column {
  display: flex;
  justify-content: space-between;
}

.light-green {
  /* background-color: #006400; */
  /* background-color: #003300; */
  background-color: #3D8B37;
}

.dark-grey {
  background-color: #333333;
}

.editar-excluir {
  display: flex;
  justify-content: flex-end;
}

.small img {
  height: 15px;
  width: 15px;
}

.middle-margin {
  margin: 10px 15px 10px 15px;
}

.bold {
  font-weight: bold;
}

.red-letter {
  color: red;
}

.green-letter {
  color:green;
}

.grey-letter {
  color: #212529;
}

</style>