<script setup>

import { RouterView } from 'vue-router';
import MenuNav from './components/MenuNav.vue';

</script>

<template>
  <header>
    <MenuNav />
  </header>

  <main>
    <RouterView />
  </main>
</template>

<style scoped>
header {
  background-color: #363A57;
  /* background-color: #00008B; */
}
</style>
