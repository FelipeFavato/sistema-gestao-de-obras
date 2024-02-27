<script>
import axios from 'axios';
import { useRouter } from 'vue-router';
import { insertSuccessToast, updateSuccessToast, deleteSuccessToast,
  deleteErrorToast, insertErrorToast, updateErrorToast } from '../utils/toasts/index';
import SkeletonTableAndHeader from '../components/skeletonLoading/SkeletonTableAndHeader.vue';

export default {
  data () {
    return {
      // Variáveis de requisição/auxiliares: -----\
      useRouter: useRouter(),
      localStorageToken: null,
      httpStatus: '',
      /////////////////////////////////////////////
      // Variáveis de autenticação/autorização: --\
      info: [],
      codigo: '',
      unidade: '',
      customToastNotification: 'Unidade de medida'
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
      // this.clearLocalStorage();
    },
    // Método para excluir o token do localStorage.
    clearLocalStorage () {
      localStorage.clear();
    },
    getLocalStorageToken () {
      this.localStorageToken = localStorage.getItem('token');
    },
    setHttpStatusCode (succesError) {
      this.httpStatus = succesError;
    },
    validateHttpStatus (status) {
      this.setHttpStatusCode(status);
      this.httpStatus === 403 ? this.redirectToLogin(): null;
    },
    validateLogin () {
      !this.localStorageToken ? this.redirectToLogin() : null;
    },
    ///////////////////////////////////////////////////////////////////////////////

    // Métodos para apagar os dados que foram preenchidos e enviados ou não: -----\
    cancel () {
      this.codigo = '';
      this.unidade = '';
    },
    ///////////////////////////////////////////////////////////////////////////////

    // Métodos de busca - GET: ---------------------------------------------------\
    fetchInfoDB () {
      axios.get('/api/unidademedida', 
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
    ///////////////////////////////////////////////////////////////////////////////

    // Métodos de INSERT - POST: -------------------------------------------------\
    createInfoDB () {
      axios.post('/api/unidademedida',
      {
        unidade: this.unidade
      },
      {
        headers: {
          Authorization: this.localStorageToken
        }
      }).then((res) => {
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
    fillUpdateDeleteModal (cod, unid) {
      this.codigo = cod;
      this.unidade = unid;
    },
    updateInfoDB () {
      axios.put('/api/unidademedida',
      {
        codigo: Number(this.codigo),
        unidade: this.unidade
      },
      {
        headers: {
          Authorization: this.localStorageToken
        }
      }).then((res) => {
        this.fetchInfoDB();
        this.setHttpStatusCode(res.status); // 200
        updateSuccessToast(this.customToastNotification);
      }).catch(error => {
        this.validateHttpStatus(error.response.status);
        updateErrorToast(this.customToastNotification);
      });
      this.cancel();
    },
    // Métodos de DELETE - DELETE: -----------------------------------------------\
    removeFromDB (codigo) {
      axios.delete('/api/unidademedida',
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
        deleteSuccessToast(this.customToastNotification);
      }).catch(error => {
        this.validateHttpStatus(error.response.status);
        deleteErrorToast('ITENS DE COMPRA')
      });
      this.cancel();
    },
    ///////////////////////////////////////////////////////////////////////////////

    // Métodos de refinamento: ---------------------------------------------------\
    HGPKEnter (event) {
      const e = event;
      const ENTER = e.key === 'Enter';

      let body = document.getElementsByTagName('body');
      let novaUnidadeButton = document.getElementById('nova-unidade-button');
      let salvaUnidadeButton = document.getElementById('salva-unidade-button');
      let atualizaUnidadeButton = document.getElementById('atualiza-unidade-button');
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
        novaUnidadeButton.click();
      } else if (!noModalOpen && isInsertModal && ENTER) {
        e.preventDefault();
        salvaUnidadeButton.click();
      } else if (!noModalOpen && isUpdateModal && ENTER) {
        e.preventDefault();
        atualizaUnidadeButton.click();
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
    }
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

  <!-- Header com o botão de + Novo -->
  <header class="header middle-margin">
    <button
      id="nova-unidade-button"
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
            id="salva-unidade-button"
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

          <button
            id="atualiza-unidade-button"
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