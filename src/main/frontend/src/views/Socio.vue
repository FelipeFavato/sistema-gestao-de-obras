<script>
import { useRouter } from 'vue-router';
import axios from 'axios';
import { insertSuccessToast, updateSuccessToast, deleteSuccessToast,
  deleteErrorToast, insertErrorToast, updateErrorToast } from '../utils/toasts/index';
import { focusFirstModalInput } from '../utils/inputFocus';
import SkeletonTableAndHeader from '../components/skeletonLoading/SkeletonTableAndHeader.vue';

export default {
  data () {
    return {
      // Variáveis de autenticação/autorização: --\
      useRouter: useRouter(),
      localStorageToken: null,
      httpStatus: '',
      /////////////////////////////////////////////
      // Variáveis de requisição/auxiliares: -----\
      info: [],
      codigo: '',
      nome: '',
      dataDesativacao: '',
      telegramID: '',
      customToastNotification: 'Sócio(a)'
      /////////////////////////////////////////////
    }
  },

  props: {
    alturaMenu: Number,
  },

  components: {
    SkeletonTableAndHeader
  },

  methods: {
    // Validações de login: ------------------------------------------------------\
    redirectToLogin () {
      this.useRouter.push('/login');
    },
    getLocalStorageToken () {
      this.localStorageToken = localStorage.getItem('token');
    },
    setHttpStatusCode (succesError) {
      this.httpStatus = succesError;
    },
    validateLogin () {
      !this.localStorageToken ? this.redirectToLogin() : null;
    },
    validateHttpStatus (status) {
      this.setHttpStatusCode(status);
      this.httpStatus === 403 ? this.redirectToLogin(): null;
    },
    ///////////////////////////////////////////////////////////////////////////////

    // Métodos para apagar os dados que foram preenchidos e enviados ou não: -----\
    cancel() {
      this.nome = '';
      this.dataDesativacao = '';
      this.telegramID = '';
    },
    ///////////////////////////////////////////////////////////////////////////////

    // Métodos de busca - GET: ---------------------------------------------------\
    fetchInfoDB (callback) {
      axios.get('/api/socio',
      {
        headers: {
          Authorization: this.localStorageToken
        }
      }).then(res => {
        this.info = res.data.sort((s1, s2) => s1.codigo - s2.codigo);
        this.setHttpStatusCode(res.status);
        if (callback) callback();
      }).catch(error => {
        this.validateHttpStatus(error.response.status);
      });
    },
    ///////////////////////////////////////////////////////////////////////////////

    // Métodos de INSERT - POST: -------------------------------------------------\
    createInfoDB () {
      axios.post('/api/socio',
      {
        nome: this.nome,
        dataDesativacao: null,
        telegramID: this.telegramID
      },
      {
        headers: {
          Authorization: this.localStorageToken
        }
      }).then(res => {
        this.fetchInfoDB();
        this.setHttpStatusCode(res.status);
        insertSuccessToast(this.customToastNotification);
      }).catch(error => {
        this.validateHttpStatus(error.response.status);
        insertErrorToast(this.customToastNotification);
      });
      this.cancel();
    },
    ///////////////////////////////////////////////////////////////////////////////

    // Métodos de UPDATE - PUT: --------------------------------------------------\
    fillUpdateDeleteModal (codigo, nome, dataD, teleID) {
      focusFirstModalInput('update-data-desativacao-input');
      this.codigo = codigo;
      this.nome = nome;
      this.dataDesativacao = dataD;
      this.telegramID = teleID;
    },
    updateInfoDB () {
      axios.put('/api/socio',
      {
        codigo: Number(this.codigo),
        nome: this.nome,
        dataDesativacao: this.dataDesativacao,
        telegramID: this.telegramID
      },
      {
        headers: {
          Authorization: this.localStorageToken
        }
      }).then(res => {
        this.fetchInfoDB();
        this.setHttpStatusCode(res.status);
        updateSuccessToast(this.customToastNotification);
      }).catch(error => {
        this.validateHttpStatus(error.response.status);
        updateErrorToast(this.customToastNotification);
      });
      this.cancel();
    },
    ///////////////////////////////////////////////////////////////////////////////

    // Métodos de DELETE - DELETE: -----------------------------------------------\
    removeFromDB (codigo) {
      axios.delete('/api/socio',
        {
          headers: {
            Authorization: this.localStorageToken
          },
          data: {
            codigo: Number(codigo)
          }
        }).then(res => {
          this.fetchInfoDB();
          this.setHttpStatusCode(res.status);
          deleteSuccessToast(this.customToastNotification);
        }).catch(error => {
          this.validateHttpStatus(error.response.status);
          deleteErrorToast('OBRAS');
        });
      this.cancel();
    },
    ///////////////////////////////////////////////////////////////////////////////

    // Métodos de renderização: --------------------------------------------------\
    brazilDate (data) {
      if (data === null) {
        return null
      }

      let partes = data.split("-");

      return partes.length === 3 ? `${partes[2]}/${partes[1]}/${partes[0]}` : null;
    },
    ///////////////////////////////////////////////////////////////////////////////

    // Métodos de refinamento: ---------------------------------------------------\
    HGPKEnter (event) {
      const e = event;
      const ENTER = e.key === 'Enter';

      let body = document.getElementsByTagName('body');
      let novoSocioButton = document.getElementById('novo-socio-button');
      let salvaSocioButton = document.getElementById('salva-socio-button');
      let atualizaSocioButton = document.getElementById('atualiza-socio-button');
      let confirmaDeleteButton = document.getElementById('confirma-delete-button');

      // Modais para comparar se elas estão aparecendo ou não.
      let deleteModal = document.getElementById('deleteModal');
      let insertModal = document.getElementById('insertModal');
      let updateModal = document.getElementById('updateModal');

      const noModalOpen = body[0].classList.value === '';
      const isDeleteModal = deleteModal.classList.value === 'modal fade show';
      const isInsertModal = insertModal.classList.value === 'modal fade show';
      const isUpdateModal = updateModal.classList.value === 'modal fade show';

      if (noModalOpen && ENTER) {
        e.preventDefault();
        novoSocioButton.click();
      } else if (!noModalOpen && isInsertModal && ENTER) {
        e.preventDefault();
        salvaSocioButton.click();
      } else if (!noModalOpen && isUpdateModal && ENTER) {
        e.preventDefault();
        atualizaSocioButton.click();
      } else if (!noModalOpen && isDeleteModal && ENTER) {
        e.preventDefault();
        confirmaDeleteButton.click();
      }
    },
    addHGPKEnter () {
      window.addEventListener('keydown', this.HGPKEnter);
    },
    removeHGPKEnter () {
      window.removeEventListener('keydown', this.HGPKEnter);
    },
    focusFirstModalInput,
    ///////////////////////////////////////////////////////////////////////////////
  },

  mounted () {
    this.getLocalStorageToken();
    this.validateLogin();
    this.fetchInfoDB();
    this.addHGPKEnter();
  },

  unmounted () {
    this.removeHGPKEnter();
  }
}


</script>

<template>

  <!-- Tabela e Header Esqueleto - Carregamento -->
  <SkeletonTableAndHeader v-if="this.info == ''" />

  <!-- Header com o botão de +Novo -->
  <header v-if="this.info != ''" class="header middle-margin">
    <button
      id="novo-socio-button"
      type="button"
      class="btn btn-success light-green"
      data-bs-toggle="modal"
      data-bs-target="#insertModal"
      @click="focusFirstModalInput('insert-name-input')"
    >+ Novo Sócio</button>
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
            id="confirma-delete-button"
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
          <h1 class="modal-title fs-5" id="insertModalLabel">Novo Sócio</h1>
          <button @click="cancel" type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
        </div>

        <div class="modal-body">

          <form action="POST">

            <div class="mb-3">
              <label for="insert-name-input" class="form-label bold">Nome:</label>
              <input
                type="text"
                class="form-control"
                id="insert-name-input"
                placeholder="Sócio"
                v-model="nome"
                maxlength="100"
                >
            </div>

            <div class="mb-3">
              <label for="insert-telegram-ID-input" class="form-label bold">ID telegram:</label>
              <input
                type="number"
                step="1"
                title=''
                class="form-control"
                id="insert-telegram-ID-input"
                placeholder="ID telegram"
                v-model="telegramID"
                maxlength="30"
                >
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
            id="salva-socio-button"
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
          <h1 class="modal-title fs-5" id="updateModalLabel">Editar Sócio</h1>
          <button @click="cancel" type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
        </div>

        <div class="modal-body">
          <form action="PUT">

            <div class="mb-3">
              <label for="update-codigo-input" class="form-label bold">Código:</label>
              <input
                type="text"
                class="form-control"
                id="update-codigo-input"
                disabled
                v-model="codigo">
            </div>

            <div class="mb-3">
              <label for="update-name-input" class="form-label bold">Nome:</label>
              <input
                type="text"
                class="form-control"
                id="update-name-input"
                placeholder="Sócio"
                v-model="nome"
                disabled>
            </div>

            <div class="mb-3">
              <label for="update-data-desativacao-input" class="form-label bold">Data desativação:</label>
              <input
                type="date"
                class="form-control"
                id="update-data-desativacao-input"
                v-model="dataDesativacao">
            </div>

            <div class="mb-3">
              <label for="update-telegram-ID-input" class="form-label bold">ID telegram:</label>
              <input
                type="number"
                step="1"
                title=''
                class="form-control"
                id="update-telegram-ID-input"
                placeholder="ID telegram"
                v-model="telegramID"
                maxlength="30"
                >
            </div>

          </form>
        </div>

        <div class="modal-footer">
          <button type="button" class="btn btn-secondary dark-grey" data-bs-dismiss="modal"
            @click="cancel"
          >Fechar</button>

          <button
            id="atualiza-socio-button"
            type="button"
            class="btn btn-success light-green"
            data-bs-dismiss="modal"
            @click="updateInfoDB()"
          >Salvar</button>
        </div>
      </div>
    </div>
  </div>

  <!-- Tabela -->
  <main v-if="this.info != ''" class="middle-margin table-responsive">
    <table class="table table-hover">
      <thead>
        <tr>
          <th scope="col">Código</th>
          <th scope="col">Nome</th>
          <th scope="col">Data desativação</th>
          <th scope="col">ID telegram</th>
          <th></th>
          <th></th>
          <th></th>
          <th></th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="(socio, i) in info" :key="i">
          <th scope="row">{{ socio.codigo }}</th>
          <td>{{ socio.nome }}</td>
          <td>{{ this.brazilDate(socio.dataDesativacao) }}</td>
          <td>{{ socio.telegramID }}</td>
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
              @click="fillUpdateDeleteModal(socio.codigo, socio.nome, socio.dataDesativacao, socio.telegramID)"
            ><img src="../assets/imagens/editar.png" alt="lata de lixo"></button>
            <button
              type="button"
              class="btn btn-light btn-sm small"
              title="Excluir"
              data-bs-toggle="modal"
              data-bs-target="#deleteModal"
              @click="fillUpdateDeleteModal(socio.codigo)"
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