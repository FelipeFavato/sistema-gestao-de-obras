<script>
import { generateCorrectData } from '../utils/dataFormatada.js'

export default {
  data () {
    return {
      info: [],
      formularioVisivel: false,
      nome: ''
    };
  },

  methods: {
    showForm () {
      this.formularioVisivel = !this.formularioVisivel;
    },
    cancel(e) {
      this.nome = '';
      this.showForm();
    },
    addInfo(e) {
      // e.preventDefault();
      this.info.push({nome: this.nome, dataCadastro: generateCorrectData()});
      this.cancel();
    },
    removeSupplier(nomeParaRemover) {
      const indiceParaRemover = this.info.findIndex(fornecedor => fornecedor.nome === nomeParaRemover)
      this.info.splice(indiceParaRemover, 1);
      // this.$forceUpdate();
    },
    editSupplier() {

    }
  },

  // mounted() {
  //   console.log('Pagina montada')
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
        <option value="id">ID</option>
        <option value="nome">Nome</option>
        <option value="recente">Recente</option>
      </select>
  </header>

  <form v-if="formularioVisivel" class="formulario">
    <label for="nome">Nome:</label>
    <input type="text" id="nome" name="nome" v-model="nome"><br><br>
  
    <div class="botoes-formulario">
      <button
        type="submit"
        @click="addInfo"
        class="btn btn-success"
      >Enviar</button>
      <button
        type="submit"
        class="btn btn-danger"
        @click="cancel"
      >Cancelar</button>
    </div>
  </form>

  <main class="main-fornecedores">
    <table class="tabela-fornecedores">
      <thead>
        <tr>
          <th>Id</th>
          <th>Nome</th>
        <th>Data Cadastro</th>
        <th></th>
        <th></th>
        <th></th>
        <th></th>
        <th></th>
        <th></th>
        <th></th>
        </tr>
      </thead>
      
      <tbody>
        <tr v-for="(fornecedor, index) in info" :key="index">
          <td>{{ fornecedor.id }}</td>
          <td>{{ fornecedor.nome }}</td>
          <td>{{ fornecedor.dataCadastro }}</td>
          <td></td>
          <td></td>
          <td></td>
          <td></td>
          <td></td>
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
              @click="removeSupplier(fornecedor.nome)"
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

.formulario {
  width: 400px;
  border: solid #363A57 5px;
  border-radius: 10px;
  padding: 10px;
  margin-left: 90px;
}

.formulario input, label {
  margin: 10px 5px 5px 10px;
  padding-left: 0px;
}

.botoes-formulario {
  display: flex;
  justify-content: space-around;
}

</style>