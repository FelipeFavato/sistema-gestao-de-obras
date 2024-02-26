<template>
  <!-- Header com o botão de +Novo -->
  <header class="header middle-margin">
    <button
      type="button"
      class="btn loading-elements"
    >Botão padrão</button>

    <button
      type="button"
      class="btn loading-elements"
    >Botão padrão</button>
  </header>

  <!-- Tabela -->
  <main class="margin-12 ">
    <div class="row row-cols-1 row-cols-md-3 g-4">

      <!-- Card de INSERT -->
      <div class="col col-6 col-md-4 col-lg-2 tamanho-cards">
          <div class="card h-100 loading-elements" style="height: 100%; width: 100%;">
            <div class="card-img-overlay" style="height: 100%; width: 100%;">
              <form  class="flex-display" style="height: 100%; width: 100%;">
                <div class="card-bodyflex" >
                </div>
              </form>
            </div>
          </div>
      </div>

      <!-- Lista de cards de Fotos. -->
      <div v-for="(i) in 15" :key="i" class="col col-6 col-md-4 col-lg-2 tamanho-cards">
        <div class="card h-100 align-center loading-elements" style="height: 100%; width: 100%;">
          <div class="card-img-overlay space-between-column hover-white-background">
          </div>
        </div>
      </div>

    </div>
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