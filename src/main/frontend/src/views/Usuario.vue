<script>
import axios from 'axios';

export default {
  data () {
    return {
      info: [],
      codigo: '',
      nome: '',
      email: '',
      tipoPerfil: ''
    };
  },

  methods: {
    cancel () {
      this.codigo = '';
      this.nome = '';
      this.email = '';
      this.tipoPerfil = '';
    },
    fetchInfoDB () {
      axios.get("/api/usuario").then(
        (res) => this.info = res.data)
    },
    createInfoDB () {
      axios.post("/api/usuario",
      {
        nome: this.nome,
        email: this.email,
        tipoPerfil: this.tipoPerfil
      }).then(() => this.fetchInfoDB());
      this.cancel();
    },
    fillUpdateDeleteModal(cod, nome, email, tipoPerfil) {
      this.codigo = cod;
      this.nome = nome;
      this.email = email;
      this.tipoPerfil = tipoPerfil;
    },
    updateInfoDB (codigo, nome, email, tipo) {
      axios.put("/api/usuario",
        {
          codigo: Number(codigo),
          nome: nome,
          email: email,
          tipoPerfil: tipo
        }).then(() => this.fetchInfoDB());
      this.cancel();
    },
    removeFromDB (codigo) {
      axios.delete("/api/usuario",
        {
          headers: {
            Authorization: ''
          },
          data: {
            codigo: Number(codigo)
          }
        }).then(() => this.fetchInfoDB())
      this.cancel();
    },
  },

  mounted () {
    this.fetchInfoDB();
  }
}


</script>

<template>

  <!-- Header com o botão de +Novo Usuário -->
  <header class="header middle-margin">
    <button
      type="button"
      class="btn btn-success light-green"
      data-bs-toggle="modal"
      data-bs-target="#insertModal"
    >+ Novo Usuário</button>
  </header>

  <!-- DeleteModal -->
  <div class="modal fade" id="deleteModal" data-bs-backdrop="static" data-bs-keyboard="false" tabindex="-1" aria-labelledby="deleteModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-dialog-scrollable modal-sm">
      <div class="modal-content">
        <div class="modal-header">
          <h1 class="modal-title fs-5" id="deleteModalLabel">Realmente deseja excluir?</h1>
          <button @click="cancel" type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
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
  <div class="modal fade" id="insertModal" data-bs-backdrop="static" data-bs-keyboard="false" tabindex="-1" aria-labelledby="insertModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-dialog-scrollable">
      <div class="modal-content">
        <div class="modal-header">
          <h1 class="modal-title fs-5" id="insertModalLabel">Novo Usuário</h1>
          <button @click="cancel" type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
        </div>

        <div class="modal-body">

          <form action="POST">

            <!-- Nome -->
            <div class="mb-3">
              <label for="name-input" class="form-label bold">Nome:</label>
              <input
                type="text"
                class="form-control"
                id="name-input"
                placeholder="Nome do usuário"
                v-model="nome">
            </div>

            <!-- Perfil -->
            <div class="mb-3">
              <label for="tipo-perfil-select" class="bold">Perfil:</label>
              <select
                class="form-select"
                id="tipo-perfil-select"
                v-model="tipoPerfil">
                <option value="Gestor">Gestor</option>
                <option value="Operacional">Operacional</option>
              </select>
            </div>

            <!-- Email -->
            <div class="mb-3">
              <label for="email-input" class="form-label bold">Email:</label>
              <input
                type="text"
                class="form-control"
                id="email-input"
                placeholder="perfil@gmail.com"
                v-model="email">
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
  <div class="modal fade" id="updateModal" data-bs-backdrop="static" data-bs-keyboard="false" tabindex="-1" aria-labelledby="updateModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-dialog-scrollable">
      <div class="modal-content">
        <div class="modal-header">
          <h1 class="modal-title fs-5" id="updateModalLabel">Editar Usuário</h1>
          <button @click="cancel" type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
        </div>

        <div class="modal-body">
          <form action="PUT">

            <!-- Código -->
            <div class="mb-3">
              <label for="id-input" class="form-label bold">Código:</label>
              <input
                type="text"
                class="form-control"
                id="id-input"
                disabled
                v-model="codigo">
            </div>

            <!-- Nome -->
            <div class="mb-3">
              <label for="nome-input" class="form-label bold">Nome:</label>
              <input
                type="text"
                class="form-control"
                id="nome-input"
                placeholder="Nome do usuário"
                v-model="nome"
                disabled>
            </div>

            <!-- Perfil -->
            <div class="mb-3">
              <label for="tipo-perfil-select" class="bold">Perfil:</label>
              <select
                class="form-select"
                id="tipo-perfil-select"
                v-model="tipoPerfil">
                <option value="Gestor">Gestor</option>
                <option value="Operacional">Operacional</option>
              </select>
            </div>

            <!-- Email -->
            <div class="mb-3">
              <label for="email-input" class="form-label bold">Email:</label>
              <input
                type="text"
                class="form-control"
                id="email-input"
                placeholder="usuario@gmail.com"
                v-model="email">
            </div>
          </form>
        </div>

        <div class="modal-footer">
          <button type="button" class="btn btn-secondary dark-grey" data-bs-dismiss="modal"
            @click="cancel"
          >Fechar</button>

          <button type="button" class="btn btn-success  light-green" data-bs-dismiss="modal"
            @click="updateInfoDB(this.codigo, this.nome, this.email, this.tipoPerfil)"
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
          <th scope="col">Email</th>
          <th></th>
          <th></th>
          <th></th>
          <th></th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="(usuario, i) in info" :key="i">
          <th scope="row">{{ usuario.codigo }}</th>
          <td>{{ usuario.nome }}</td>
          <td>{{ usuario.tipoPerfil }}</td>
          <td>{{ usuario.email }}</td>
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
              @click="fillUpdateDeleteModal(usuario.codigo, usuario.nome, usuario.email, usuario.tipoPerfil)"
            ><img src="../assets/imagens/editar.png" alt="lata de lixo"></button>
            <button
              type="button"
              class="btn btn-light btn-sm small"
              title="Excluir"
              data-bs-toggle="modal"
              data-bs-target="#deleteModal"
              @click="fillUpdateDeleteModal(usuario.codigo)"
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