<script>
import axios from 'axios';

export default {
  data () {
    return {
      info: [],
      codigo: '',
      nome: '',
      tipoProduto: '',
    };
  },

  methods: {
    cancel() {
      this.nome = '';
      this.tipoProduto = '';
    },
    fetchInfoDB () {
      axios.get("/api/produto").then(
        (res) => this.info = res.data.sort((s1, s2) => s1['nome'].localeCompare(s2['nome'])))
    },
    createInfoDB () {
      axios.post("/api/produto",
        {
          nome: this.nome,
          tipoProduto: this.tipoProduto
        }).then(() => this.fetchInfoDB());
      this.cancel();
    },
    fillUpdateDeleteModal (codigo, nome, tipo) {
      this.codigo = codigo;
      this.nome = nome;
      this.tipoProduto = tipo;
    },
    updateInfoDB (codigo, nome, tipo) {
      axios.put("/api/produto",
        {
          codigo: Number(codigo),
          nome: nome,
          tipoProduto: tipo
        }).then(() => this.fetchInfoDB());
      this.cancel();
    },
    removeFromDB (codigo) {
      axios.delete("/api/produto",
        {
          headers: {
            Authorization: ''
          },
          data: {
            codigo: Number(codigo)
          }
        }).then(() => this.fetchInfoDB());
      this.cancel();
    },
    fixTaxasImpostos(tipo) {
      const Tipo = tipo
      if (Tipo === "TaxasImpostos") {
        return "Taxas/Impostos"
      }
      return Tipo
    }
  },

  mounted () {
    this.fetchInfoDB();
  }
}
</script>

<template>

    <!-- Header com o botão de +Novo -->
    <header class="header middle-margin">
      <button
        type="button"
        class="btn btn-success light-green"
        data-bs-toggle="modal"
        data-bs-target="#insertModal"
      >+ Novo Produto</button>
    </header>

  <!-- DeleteModal -->
  <div class="modal" id="deleteModal" tabindex="-1" aria-labelledby="deleteModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-dialog-scrollable modal-sm">
      <div class="modal-content">
        <div class="modal-header">
          <h1 class="modal-title fs-5" id="deleteModalLabel">Realmente deseja excluir?</h1>
          <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
        </div>

        <div class="modal-footer header">
          <button
            type="button"
            class="btn btn-secondary dark-grey"
            data-bs-dismiss="modal"
            @click="cancel"
          >Não</button>

          <button
            type="button"
            class="btn btn-success light-green"
            data-bs-dismiss="modal"
            @click="removeFromDB(this.codigo)"
          >Sim</button>
        </div>
      </div>
    </div>
  </div>

  <!-- InsertModal -->
  <div class="modal" id="insertModal" tabindex="-1" aria-labelledby="insertModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-dialog-scrollable">
      <div class="modal-content">
        <div class="modal-header">
          <h1 class="modal-title fs-5" id="insertModalLabel">Novo Produto</h1>
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
                placeholder="Produto, mão de obra, etc..."
                v-model="nome"
                disabled>
            </div>

            <div class="mb-3">
              <label for="tipoProduto-select" class="bold">Categoria:</label>
              <select
                class="form-select"
                id="tipoProduto-select"
                v-model="tipoProduto">
                <option value="Material">Material</option>
                <option value="Serviço">Serviço</option>
                <option value="TaxasImpostos">Taxas/Impostos</option>
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
          <h1 class="modal-title fs-5" id="updateModalLabel">Editar Produto</h1>
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
                disabled
                v-model="codigo">
            </div>

            <div class="mb-3">
              <label for="nome-input" class="form-label bold">Nome:</label>
              <input
                type="text"
                class="form-control"
                id="nome-input"
                placeholder="Produto, mão de obra, etc..."
                v-model="nome"
                disabled>
            </div>

            <div class="mb-3">
              <label for="tipoProduto-select" class="bold">Categoria:</label>
              <select
                class="form-select"
                id="tipoProduto-select"
                v-model="tipoProduto">
                <option value="Material">Material</option>
                <option value="Serviço">Serviço</option>
                <option value="TaxasImpostos">Taxas/Impostos</option>
              </select>
            </div>

          </form>

        </div>

        <div class="modal-footer">
          <button type="button" class="btn btn-secondary dark-grey" data-bs-dismiss="modal"
            @click="cancel"
          >Fechar</button>

          <button type="button" class="btn btn-success  light-green" data-bs-dismiss="modal"
            @click="updateInfoDB(this.codigo, this.nome, this.tipoProduto)"
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
          <th></th>
          <th></th>
          <th></th>
          <th></th>
          <th></th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="(produto, i) in info" :key="i">
          <th scope="row">{{ produto.codigo }}</th>
          <td>{{ produto.nome }}</td>
          <td>{{ fixTaxasImpostos(produto.tipoProduto) }}</td>
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
              @click="fillUpdateDeleteModal(produto.codigo, produto.nome,
              produto.tipoProduto)"
            ><img src="../assets/imagens/editar.png" alt="lata de lixo"></button>
            <button
              type="button"
              class="btn btn-light btn-sm small"
              title="Excluir"
              data-bs-toggle="modal"
              data-bs-target="#deleteModal"
              @click="fillUpdateDeleteModal(produto.codigo)"
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