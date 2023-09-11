<!-- Proximos passos:
  3 - Aprender como faz os métodos DELETE, PATCH
  4 - Usar o CrossOrigins no BACKEND para todos os Métodos -->
<script>
// import { defineComponent, onMounted, ref } from 'vue';
// import API from '../utils/API.js'
import axios from 'axios';

// export default defineComponent({
//   setup() {
//     const infoDB = ref([]);

//     const fetchInfoDB = () => {
//       API.get("/localuso").then((response) => infoDB.value = response.data)
//     };

//     onMounted(fetchInfoDB);

//     return { infoDB }
//   },
// })

export default {
  data () {
    return {
      info: [],
      codigoLocalUsoObra: '',
      nomeLocalUsoObra: '',
    };
  },

  methods: {
    cancel() {
      this.codigoLocalUsoObra = '';
      this.nomeLocalUsoObra = '';
    },
    fetchInfoDB () {
      axios.get("http://localhost:8080/localuso").then(
        (res) => this.info = res.data.sort((s1, s2) => s1.codigoLocalUsoObra - s2.codigoLocalUsoObra))
    },
    createInfoDB () {
      axios.post("http://localhost:8080/localuso", 
      {
        nomeLocalUsoObra: this.nomeLocalUsoObra
      }).then(() => this.fetchInfoDB());
      this.cancel();
    },
    fillUpdateModal (codigo, nome) {
      this.codigoLocalUsoObra = codigo;
      this.nomeLocalUsoObra = nome;
    },
    updateInfoDB (codigo, nome) {
      axios.put("http://localhost:8080/localuso",
        {
          codigoLocalUsoObra: Number(codigo),
          nomeLocalUsoObra: nome
        }).then(() => this.fetchInfoDB());
      this.cancel();
    },
    removeFromDB(codigo) {
      axios.delete("http://localhost:8080/localuso", {
        headers: {
          Authorization: ''
        },
        data: {
          codigoLocalUsoObra: codigo
        }
      }).then(() => this.fetchInfoDB())
    },
  },

  mounted () {
    this.fetchInfoDB()
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
    >+ Novo Local de Uso</button>
  </header>

  <!-- InsertModal -->
  <div class="modal" id="insertModal" tabindex="-1" aria-labelledby="insertModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-dialog-scrollable">
      <div class="modal-content">
        <div class="modal-header">
          <h1 class="modal-title fs-5" id="insertModalLabel">Novo Local de Uso</h1>
          <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
        </div>

        <div class="modal-body">
          <form action="POST">
            <div class="mb-3">
              <label for="category-input" class="form-label bold">Categoria:</label>
              <input
                type="text"
                class="form-control"
                id="category-input"
                placeholder="Alvenaria, Ferragens, etc..."
                v-model="nomeLocalUsoObra">
            </div>
          </form>
        </div>

        <div class="modal-footer">
          <button
            type="button"
            class="btn btn-secondary dark-grey"
            data-bs-dismiss="modal"
            @click="cancel"
          >Fechar</button>
          <button
            type="button"
            class="btn btn-success  light-green"
            data-bs-dismiss="modal"
            @click="createInfoDB"
          >Salvar</button>
        </div>
      </div>
    </div>
  </div>

  <!-- UpdateModal -->
  <div class="modal" id="updateModal" tabindex="-1" aria-labelledby="updateModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-dialog-scrollable">
      <div class="modal-content">
        <div class="modal-header">
          <h1 class="modal-title fs-5" id="updateModalLabel">Editar Local de Uso</h1>
          <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
        </div>

        <div class="modal-body">
          <form action="PUT">
            <div class="mb-3">
              <label for="id-input" class="form-label bold">Código:</label>
              <input
                type="text"
                class="form-control"
                id="id-input"
                placeholder=""
                disabled
                v-model="codigoLocalUsoObra">
            </div>

            <div class="mb-3">
              <label for="category-input" class="form-label bold">Categoria:</label>
              <input
                type="text"
                class="form-control"
                id="category-input"
                placeholder="Alvenaria, Ferragens, etc..."
                v-model="nomeLocalUsoObra">
            </div>

          </form>
        </div>

        <div class="modal-footer">
          <button type="button" class="btn btn-secondary dark-grey"
            data-bs-dismiss="modal" @click="cancel"
          >Fechar</button>

          <button type="button" class="btn btn-success  light-green" data-bs-dismiss="modal"
            @click="updateInfoDB(this.codigoLocalUsoObra, this.nomeLocalUsoObra)"
          >Salvar</button>
        </div>
      </div>
    </div>
  </div>

  <main class="middle-margin">
    <table class="table table-hover">
      <thead>
        <tr>
          <th scope="col">Código</th>
          <th scope="col">Categoria</th>
          <th></th>
          <th></th>
          <th></th>
          <th></th>
          <th></th>
          <th></th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="(localUso, i) in info" :key="i">
          <th scope="row">{{ localUso.codigoLocalUsoObra }}</th>
          <td>{{ localUso.nomeLocalUsoObra }}</td>
          <td></td>
          <td></td>
          <td></td>
          <td></td>
          <td></td>
          <td class="editar-excluir">
            <button
              type="button"
              class="btn btn-light btn-sm small"
              title="Editar"
              data-bs-toggle="modal"
              data-bs-target="#updateModal"
              @click="fillUpdateModal(localUso.codigoLocalUsoObra ,localUso.nomeLocalUsoObra)"
            ><img src="../assets/editar.png" alt="lata de lixo"></button>
            <button
              type="button"
              class="btn btn-light btn-sm small"
              title="Excluir"
              @click="removeFromDB(localUso.codigoLocalUsoObra)"
            ><img src="../assets/lata-de-lixo.png" alt="lata de lixo"></button>
          </td>
        </tr>
      </tbody>
    </table>
  </main>
</template>

<style scope>

.header {
  display: flex;
  justify-content: space-between;
  padding-bottom: 5px;
  border-bottom: solid #212529 2px;
}

.light-green {
  /* background-color: #006400; */
  /* background-color: #003300; */
  background-color: #3D8B37;
}

.dark-grey {
  background-color: #333333;
}

.editar-excluir {
  display: flex;
  justify-content: flex-end;
}

.small img {
  height: 15px;
  width: 15px;
}

.middle-margin {
  margin: 10px 30px 10px 30px;
}

.bold {
  font-weight: bold;
}
</style>