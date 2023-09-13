<script setup>

import { useRoute, useRouter } from 'vue-router';

const route = useRoute();
const router = useRouter();

console.log(route.params.id);
console.log(route.query.name);

// redireciona para a home
router.push('/');

</script>

<template>
  <h2>Product</h2>
</template>

<style scope>
</style>