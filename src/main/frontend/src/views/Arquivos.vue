<script>
import { useRouter } from 'vue-router';
import axios from 'axios';

export default {
  data () {
    return {
      // Validações de login -----\
      useRouter: useRouter(),
      localStorageToken: null,
      httpStatus: '',
      /////////////////////////////
      info: [],
      imageData: '',
      show: false

    }
  },

  props: {
    alturaMenu: Number,
  },
    
  methods: {
    // Validações de Login:--------------------------------------------------------------\
    // Redireciona para a página de login.
    redirectToLogin () {
      this.useRouter.push('/login');
    },
    // Recupera o token do localStorage e preencher 'this.localStorageToken'.
    getLocalStorageToken () {
      this.localStorageToken = localStorage.getItem('token');
    },
    // Seta o 'this.httpStatus' com os casos de sucesso e erro.
    setHttpStatusCode (succesError) {
      this.httpStatus = succesError;
    },
    // Valida o login baseado no token.
    validateLogin () {
      !this.localStorageToken ? this.redirectToLogin() : null;
    },
    // Valida o StatusHttp da requisição. Casos de token expirado.
    validateHttpStatus (status) {
      this.setHttpStatusCode(status);
      this.httpStatus === 403 ? this.redirectToLogin(): null;
    },
    //////////////////////////////////////////////////////////////////////////////////////
    fetchInfoDB (callback) {
      axios.get('/api/obra-arquivos',
      {
        headers: {
          Authorization: `Bearer ${this.localStorageToken}`
        },
      })
      .then(response => {
        const imageData = response.data[0].conteudoArquivo;
        console.log(typeof(imageData))
        // Convert the image data to a base64-encoded string
        this.imageData = 'data:image/jpeg;base64,' + imageData;
      }).catch(error => {
        console.error('Error fetching image data:', error);
      });
    }
  },

  mounted () {
    this.getLocalStorageToken();
    this.fetchInfoDB();
  }
}

</script>

<template>
  <h2>Arquivos</h2>
  <div>
    <input type="file">
  </div>

  <!-- Arquivos -->
  <div>
      <img v-if="imageData" :src="this.imageData"  style="height: 200px; width: 200px;" alt="Image" />
      <div v-else>Loading...</div>
  </div>

</template>

<style scope>
</style>