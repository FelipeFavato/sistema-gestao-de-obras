<script>
import { generateCorrectData } from '../utils/dataFormatada.js'

export default {
  data () {
    return {
      info: [],
      openModal: false,
      name: '',
      category: '',
      telephone: '',
      address: ''
    };
  },

  methods: {
    showForm () {
      this.openModal = !this.openModal;
    },
    cancel() {
      this.name = '';
      this.category = '';
      this.telephone = '';
      this.address = '';
      this.showForm();
    },
    addInfo(e) {
      // e.preventDefault();
      this.info.push({
        name: this.name,
        category: this.category,
        telephone: this.telephone,
        address: this.address,
        registrationDate: generateCorrectData()});
      this.cancel();
    },
    removeSupplier(nameToRemove) {
      const indexToRemove = this.info.findIndex(supplier => supplier.name === nameToRemove)
      this.info.splice(indexToRemove, 1);
      // this.$forceUpdate();
    },
    editSupplier() {

    },
  },

  // onMounted() {
  //   console.log('Pagina montada - Montar tabela')
  // }
}


</script>

<template>
  <header class="header-fornecedores">

    <button
      type="button"
      class="btn btn-success"
      @click="showForm"
    >+ Novo Fornecedor</button>
    
    <div class="titulo-fornecedores">
      <h5>Fornecedores</h5>
    </div>

      <label for="filtro"></label>
      <select
        id="filtro"
        name="filtro"
        class="filtro-fornecedores"
      >
        <option value="id">Código</option>
        <option value="name">Nome</option>
        <option value="category">Categoria</option>
        <option value="recent">Recente</option>
      </select>
  </header>

  <div v-if="openModal" class="modal">
    <div>
      <form action="" class="formulario">
        <label for="nome">Nome:</label>
        <input type="text" id="nome" name="nome" v-model="name"><br><br>

        <label for="categoria">Categoria:</label>
        <input type="text" id="categoria" name="categoria" v-model="category"><br><br>

        <label for="telefone">Telefone:</label>
        <input type="text" id="telefone" name="telefone" v-model="telephone"><br><br>

        <label for="endereco">Endereço:</label>
        <input type="text" id="endereco" name="endereco" v-model="address"><br><br>
      
        <div class="botoes-formulario">
          <button
            type="submit"
            @click="addInfo"
            class="btn btn-success"
          >Enviar</button>
          <button
            type="submit"
            class="btn btn-secondary"
            @click="cancel"
          >Cancelar</button>
        </div>
      </form>
    </div>
  </div>

  <main class="main-fornecedores">
    <table class="tabela-fornecedores">
      <thead>
        <tr>
          <th>Código</th>
          <th>Nome</th>
          <th>Categoria</th>
          <th>Telefone</th>
          <th>Endereço</th>
          <th>Data Cadastro</th>
          <th></th>
          <th></th>
        </tr>
      </thead>
      
      <tbody>
        <tr v-for="(supplier, index) in info" :key="index">
          <td>{{ supplier.id }}</td>
          <td>{{ supplier.name }}</td>
          <td>{{ supplier.category }}</td>
          <td>{{ supplier.telephone }}</td>
          <td>{{ supplier.address }}</td>
          <td>{{ supplier.registrationDate }}</td>
          <td></td>
          <td>
            <button
              type="button"
              class="btn btn-light"
              title="Editar"
              @click="editSupplier"
            >📝</button>

            <button
              type="button"
              class="btn btn-light"
              title="Excluir"
              @click="removeSupplier(supplier.name)"
            >🗑️</button>
          </td>
        </tr>
      </tbody>
    </table>
  </main>
  
</template>

<style scope>

.header-fornecedores {
  display: flex;
  /* border: solid black 1px; */
  justify-content: space-between;
  margin: 10px 90px 10px 90px;
  padding-bottom: 5px;
  border-bottom: solid #363A57 5px;
}

.titulo-fornecedores {
  /* border: solid black 1px; */
  padding: 10px 5px 0px 5px;
  color: #363A57;
}

.main-fornecedores {
  /* border: solid black 1px; */
  margin: 10px 100px 10px 100px;
}

th, tr {
  padding: 5px 50px 5px 0px;
  border-bottom: 1px solid rgba(0, 0, 0, 0.2);
  width: 1000px;
}

.filtro-fornecedores {
  /* border: solid black 1px; */
  width: 80px;
  background-color: #FFA500;
  border: lightblue;
  border-radius: 5px;
}

/* .formulario {
  width: 400px;
  border: solid #363A57 5px;
  border-radius: 10px;
  padding: 10px;
  margin-left: 90px;
} */

/* .formulario input, label {
  margin: 10px 5px 5px 10px;
  padding-left: 0px;
} */

/* .botoes-formulario {
  display: flex;
  justify-content: space-around;
} */

.modal {
  position: absolute;
  top: 0;
  left: 0;
  background-color: rgba(0, 0, 0, 0.2);
  width: 100%;
  height: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
}

.modal > div {
  background-color: #fff;
  padding: 200px;
  border-radius: 10px;
}

</style>