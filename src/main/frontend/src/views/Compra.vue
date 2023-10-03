<script>
import axios from 'axios';

export default {
  data () {
    return {
      info: []
    }
  },

  methods: {
    fetchComprasInfoDB () {
      axios.get("/api/compra").then(
        (res) => this.info = res.data.sort((s1, s2) => s1.codigo - s2.codigo)
      )
    }
  },

  mounted () {
    this.fetchComprasInfoDB();
  }
}
</script>

<template>
  <!-- Tabela -->
  <main class="middle-margin table-responsive">
    <table class="table table-hover">
      <thead>
        <tr>
          <th scope="col">Código</th>
          <th scope="col">Obra</th>
          <th scope="col">Fornecedor</th>
          <th scope="col">Data da Compra</th>
          <th scope="col">Data da Entrega</th>
          <th scope="col">Valor Original</th>
          <th scope="col">Desconto</th>
          <th scope="col">Valor Final</th>
          <th></th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="(compra, i) in info" :key="i">
          <th scope="row">{{ compra.codigo }}</th>
          <td>{{ compra.obra.nome }}</td>
          <td>{{ compra.fornecedor.nome }}</td>
          <td>{{ compra.dataCompra }}</td>
          <td>{{ compra.dataEntrega }}</td>
          <td>{{ compra.valorOriginal }}</td>
          <td>{{ compra.valorDesconto }}</td>
          <td>{{ compra.valorFinal }}</td>
          <td class="editar-excluir">
            <button
              type="button"
              class="btn btn-light btn-sm small"
              title="Editar"
              data-bs-toggle="modal"
              data-bs-target="#updateModal"
              @click="fillUpdateDeleteModal(fornecedor.codigo, fornecedor.nome,
              fornecedor.tipoFornecedor, fornecedor.telefone, fornecedor.endereco)"
            ><img src="../assets/imagens/editar.png" alt="lata de lixo"></button>
            <button
              type="button"
              class="btn btn-light btn-sm small"
              title="Excluir"
              data-bs-toggle="modal"
              data-bs-target="#deleteModal"
              @click="fillUpdateDeleteModal(fornecedor.codigo)"
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