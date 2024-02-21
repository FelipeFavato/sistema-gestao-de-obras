<script>

import { RouterView } from 'vue-router';
import MenuNav from './components/MenuNav.vue';


export default {

  data () {
    return {
      menuHeight: 0
    }
  },

  components: {
    MenuNav,
  },

  computed: {
    isNotLoginPage() {
      return this.$route.path !== "/login"
    }
  },

  methods: {
    setMenuHeight(height) {
      // Receber a altura do menu de navegação e ajustar a altura da barra lateral
      this.menuHeight = height;
    },
  },

  mounted () {
  }
}

</script>

<template>
  <header v-if="isNotLoginPage" class="App-header">
    <MenuNav @menuHeight="setMenuHeight" />
  </header>

  <main>
    <RouterView :alturaMenu="menuHeight" />
  </main>
</template>

<style setup>
body {
  background-color: rgb(240, 240, 240);
}
</style>
