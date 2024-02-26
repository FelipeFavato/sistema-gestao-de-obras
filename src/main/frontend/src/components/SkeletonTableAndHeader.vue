<template>
  <!-- Header com o botão de +Novo -->
  <header class="header middle-margin">
    <button
      type="button"
      class="btn loading-elements"
    >Botão padrão</button>
  </header>

  <!-- Tabela -->
  <main class="middle-margin table-responsive">
    <table class="table table-hover">

      <thead class="tamanho-fonte-18">
        <tr>
          <th scope="col"><p class="loading-elements">Padrão</p></th>
          <th scope="col"><p class="loading-elements">Padrão</p></th>
          <th scope="col"><p class="loading-elements">Padrão</p></th>
          <th scope="col"><p class="loading-elements">Padrão</p></th>
          <th scope="col"><p class="loading-elements">Padrão</p></th>
          <th></th>
          <th></th>
          <th></th>
        </tr>
      </thead>

      <tbody class="loading-elements">
        <tr v-for="(i) in 10" :key="i" class="loading-elements">
          <th scope="row"><p class="loading-elements">000</p></th>
          <td><p class="loading-elements">Padrão</p></td>
          <td><p class="loading-elements">Padrão</p></td>
          <td><p class="loading-elements">Padrão</p></td>
          <td><p class="loading-elements">Padrão</p></td>
          <td></td>
          <td></td>
          <td class="flex-end">
            <p class="loading-elements">aaa</p>
            <p class="loading-elements">aaa</p>
          </td>
        </tr>
      </tbody>

    </table>

  </main>
</template>

<style>
.loading-elements {
  border-radius: 5px;
  animation: pulse-bg 1.5s infinite, pulse-text 1.5s infinite;
}

@keyframes pulse-bg {
  0% {
    background-color: #f0f0f0;
  }
  50% {
    background-color: #bcbcbc;
  }
  100% {
    background-color: #f0f0f0;
  }
}

@keyframes pulse-text {
  0% {
    color: #f0f0f0;
  }
  50% {
    color: #bcbcbc;
  }
  100% {
    color: #f0f0f0;
  }
}
</style>