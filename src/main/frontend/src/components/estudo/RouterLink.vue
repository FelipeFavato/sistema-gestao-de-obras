<script setup>

import {RouterLink, RouterView, useRouter} from 'vue-router';

const router = useRouter()

const redirect = (path) => {
  router.push(path)
}

</script>

<template>
  <!-- <router-link to="/">Home</router-link>
  <router-link to="/about">About</router-link> -->
  <!-- <router-view /> -->

  <!-- <RouterLink to="/"></RouterLink> -->
  <!-- <RouterLink to="/about">About</RouterLink> -->
  <!-- <RouterLink :to="{name: 'product', params: {id: 12}}">Product</RouterLink> -->
  <RouterView />
</template>