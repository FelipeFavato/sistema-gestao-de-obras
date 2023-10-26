<script setup>

  import { RouterView } from 'vue-router';
  import MenuNav from './components/MenuNav.vue';

</script>

<template>
  <header class="App-header">
    <MenuNav />
  </header>

  <main>
    <RouterView />
  </main>
</template>

<style scoped>
</style>
