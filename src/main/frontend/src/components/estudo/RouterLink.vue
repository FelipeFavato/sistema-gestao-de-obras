<!-- <script src="https://unpkg.com/vue@3"></script> -->
<!-- <script src="https://unpkg.com/vue-router@4"></script> -->

<template>
  <div id="app">
    <h1>Hello App!</h1>
    <p>
      <!-- Componente router-link => Navegação -->
      <!-- Especifica o "/link" passando para a prop 'to' -->
      <!-- `<router-link>` renderiza uma tag `<a>` com o atributo `href` correto -->
      <router-link to="/">Go to Home</router-link>
      <router-link to="/about">Go to About</router-link>
    </p>
    <!-- route outlet -->
    <!-- component matched by the route will render here -->
    <router-view></router-view>
  </div>
</template>