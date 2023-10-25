<script>
import {RouterLink, RouterView, useRouter} from 'vue-router';

export default {
  data () {
    return {
      router: useRouter()
    }
  },

  methods: {
    redirect (path) {
      this.router.push(path)
    },
    login () {
      this.redirect('/aplicacao')
    }
  },

  mounted () {

  },

}

</script>

<template>
  <h2>login</h2>

  <form @submit.prevent="login">
    <input type="text" placeholder="usuario@gmail.com">
    <input type="text" placeholder="Sua senha">
    <button type="submitt">Login</button>
  </form>
</template>

<style>
</style>
