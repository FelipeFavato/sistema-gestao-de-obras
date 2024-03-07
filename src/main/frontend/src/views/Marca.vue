<script>
import axios from 'axios';
import { useRouter } from 'vue-router';
import { insertSuccessToast, updateSuccessToast, deleteSuccessToast,
  deleteErrorToast, insertErrorToast, updateErrorToast } from '../utils/toasts/index';
import { focusFirstModalInput } from '../utils/inputFocus/index';
import SkeletonTableAndHeader from '../components/skeletonLoading/SkeletonTableAndHeader.vue';
import { getLocalStorageToken } from '../utils/userLoginValidations';
import { checkInputValue, clickSavecheckRequiredInsertField,
  removeElementClass, setAttributeSalvarButton } from '../utils/inputValidations';

export default {
  data () {
    return {
      // Variáveis de autenticação/autorização: --\
      useRouter: useRouter(),
      localStorageToken: getLocalStorageToken(),
      httpStatus: '',
      /////////////////////////////////////////////
      // Variáveis de requisição/auxiliares: -----\
      info: [],
      codigo: '',
      nome: '',
      dataDesativacao: '',
      customToastNotification: 'Marca'
      /////////////////////////////////////////////
    }
  },

  watch: {
    nome () {
      this.watchRequiredInsertFields();
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
    // Método para redirecionar para a página de login.
    redirectToLogin () {
      this.useRouter.push('/login');
    },
    // Método para setar o 'this.httpStatus' com os casos de sucesso e erro.
    setHttpStatusCode (succesError) {
      this.httpStatus = succesError;
    },
    // Método para validar o login baseado no token.
    validateLogin () {
      !this.localStorageToken ? this.redirectToLogin() : null;
    },
    // Método para validar o StatusHttp da requisição. Casos de token expirado.
    validateHttpStatus (status) {
      this.setHttpStatusCode(status);
      this.httpStatus === 403 ? this.redirectToLogin(): null;
    },
    ///////////////////////////////////////////////////////////////////////////////

    // Métodos para apagar os dados que foram preenchidos e enviados ou não: -----\
    cancel () {
      this.nome = '';
      this.dataDesativacao = '';
    },
    cancelInsert () {
      this.cancel();

      removeElementClass('insert-name-input', 'required-red-border');
      removeElementClass('insert-name-label', 'campo-obrigatorio-warning');
      setAttributeSalvarButton('salva-marca-button', 'no-closing-modal');
    },
    ///////////////////////////////////////////////////////////////////////////////

    // Métodos de busca - GET: ---------------------------------------------------\
    fetchInfoDB () {
      axios.get('/api/marca',
      {
        headers: {
          Authorization: this.localStorageToken
        }
      }).then(res => {
        this.info = res.data.sort((s1, s2) => s1['nome'].localeCompare(s2['nome']));
        this.setHttpStatusCode(res.status);
      }).catch(error => {
        this.validateHttpStatus(error.response.status);
      });
    },
    ///////////////////////////////////////////////////////////////////////////////

    // Métodos de INSERT - POST: -------------------------------------------------\
    create () {
      axios.post('/api/marca',
      {
        nome: this.nome,
        dataDesativacao: null
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
        insertErrorToast(error.response.data.resposta);
      });
      this.cancelInsert();
    },
    watchRequiredInsertFields () {
      this.nome ?
        setAttributeSalvarButton('salva-marca-button', 'modal') :
        setAttributeSalvarButton('salva-marca-button', 'no-closing-modal');
    },
    createInfoDB() {
      clickSavecheckRequiredInsertField(this.nome, 'insert-name-input', 'insert-name-label', 'salva-marca-button');

      if (this.nome) {
        this.create();
      }
    },
    ///////////////////////////////////////////////////////////////////////////////

    // Métodos de UPDATE - PUT: --------------------------------------------------\
    fillUpdateDeleteModal (codigo, nome, dataD) {
      focusFirstModalInput('dataDesativacao-input')
      this.codigo = codigo;
      this.nome = nome;
      this.dataDesativacao = dataD;
    },
    updateInfoDB () {
      axios.put('/api/marca',
      {
       codigo: Number(this.codigo),
       nome: this.nome,
       dataDesativacao: this.dataDesativacao 
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
      axios.delete('/api/marca',
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
          deleteSuccessToast(res.data);
        }).catch(error => {
          this.validateHttpStatus(error.response.status);
          deleteErrorToast(error.response.data.resposta);
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
      let novaMarcaButton = document.getElementById('nova-marca-button');
      let salvaMarcaButton = document.getElementById('salva-marca-button');
      let atualizaMarcaButton = document.getElementById('atualiza-marca-button');
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
        novaMarcaButton.click();
      } else if (!noModalOpen && isInsertModal && ENTER) {
        e.preventDefault();
        salvaMarcaButton.click();
      } else if (!noModalOpen && isUpdateModal && ENTER) {
        e.preventDefault();
        atualizaMarcaButton.click();
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
    checkInputValue,
    ///////////////////////////////////////////////////////////////////////////////
  },

  mounted () {
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
      id="nova-marca-button"
      type="button"
      class="btn btn-success light-green"
      data-bs-toggle="modal"
      data-bs-target="#insertModal"
      @click="focusFirstModalInput('insert-name-input')"
    >+ Nova Marca</button>
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
          <h1 class="modal-title fs-5" id="insertModalLabel">Nova Marca</h1>
          <button @click="cancelInsert" type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
        </div>

        <div class="modal-body">

          <form action="POST">

            <div class="mb-3">
              <label id="insert-name-label" for="insert-name-input" class="form-label bold red-asterisk">Nome:</label>
              <input
                type="text"
                class="form-control"
                id="insert-name-input"
                placeholder="Amanco, Krona, CMC..."
                v-model="nome"
                maxlength="80"
                @keyup="checkInputValue(nome, 'insert-name-input')"
              >
            </div>

          </form>
        </div>

        <div class="modal-footer">
          <button
            type="button"
            class="btn btn-secondary dark-grey"
            data-bs-dismiss="modal"
            @click="cancelInsert"
          >Fechar</button>
          <!-- data-bs-dismiss="modal" -->
          <button
            id="salva-marca-button"
            type="button"
            class="btn btn-success  light-green"
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
          <h1 class="modal-title fs-5" id="updateModalLabel">Editar Marca</h1>
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
              <label for="nome-input" class="form-label bold">Nome:</label>
              <input
                type="text"
                class="form-control"
                id="nome-input"
                placeholder=""
                v-model="nome"
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

          <button
            id="atualiza-marca-button"
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
          <th></th>
          <th></th>
          <th></th>
          <th></th>
          <th></th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="(marca, i) in info" :key="i">
          <th scope="row">{{ marca.codigo }}</th>
          <td>{{ marca.nome }}</td>
          <td>{{ this.brazilDate(marca.dataDesativacao) }}</td>
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
              @click="fillUpdateDeleteModal(marca.codigo, marca.nome,
              marca.dataDesativacao)"
            ><img src="../assets/imagens/editar.png" alt="lata de lixo"></button>
            <button
              type="button"
              class="btn btn-light btn-sm small"
              title="Excluir"
              data-bs-toggle="modal"
              data-bs-target="#deleteModal"
              @click="fillUpdateDeleteModal(marca.codigo)"
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

@keyframes piscar {
  0%, 100% {
    border-color: #ff0000;
  }
  50% {
    border-color: #FF69B4;
  }
}

.required-red-border {
  border: 2px solid red;
  animation: piscar 2s infinite;
}

.red-asterisk::after {
  content: " *";
  color: red;
}

.campo-obrigatorio-warning::after {
  content: " * Campo obrigatório";
  color: red;
}
</style>