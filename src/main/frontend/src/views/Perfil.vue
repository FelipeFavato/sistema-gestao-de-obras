<script>

export default {
  data () {
    return {
      nomeLocalUsoObra: '',
      requiredRedBorder: '',
      modal: 'no-closing-modal'
    }
  },

  props: {
    alturaMenu: Number,
  },

  components: {

  },

  methods: {
    showNome () {
      if (this.nomeLocalUsoObra === '') {
        this.requiredRedBorder = 'required-red-border'
        this.modal = 'no-closing-modal';
      } else {
        this.requiredRedBorder = '';
        this.modal = 'modal';
      }
    },
  },

  mounted () {
  },

  unmounted () {
  }
}
</script>

<template>

  <header class="header middle-margin">
    <button
      type="button"
      class="btn btn-success light-green"
      data-bs-toggle="modal"
      data-bs-target="#insertModal"
      id="novoLocalButton"
    >+ Novo Local de Uso</button>
  </header>

  <!-- InsertModal -->
  <div class="modal fade" id="insertModal" data-bs-backdrop="static" data-bs-keyboard="false" tabindex="-1" aria-labelledby="insertModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-dialog-scrollable">
      <div class="modal-content">
        <div class="modal-header">
          <h1 class="modal-title fs-5" id="insertModalLabel">Novo Local de Uso</h1>
          <button @click="cancel" type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
        </div>

        <div class="modal-body">
          <form action="POST">

            <div class="mb-3">
              <label for="insert-name-input" class="form-label bold red-asterisk">Local:</label>
              <input
                :class="this.requiredRedBorder"
                type="text"
                class="form-control"
                id="insert-name-input"
                required
                placeholder="Fundação, Hidráulica, etc..."
                v-model="nomeLocalUsoObra"
                @keyup="showNome"
                maxlength="30">
            </div>

          </form>
        </div>

        <div class="modal-footer">
          <button
            id="fecharInsertButton"
            type="button"
            class="btn btn-secondary dark-grey"
            data-bs-dismiss="modal"
            @click="cancel"
          >Fechar</button>
          <button
          id="salvarNovoButton"
          type="button"
          class="btn btn-success  light-green"
          :data-bs-dismiss="modal"
          @click="showNome"
          >Salvar</button>
        </div>
      </div>
    </div>
  </div>

</template>

<style scope>
@keyframes piscar {
    0%, 100% {
        border-color: #ff0000; /* Vermelho inicial */
    }
    50% {
        /* border-color: #FFC0CB; */
        border-color: #FF69B4;
    }
}


.required-red-border {
  border: 2px solid red;
  animation: piscar 2s infinite;
}

.red-asterisk::after {
  content: " *";
  color: red;
}

</style>