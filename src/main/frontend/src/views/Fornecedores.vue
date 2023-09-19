<script>
import axios from 'axios';

export default {
  data () {
    return {
      info: [],
      nome: '',
      tipoFornecedor: '',
      telefone: '',
      endereco: ''
    };
  },

  methods: {
    cancel() {
      this.nome = '';
      this.telefone = '';
      this.endereco = '';
      this.tipoFornecedor = '';
    },
    fetchInfoDB () {
      axios.get("/api/fornecedor").then(
        (res) => this.info = res.data.sort((s1, s2) => s1.codigo - s2.codigo))
    },
    createInfoDB () {
      axios.post("api/fornecedor",
      {
        nome: this.nome,
        tipoFornecedor: this.tipoFornecedor,
        telefone: this.telefone,
        endereco: this.endereco
      }).then(() => this.fetchInfoDB());
      this.cancel();
    },
    fillUpdateDeleteModal (codigo, nome, tipoFornecedor, telefone, endereco) {
      this.codigo = codigo;
      this.nome = nome;
      this.tipoFornecedor = tipoFornecedor;
      this.telefone = telefone,
      this.endereco = endereco;
    },
    updateInfoDB (codigo, nome, tipo, telefone, endereco) {
      axios.put("/api/fornecedor",
        {
          codigo: Number(codigo),
          nome: nome,
          tipoFornecedor: tipo,
          telefone: telefone,
          endereco: endereco
        }).then(() => this.fetchInfoDB());
      this.cancel();
    },
    removeFromDB (codigo) {
      axios.delete("/api/fornecedor", {
        headers: {
          Authorization: ''
        },
        data: {
          codigo: Number(codigo)
        }
      }).then(() => this.fetchInfoDB())
      this.cancel();
    },
    fixTelNumber(numero) {
      // Remove todos os caracteres não numéricos do número de telefone
      const numeroLimpo = numero.replace(/\D/g, '');

      // Verifica se o número tem 11 dígitos
      if (numeroLimpo.length === 11) {
          // Formata o número de telefone
          return `(${numeroLimpo.slice(0, 2)}) ${numeroLimpo.slice(2, 7)}-${numeroLimpo.slice(7)}`;
      }
      if (numeroLimpo.length === 10) {
        return `(${numeroLimpo.slice(0, 2)}) ${numeroLimpo.slice(2, 6)}-${numeroLimpo.slice(6)}`
      }
    }
  },

  mounted () {
    this.fetchInfoDB()
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
    >+ Novo Fornecedor</button>
  </header>

  <!-- DeleteModal -->
  <div class="modal" id="deleteModal" tabindex="-1" aria-labelledby="deleteModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-dialog-scrollable modal-sm">
      <div class="modal-content">
        <div class="modal-header">
          <h1 class="modal-title fs-5" id="deleteModalLabel">Realmente deseja excluir?</h1>
          <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
        </div>

        <!-- <div class="modal-body">
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
                placeholder=""
                disabled
                v-model="nomeLocalUsoObra">
            </div>
        </div> -->

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
          <h1 class="modal-title fs-5" id="insertModalLabel">Novo Fornecedor</h1>
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
                placeholder="Nome do Fornecedor"
                v-model="nome">
            </div>

            <div class="mb-3">
              <label for="tipoFornecedor-select" class="bold">Categoria:</label>
              <select
                class="form-select"
                id="tipoFornecedor-select"
                v-model="tipoFornecedor">
                <option value="M">Material</option>
                <option value="S">Serviço</option>
                <option value="A">Ambos</option>
              </select>
            </div>

            <div class="mb-3">
              <label for="telefone-input" class="form-label bold">Telefone:</label>
              <input
                type="text"
                class="form-control"
                id="telefone-input"
                placeholder="Telefone (com DDD)"
                v-model="telefone">
            </div>

            <div class="mb-3">
              <label for="address-input" class="form-label bold">Endereço:</label>
              <input
                type="text"
                class="form-control"
                id="address-input"
                placeholder="Avenida dos Fornecedores, 1000"
                v-model="endereco">
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
          <h1 class="modal-title fs-5" id="updateModalLabel">Editar Fornecedor</h1>
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
                placeholder="Alvenaria, Ferragens, etc..."
                v-model="nome">
            </div>

            <div class="mb-3">
              <label for="tipoFornecedor-select" class="bold">Categoria:</label>
              <select
                class="form-select"
                id="tipoFornecedor-select"
                v-model="tipoFornecedor">
                <option value="M">Material</option>
                <option value="S">Serviço</option>
                <option value="A">Ambos</option>
              </select>
            </div>

            <div class="mb-3">
              <label for="telefone-input" class="form-label bold">Telefone:</label>
              <input
                type="text"
                class="form-control"
                id="telefone-input"
                placeholder="Telefone (com DDD)"
                v-model="telefone">
            </div>

            <div class="mb-3">
              <label for="endereco-input" class="form-label bold">Endereço:</label>
              <input
                type="text"
                class="form-control"
                id="endereco-input"
                placeholder="Avenida dos Fornecedores, 1000"
                v-model="endereco">
            </div>

          </form>
        </div>

        <div class="modal-footer">
          <button type="button" class="btn btn-secondary dark-grey" data-bs-dismiss="modal"
            @click="cancel"
          >Fechar</button>

          <button type="button" class="btn btn-success  light-green" data-bs-dismiss="modal"
            @click="updateInfoDB(this.codigo, this.nome, this.tipoFornecedor, this.telefone, this.endereco)"
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
          <th></th>
          <th></th>
          <th></th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="(fornecedor, i) in info" :key="i">
          <th scope="row">{{ fornecedor.codigo }}</th>
          <td>{{ fornecedor.nome }}</td>
          <td>{{ fornecedor.tipoFornecedor }}</td>
          <td>{{ this.fixTelNumber(fornecedor.telefone) }}</td>
          <td>{{ fornecedor.endereco }}</td>
          <td></td>
          <td></td>
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