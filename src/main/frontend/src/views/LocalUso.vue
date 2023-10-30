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
      codigoLocalUsoObra: '',
      nomeLocalUsoObra: '',
      dataDesativacao: null,
    };
  },

  methods: {
    // Método para redirecionar para a página de login.
    redirectToLogin () {
      this.useRouter.push('/login');
    },
    // Método para recuperar o token do localStorage e preencher 'this.localStorageToken'.
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
    // Método para validar o login baseado no token.
    validateLogin () {
      !this.localStorageToken ? this.redirectToLogin() : null;
    },
    cancel() {
      this.codigoLocalUsoObra = '';
      this.nomeLocalUsoObra = '';
    },
    fetchInfoDB () {
      axios.get("/api/localuso",
      {
        headers: {
          Authorization: this.localStorageToken
        }
      }).then(res => {
        this.info = res.data.sort((s1, s2) => s1['nomeLocalUsoObra'].localeCompare(s2['nomeLocalUsoObra']))
        this.setHttpStatusCode(res.status);
      }).catch(error => {
        this.validateHttpStatus(error.response.status);
      })
    },
    createInfoDB () {
      axios.post("/api/localuso", 
      {
        nomeLocalUsoObra: this.nomeLocalUsoObra,
        dataDesativacao: null
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
    fillUpdateDeleteModal (codigo, nome, data) {
      this.codigoLocalUsoObra = codigo;
      this.nomeLocalUsoObra = nome;
      this.dataDesativacao = data;
    },
    updateInfoDB (codigo, nome, data) {
      axios.put("/api/localuso",
        {
          codigoLocalUsoObra: Number(codigo),
          nomeLocalUsoObra: nome,
          dataDesativacao: data
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
    removeFromDB (codigo) {
      axios.delete("/api/localuso",
        {
          headers: {
            Authorization: this.localStorageToken
          },
          data: {
            codigoLocalUsoObra: Number(codigo)
          }
        }).then((res) => {
          this.fetchInfoDB();
          this.setHttpStatusCode(res.status);
        }).catch(error => {
          this.validateHttpStatus(error.response.status);
        });
      this.cancel();
    },
    brazilDate (data) {
      if (data === null) {
        return null
      }

      let partes = data.split("-");

      return partes.length === 3 ? `${partes[2]}/${partes[1]}/${partes[0]}` : null;
    },
  },

  mounted () {
    this.getLocalStorageToken();
    this.validateLogin();
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
    >+ Novo Local de Uso</button>
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
            @click="removeFromDB(this.codigoLocalUsoObra)"
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
          <h1 class="modal-title fs-5" id="insertModalLabel">Novo Local de Uso</h1>
          <button @click="cancel" type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
        </div>

        <div class="modal-body">
          <form action="POST">

            <div class="mb-3">
              <label for="name-input" class="form-label bold">Local:</label>
              <input
                type="text"
                class="form-control"
                id="name-input"
                placeholder="Fundação, Hidráulica, etc..."
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
  <div class="modal fade" id="updateModal" data-bs-backdrop="static" data-bs-keyboard="false" tabindex="-1" aria-labelledby="updateModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-dialog-scrollable">
      <div class="modal-content">
        <div class="modal-header">
          <h1 class="modal-title fs-5" id="updateModalLabel">Editar Local de Uso</h1>
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
                v-model="codigoLocalUsoObra">
            </div>

            <div class="mb-3">
              <label for="category-input" class="form-label bold">Local:</label>
              <input
                type="text"
                class="form-control"
                id="category-input"
                placeholder="Alvenaria, Ferragens, etc..."
                v-model="nomeLocalUsoObra"
                disabled>
            </div>

            <div class="mb-3">
              <label for="dataDesativacao-input" class="form-label bold">Data desativação:</label>
              <input
                type="date"
                class="form-control"
                id="dataDesativacao-input"
                v-model="dataDesativacao">
            </div>

          </form>
        </div>

        <div class="modal-footer">
          <button type="button" class="btn btn-secondary dark-grey" data-bs-dismiss="modal"
            @click="cancel"
          >Fechar</button>

          <button type="button" class="btn btn-success  light-green" data-bs-dismiss="modal"
            @click="updateInfoDB(this.codigoLocalUsoObra, this.nomeLocalUsoObra, this.dataDesativacao)"
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
          <th scope="col">Local</th>
          <th scope="col">Data desativação</th>
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
          <td>{{ this.brazilDate(localUso.dataDesativacao) }}</td>
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
              @click="fillUpdateDeleteModal(localUso.codigoLocalUsoObra ,localUso.nomeLocalUsoObra, localUso.dataDesativacao)"
            ><img src="../assets/imagens/editar.png" alt="lata de lixo"></button>
            <button
              type="button"
              class="btn btn-light btn-sm small"
              title="Excluir"
              data-bs-toggle="modal"
              data-bs-target="#deleteModal"
              @click="fillUpdateDeleteModal(localUso.codigoLocalUsoObra ,localUso.nomeLocalUsoObra, localUso.dataDesativacao)"
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