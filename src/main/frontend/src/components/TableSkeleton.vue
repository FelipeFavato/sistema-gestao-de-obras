<template>
  <!-- Header com o botão de +Novo -->
  <!-- class="btn btn-success" -->
  <header class="header middle-margin">
    <button
      type="button"
      class="add-button"
    >aaa</button>
  </header>

  <!-- Tabela -->
  <main class="middle-margin table-responsive">
    <table class="table table-hover">

      <thead class="">
        <tr>
          <th scope="col"></th>
          <th scope="col"></th>
          <th scope="col"></th>
          <th></th>
          <th></th>
          <th></th>
          <th></th>
          <th></th>
        </tr>
      </thead>

      <tbody>
        <tr v-for="(i) in 10" :key="i">
          <th scope="row"></th>
          <td></td>
          <td></td>
          <td></td>
          <td></td>
          <td></td>
          <td></td>
          <td class="flex-end">
            <button
              type="button"
              class="btn btn-light btn-sm small"
            ><img></button>
            <button
              type="button"
              class="btn btn-light btn-sm small"
            ><img ></button>
          </td>
        </tr>
      </tbody>

    </table>

  </main>
</template>

<style>
.add-button {
  animation: pulse-bg 1.5s infinite;
}

@keyframes pulse-bg {
  0% {
    background-color: #f0f0f0;
  }
  50% {
    background-color: #dcdcdc;
  }
  100% {
    background-color: #f0f0f0;
  }
}
</style>