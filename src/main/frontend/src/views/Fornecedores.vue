<script setup>
const info = [
  {
    id: 5,
    nome: 'Pedras CIA',
    dataCadastro: '04/09/2023'
  },
  {
    id: 8,
    nome: 'Tijolo CIA',
    dataCadastro: '04/09/2023'
  }
]

const addInfo = () => {

}
</script>

<template>
  <header class="header-fornecedores">

    <button
      type="button"
      class="btn btn-success"
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

  <main class="main-fornecedores">
    <table class="tabela-fornecedores">

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

      <tr>
        <td>1</td>
        <td>Exemplo</td>
        <td>04/09/2023</td>
        <td></td>
        <td></td>
        <td></td>
        <td></td>
        <td></td>
        <td></td>
        <td>
          <button type="button" class="btn btn-light">📝</button>
          <button type="button" class="btn btn-light">🗑️</button>
        </td>
      </tr>

      <tr>
        <td>2</td>
        <td>Exemplo 2</td>
        <td>04/09/2023</td>
        <td></td>
        <td></td>
        <td></td>
        <td></td>
        <td></td>
        <td></td>
        <td>
          <button type="button" class="btn btn-light">📝</button>
          <button type="button" class="btn btn-light">🗑️</button>
        </td>
      </tr>

      <!-- <tr v-for="(fornecedor, index) in info" :key="index">
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
          <button type="button" class="btn btn-light">📝</button>
          <button type="button" class="btn btn-light">🗑️</button>
        </td>
      </tr> -->

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

.tabela-fornecedores {
  display: flex;
  /* border: solid black 1px; */
  justify-content: space-between;
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

</style>