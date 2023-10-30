<script>

import { RouterView } from 'vue-router';
import MenuNav from './components/MenuNav.vue';

export default {

  components: {
    MenuNav,
  },

  computed: {
    isNotLoginPage() {
      return this.$route.path !== "/login"
    }
  },

  mounted () {
  }
}

</script>

<template>
  <header v-if="isNotLoginPage" class="App-header">
    <MenuNav />
  </header>

  <main>
    <RouterView />
  </main>
</template>

<style scoped>
</style>
