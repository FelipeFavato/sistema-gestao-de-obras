<script>
import { generateCorrectData } from '../utils/dataFormatada.js'

export default {
  data () {
    return {
      info: [],
      name: '',
      category: '',
      telephone: '',
      address: ''
    };
  },

  methods: {
    cancel() {
      this.name = '';
      this.category = '';
      this.telephone = '';
      this.address = '';
    },
    addInfo() {
      this.info.push({
        name: this.name,
        category: this.category,
        telephone: this.telephone,
        address: this.address,
        registrationDate: generateCorrectData()});
      this.cancel();
    },
    remove(nameToRemove) {
      const indexToRemove = this.info.findIndex(supplier => supplier.name === nameToRemove)
      this.info.splice(indexToRemove, 1);
    },
    edit() {

    },
  },

  // onMounted() {
  //   console.log('Pagina montada - Montar tabela')
  // }
}


</script>

<template>

  <!-- Header com o botão de +Novo -->
  <header class="header middle-margin">
    <button
      type="button"
      class="btn btn-success light-green"
      data-bs-toggle="modal"
      data-bs-target="#exampleModal"
    >+ Novo Fornecedor</button>
  </header>

  <!-- InsertModal -->
  <div class="modal" id="exampleModal" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-dialog-scrollable">
      <div class="modal-content">
        <div class="modal-header">
          <h1 class="modal-title fs-5" id="exampleModalLabel">Novo Fornecedor</h1>
          <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
        </div>
        <div class="modal-body">

          <form action="POST">

            <div class="mb-3">
              <label for="name-input" class="form-label bold">Nome:</label>
              <input
                type="text"
                class="form-control"
                id="name-input"
                placeholder="Digite aqui"
                v-model="name">
            </div>

            <div class="mb-3">
              <label for="telephone-input" class="form-label bold">Telefone:</label>
              <input
                type="text"
                class="form-control"
                id="telephone-input"
                placeholder="(xx) xxxx-xxxx"
                v-model="telephone"
                >
            </div>

            <div class="mb-3">
              <label for="address-input" class="form-label bold">Endereço:</label>
              <input
                type="text"
                class="form-control"
                id="address-input"
                placeholder="Avenida Exemplo, 000"
                v-model="address">
            </div>

            <div class="mb-3">
              <label for="category-select" class="bold">Categoria:</label>
              <select
                class="form-select"
                id="category-select"
                v-model="category">
                <option value="Material">Material</option>
                <option value="S">Serviço</option>
                <option value="M/S">Material/Serviço</option>
              </select>
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
            @click="addInfo"
          >Salvar</button>
        </div>
      </div>
    </div>
  </div>

  <!-- Tabela -->
  <main class="middle-margin table-responsive">
    <table class="table table-hover">
      <thead>
        <tr>
          <th scope="col">Código</th>
          <th scope="col">Nome</th>
          <th scope="col">Categoria</th>
          <th scope="col">Telefone</th>
          <th scope="col">Endereço</th>
          <th scope="col">Data Cadastro</th>
          <th></th>
          <th></th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="(supplier, index) in info" :key="index">
          <th scope="row">{{ supplier.id }}</th>
          <td>{{ supplier.name }}</td>
          <td>{{ supplier.category }}</td>
          <td>{{ supplier.telephone }}</td>
          <td>{{ supplier.address }}</td>
          <td>{{ supplier.registrationDate }}</td>
          <td></td>
          <td class="editar-excluir">
            <button
              type="button"
              class="btn btn-light btn-sm small"
              title="Editar"
              @click="edit"
            ><img src="../assets/imagens/editar.png" alt="lata de lixo"></button>
            <button
              type="button"
              class="btn btn-light btn-sm small"
              title="Excluir"
              @click="remove(supplier.name)"
            ><img src="../assets/imagens/lata-de-lixo.png" alt="lata de lixo"></button>
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
  /* border-bottom: solid #212529 2px; */
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
  margin: 10px 15px 10px 15px;
}

.bold {
  font-weight: bold;
}
</style>