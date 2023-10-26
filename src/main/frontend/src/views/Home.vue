<script>

import { useRouter } from 'vue-router';
import axios from 'axios';

export default {
  data () {
    return {
      produtos: [],
      useRouter: useRouter(),
      httpStatus: '',
      localStorageToken: ''
    }
  },

  methods: {
    redirect (path) {
      this.useRouter.push(path)
    },
    fetchProdutoInfoDB (callback) {
      axios.get('/api/produto').then(
        (res) => { 
          console.log('res: ', res);
          if (callback) callback();
        }).catch(error => {
          console.log('error: ', error.response.status);
          this.httpStatus = error.response.status;
          console.log('httpStatus: ', this.httpStatus);
          if (callback) callback();
        })
    },
    getLocalStorageToken () {
      this.localStorageToken = localStorage.getItem('token');
    },
    fetchProduto (callback) {
      axios.get("/api/produto",
        {
          headers: {
            Authorization: this.localStorageToken
          }
        }).then((res) => { 
          this.produtos = res.data
          if (callback) callback();
        }).catch(error => {
          this.httpStatus = error.response.status;
          if (callback) callback();
        })
    },
    validateLogin () {
      this.fetchProduto(() => {
        if (this.httpStatus === 403) {
          this.redirect('/login');
        }
      })
    }
  },

  mounted () {
    this.getLocalStorageToken();
    // this.fetchProdutoInfoDB();
    this.validateLogin();
  }
}


</script>

<template>
  <h2>Home</h2>
  <p>Home</p>
  <p>{{ this.produtos }}</p>
</template>

<setup>
</setup>