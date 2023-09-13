<script setup>

const props = defineProps({
  name: String,
  function: Function,
});
</script>

<template>
  <button class="Button">
    <component 
      :is="props.name"
      @click="props.function"
    />
    <!-- slot permite passar qualquer nome ao botão -->
    <slot />
  </button>
</template>

<style>
</style>