<script>
import axios from 'axios';
import { useRouter } from 'vue-router';

export default {
  data () {
    return {
      useRouter: useRouter(),
      localStorageToken: null,
      httpStatus: '',
      info: [],
      codigo: '',
      unidade: ''
    }
  },

  methods: {
    // Método para redirecionar para a página de Login.
    redirectToLogin () {
      this.useRouter.push('/login');
      this.clearLocalStorage();
    },
    // Método para excluir o token do localStorage.
    clearLocalStorage () {
      localStorage.clear();
    },
    // Método para recuperar o token do localStorage.
    getLocalStorageToken () {
      this.localStorageToken = localStorage.getItem('token');
    },
    // Método para setar o 'this.httpStatusCode' com os casos de sucesso e erro.
    setHttpStatusCode (succesError) {
      this.httpStatus = succesError;
    },
    // Método para validar o StatusHttp da requisição. Casos de token expirado.
    validateHttpStatus (status) {
      this.setHttpStatusCode(status);
      this.httpStatus === 403 ? this.redirectToLogin(): null;
    },
    validateLogin () {
      !this.localStorageToken ? this.redirectToLogin() : null;
    },
    // Método para esvaziar dados quando enviada/cancelada a requisição.
    cancel () {
      this.codigo = '';
      this.unidade = '';
    },
    // Método para buscar os dados no Banco.
    fetchInfoDB () {
      axios.get("/api/unidademedida", 
      {
        headers: {
          Authorization: this.localStorageToken
        }
      }).then(res => { 
        this.info = res.data.sort((s1, s2) => s1['unidade'].localeCompare(s2['unidade']))
      }).catch(error => {
        this.validateHttpStatus(error.response.status);
      });
    },
    // Método para criar dados no Banco.
    createInfoDB () {
      axios.post("/api/unidademedida",
      {
        unidade: this.unidade
      },
      {
        headers: {
          Authorization: `Bearer ${this.localStorageToken}`
        }
      }).then((res) => {
        this.fetchInfoDB();
        this.setHttpStatusCode(res.status);
      }).catch(error => {
        this.validateHttpStatus(error.response.status);
      });
      this.cancel();
    },
    // Método para preencher as informações da Modal.
    fillUpdateDeleteModal (codigo, unidade) {
      this.codigo = codigo;
      this.unidade = unidade;
    },
    // Método para atualizar um Produto no Banco.
    updateInfoDB (codigo, unidade) {
      axios.put("/api/unidademedida",
      {
        codigo: Number(codigo),
        unidade: unidade
      },
      {
        headers: {
          Authorization: `Bearer ${this.localStorageToken}`
        }
      }).then((res) => {
        this.fetchInfoDB();
        this.setHttpStatusCode(res.status); // 200
      }).catch(error => {
        this.validateHttpStatus(error.response.status);
      });
      this.cancel();
    },
    // Método para remover um dado do Banco.
    removeFromDB (codigo) {
      axios.delete("/api/unidademedida",
      {
        headers: {
          Authorization: this.localStorageToken
        },
        data: {
          codigo: Number(codigo)
        }
      }).then((res) => {
        this.fetchInfoDB();
        this.setHttpStatusCode(res.status);
      }).catch(error => {
        this.validateHttpStatus(error.response.status);
      });
      this.cancel();
    }
  },
  
  mounted () {
    this.getLocalStorageToken();
    this.validateLogin();
    this.fetchInfoDB();
  }
}
</script>

<template>

  <!-- Header com o botão de + Novo -->
  <header class="header middle-margin">
    <button
      type="button"
      class="btn btn-success light-green"
      data-bs-toggle="modal"
      data-bs-target="#insertModal"
    >+ Nova Unidade de medida
    </button>
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
          <h1 class="modal-title fs-5" id="insertModalLabel">Nova unidade de medida</h1>
          <button @click="cancel" type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
        </div>

        <div class="modal-body">
          <form action="POST">

            <div class="mb-3">
              <label for="unidade-input" class="form-label bold">Unidade de medida:</label>
              <input
                type="text"
                class="form-control"
                id="unidade-input"
                placeholder="Gramas, quilos, metros..."
                v-model="unidade"
                maxlength="30">
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
          <h1 class="modal-title fs-5" id="updateModalLabel">Editar unidade de medida</h1>
          <button @click="cancel" type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
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
              <label for="category-input" class="form-label bold">Unidade de medida:</label>
              <input
                type="text"
                class="form-control"
                id="category-input"
                placeholder="Alvenaria, Ferragens, etc..."
                v-model="unidade">
            </div>

          </form>
        </div>

        <div class="modal-footer">
          <button type="button" class="btn btn-secondary dark-grey" data-bs-dismiss="modal"
            @click="cancel"
          >Fechar</button>

          <button type="button" class="btn btn-success  light-green" data-bs-dismiss="modal"
            @click="updateInfoDB(this.codigo, this.unidade)"
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
          <th scope="col">Unidade de medida</th>
          <th></th>
          <th></th>
          <th></th>
          <th></th>
          <th></th>
          <th></th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="(unidadeMedida, i) in info" :key="i">
          <th scope="row">{{ unidadeMedida.codigo}}</th>
          <td>{{ unidadeMedida.unidade }}</td>
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
              @click="fillUpdateDeleteModal(unidadeMedida.codigo, unidadeMedida.unidade)"
            ><img src="../assets/imagens/editar.png" alt="lata de lixo"></button>
            <button
              type="button"
              class="btn btn-light btn-sm small"
              title="Excluir"
              data-bs-toggle="modal"
              data-bs-target="#deleteModal"
              @click="fillUpdateDeleteModal(unidadeMedida.codigo)"
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