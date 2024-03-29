<script>
import axios from 'axios';
import { useRouter } from 'vue-router';
import { insertSuccessToast, updateSuccessToast, deleteSuccessToast,
  deleteErrorToast, insertErrorToast, updateErrorToast } from '../utils/toasts/index';
import { getLocalStorageToken } from '../utils/userLoginValidations';
import { focusFirstModalInput } from '../utils/inputFocus';
import SkeletonTableAndHeader from '../components/skeletonLoading/SkeletonTableAndHeader.vue';
import { clickSavecheckRequiredInsertField, checkInputValue,
  setAttributeSalvarButton, removeElementClass } from '../utils/inputValidations';


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
      codigoLocalUsoObra: '',
      nomeLocalUsoObra: '',
      dataDesativacao: null,
      /////////////////////////////////////////////
      // Variáveis de comportamento: -------------\
      startNewAdd: false,
      customToastNotification: 'Local de uso',
      /////////////////////////////////////////////
    };
  },

  props: {
    alturaMenu: Number,
  },

  components: {
    SkeletonTableAndHeader,
  },

  // O vue percebe que estou usando algumas variaveis, qualquer atualização nas variaveis, ele atualiza
  // uma variável X
  computed: {

  },

  // Pode definir um comportamento a ser chamado quando uma variável mudar.
  watch: {
    nomeLocalUsoObra() {
      this.watchRequiredInsertFields();
    }
  },

  methods: {
    // Validações de login: ------------------------------------------------------\
    redirectToLogin () {
      this.useRouter.push('/login');
    },
    setHttpStatusCode (succesError) {
      this.httpStatus = succesError;
    },
    validateHttpStatus (status) {
      this.setHttpStatusCode(status);
      this.httpStatus === 403 ? this.redirectToLogin(): 'Usuário autenticado.';
    },
    validateLogin () {
      !this.localStorageToken ? this.redirectToLogin() : 'Usuário autenticado.';
    },
    ///////////////////////////////////////////////////////////////////////////////

    // Métodos para apagar os dados que foram preenchidos e enviados ou não: -----\
    cancel() {
      this.codigoLocalUsoObra = '';
      this.nomeLocalUsoObra = '';
      this.dataDesativacao = '';
    },
    cancelInsert () {
      this.cancel();

      removeElementClass('insert-name-input', 'required-red-border');
      removeElementClass('insert-nome-label', 'campo-obrigatorio-warning');
      setAttributeSalvarButton('salvarNovoButton', 'no-closing-modal');
    },
    ///////////////////////////////////////////////////////////////////////////////

    // Métodos de busca - GET: ---------------------------------------------------\
    fetchInfoDB (callback) {
      axios.get('/api/localuso',
      {
        headers: {
          Authorization: this.localStorageToken
        }
      }).then(res => {    
        this.info = res.data.sort((s1, s2) => s1['nomeLocalUsoObra'].localeCompare(s2['nomeLocalUsoObra']))
        this.setHttpStatusCode(res.status);
        if (callback) callback();
      }).catch(error => {
        this.validateHttpStatus(error.response.status);
      })
    },
    ///////////////////////////////////////////////////////////////////////////////
    
    // Métodos de INSERT - POST: -------------------------------------------------\
    create () {
      axios.post('/api/localuso', 
        {
          nomeLocalUsoObra: this.nomeLocalUsoObra,
          dataDesativacao: null
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
          insertErrorToast(error.response.data.resposta);
        }
      );
      this.cancelInsert();
    },
    watchRequiredInsertFields () {
      this.nomeLocalUsoObra === '' ?
        setAttributeSalvarButton('salvarNovoButton', 'no-closing-modal') :
        setAttributeSalvarButton('salvarNovoButton', 'modal');
    },
    createInfoDB () {
      // Verifica a variável 'this.nomeLocalUsoObra' e aplica comportamentos nos campos dependendo da variável. 
      clickSavecheckRequiredInsertField(this.nomeLocalUsoObra, 'insert-name-input', 'insert-nome-label', 'salvarNovoButton');

      // Caso a variável tenha valor e esteja dentro dos parametros, é permitido o INSERT.
      if (this.nomeLocalUsoObra) {
        this.create();
      }

    },
    ///////////////////////////////////////////////////////////////////////////////

    // Métodos de UPDATE - PUT: --------------------------------------------------\
    fillUpdateDeleteModal (cod, nome, data) {
      focusFirstModalInput('update-data-desativacao-input');
      this.codigoLocalUsoObra = cod;
      this.nomeLocalUsoObra = nome;
      this.dataDesativacao = data;
    },
    updateInfoDB () {
      axios.put('/api/localuso',
        {
          codigoLocalUsoObra: Number(this.codigoLocalUsoObra),
          nomeLocalUsoObra: this.nomeLocalUsoObra,
          dataDesativacao: this.dataDesativacao
        },
        {
          headers: {
            Authorization: this.localStorageToken
          }
        }).then((res) => {
          this.fetchInfoDB();
          this.setHttpStatusCode(res.status);
          updateSuccessToast(this.customToastNotification);
        }).catch(error => {
          this.validateHttpStatus(error.response.status);
          updateErrorToast(this.customToastNotification);
        }
      );
      this.cancel();
    },
    ///////////////////////////////////////////////////////////////////////////////

    // Métodos de DELETE - DELETE: -----------------------------------------------\
    removeFromDB (codigo) {
      axios.delete('/api/localuso',
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
          deleteSuccessToast(res.data);
        }).catch(error => {
          deleteErrorToast(error.response.data.resposta);
          this.validateHttpStatus(error.response.status);
        });
      this.cancel();
    },
    ///////////////////////////////////////////////////////////////////////////////

    // Métodos de comportamento: -------------------------------------------------\
    startNewAddition () {
      this.startNewAdd = !this.startNewAdd;
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
    // HGPK = Handle Global Press Key
    HGPKEnter (event) {
      const e = event;
      const ENTER = e.key === 'Enter';

      // Recupera botões e elementos da página.
      let body = document.getElementsByTagName('body');
      let novoLocalButton = document.getElementById('novoLocalButton');
      let salvarNovoButton = document.getElementById('salvarNovoButton');
      let atualizarButton = document.getElementById('atualizarButton');
      let confirmarDeleteButton = document.getElementById('confirmarDeleteButton');

      // Modais para comparar se elas estão aparecendo ou não.
      let deleteModal = document.getElementById('deleteModal');
      let insertModal = document.getElementById('insertModal');
      let updateModal = document.getElementById('updateModal');

      const noModalOpen = body[0].classList.value === '';
      const isDeleteModal = deleteModal.classList.value === 'modal fade show';
      const isInsertModal = insertModal.classList.value === 'modal fade show';
      const isUpdateModal = updateModal.classList.value === 'modal fade show';

      // Ativa o comportamento desejado baseado no momento que o usuário está na página:
      if (noModalOpen && ENTER) {
        e.preventDefault();
        novoLocalButton.click();
      } else if (!noModalOpen && isInsertModal && ENTER) {
        e.preventDefault();
        salvarNovoButton.click();
      } else if (!noModalOpen && isUpdateModal && ENTER) {
        e.preventDefault();
        atualizarButton.click();
      } else if (!noModalOpen && isDeleteModal && ENTER) {
        e.preventDefault();
        confirmarDeleteButton.click();
      }
    },
    addHGPKEnter () {
      window.addEventListener('keydown', this.HGPKEnter);
    },
    removeHGPKEnter () {
      window.removeEventListener('keydown', this.HGPKEnter);
    },
    // Esse método vem de '../utils/inputFocus'.
    
    HGPKEsc () {
      window.addEventListener('keydown', (event) => {
        const e = event;
        const ESC = e.keyCode === 27;

        // Recupera botões e elementos da página.
        let body = document.getElementsByTagName('body');
        let fecharInsertButton = document.getElementById('fecharInsertButton');
        let fecharUpdateButton = document.getElementById('fecharUpdateButton');
        let naoDeleteButton = document.getElementById('naoDeleteButton');

        // Modais para comparar se elas estão aparecendo ou não;
        let deleteModal = document.getElementById('deleteModal');
        let insertModal = document.getElementById('insertModal');
        let updateModal = document.getElementById('updateModal');

        const noModalOpen = body[0].classList.value === '';
        const isDeleteModal = deleteModal.classList.value === 'modal fade show';
        const isInsertModal = insertModal.classList.value === 'modal fade show';
        const isUpdateModal = updateModal.classList.value === 'modal fade show';

        // Ações baseadas na situação:
        if (isInsertModal && ESC) {
          e.preventDefault();
          fecharInsertButton.click();
        } else if (!noModalOpen && isUpdateModal && ESC) {
          e.preventDefault();
          fecharUpdateButton.click();
        } else if (!noModalOpen && isDeleteModal && ESC) {
          e.preventDefault();
          naoDeleteButton.click();
        }
      });
    },
    focusFirstModalInput,
    checkInputValue,
    setAttributeSalvarButton,
    removeElementClass
    ///////////////////////////////////////////////////////////////////////////////

  },

  mounted () {
    this.validateLogin();
    this.fetchInfoDB();
    // Adiciona os escutadores de evento relacionados a tecla 'Enter'.
    this.addHGPKEnter();
  },

  unmounted () {
    // Remove os escutadores de evento relacionados a tecla 'Enter'.
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
      type="button"
      class="btn btn-success light-green"
      data-bs-toggle="modal"
      data-bs-target="#insertModal"
      id="novoLocalButton"
      @click="focusFirstModalInput('insert-name-input')"
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
            id="naoDeleteButton"
            type="button"
            class="btn btn-secondary dark-grey"
            data-bs-dismiss="modal"
            @click="cancel"
          >Não</button>

          <button
            id="confirmarDeleteButton"
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
          <button @click="cancelInsert" type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
        </div>

        <div class="modal-body">
          <form action="POST">

            <div class="mb-3">
              <label id="insert-nome-label" for="insert-name-input" class="form-label bold red-asterisk">Local:</label>
              <input
                type="text"
                class="form-control"
                id="insert-name-input"
                placeholder="Fundação, Hidráulica, etc..."
                v-model="nomeLocalUsoObra"
                maxlength="30"
                @keyup="checkInputValue(nomeLocalUsoObra, 'insert-name-input')">
            </div>

          </form>
        </div>

        <div class="modal-footer">
          <button
            id="fecharInsertButton"
            type="button"
            class="btn btn-secondary dark-grey"
            data-bs-dismiss="modal"
            @click="cancelInsert"
          >Fechar</button>
          <!-- :data-bs-dismiss="modal" -->
          <button
            id="salvarNovoButton"
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
          <h1 class="modal-title fs-5" id="updateModalLabel">Editar Local de Uso</h1>
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
                v-model="codigoLocalUsoObra">
            </div>

            <div class="mb-3">
              <label for="update-name-input" class="form-label bold">Local:</label>
              <input
                type="text"
                class="form-control"
                id="update-name-input"
                placeholder="Alvenaria, Ferragens, etc..."
                v-model="nomeLocalUsoObra"
                disabled
                maxlength="30">
            </div>

            <div class="mb-3">
              <label for="update-data-desativacao-input" class="form-label bold">Data desativação:</label>
              <input
                type="date"
                class="form-control"
                id="update-data-desativacao-input"
                v-model="dataDesativacao">
            </div>

          </form>
        </div>

        <div class="modal-footer">
          <button id="fecharUpdateButton" type="button" class="btn btn-secondary dark-grey" data-bs-dismiss="modal"
            @click="cancel"
          >Fechar</button>

          <button id="atualizarButton" type="button" class="btn btn-success  light-green" data-bs-dismiss="modal"
            @click="updateInfoDB()"
          >Salvar</button>
        </div>
      </div>
    </div>
  </div>

  <!-- Tabela -->
  <main v-if="this.info != ''" class="middle-margin table-responsive">
    <table class="table table-hover">

      <thead class="tamanho-fonte-18">
        <tr>
          <th scope="col">Código</th>
          <th scope="col">Local</th>
          <th scope="col">Data desativação</th>
          <th></th>
          <th></th>
          <th></th>
          <th></th>
          <th></th>
          <!-- <th class="flex-end">
            <button
              type="button"
              class="btn btn-success light-green"
              id="novoLocalButton"
              data-bs-target="#insertModal"
              data-bs-toggle="modal"
            >+ Novo Local
            </button>
          </th> -->
        </tr>
      </thead>

      <tbody>
        <!-- Primeira tentativa -->
        <!-- <tr v-if="this.startNewAdd" class="red-border">
          <th scope="row">
          </th>
          <td class="red-border">
            <div class="input-group input-group-sm mb-3" style="height: 50%;">
              <input
                id="nameInput"
                type="text"
                class="form-control"
                aria-label="Sizing example input"
                aria-describedby="inputGroup-sizing-sm"
                placeholder="Fundação, Hidráulica, etc..."
                v-model="nomeLocalUsoObra"
                maxlength="30"
              >
            </div>
          </td>
          <td style="height: 100%;">
            <div class="input-group input-group-sm mb-3" style="height: 100%;">
              <input
                type="text"
                aria-label="Sizing example input"
                aria-describedby="inputGroup-sizing-sm"
                disabled
                placeholder="Ativo"
              >
            </div>
          </td>
          <td></td>
          <td></td>
          <td></td>
          <td></td>
          <td class="flex-end red-border">
            <button
              type="button"
              class="btn btn-light btn-sm medium medium"
              title="Cancelar"
              @click="cancel"
            ><img src="../assets/imagens/close-window-icon.png" alt="close-button" />
            </button>
            <button
              type="button"
              class="btn btn-light btn-sm medium"
              title="Salvar"
              @click="createInfoDB"
            ><img src="../assets/imagens/check.webp" alt="salvar-button"/>
            </button>
          </td>
        </tr> -->

        <tr v-for="(localUso, i) in info" :key="i">
          <th scope="row">{{ localUso.codigoLocalUsoObra }}</th>
          <td>{{ localUso.nomeLocalUsoObra }}</td>
          <td>{{ this.brazilDate(localUso.dataDesativacao) }}</td>
          <td></td>
          <td></td>
          <td></td>
          <td></td>
          <td class="flex-end">
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
}

.light-green {
  background-color: #3D8B37;
}

.dark-grey {
  background-color: #333333;
}

.red-border {
  border: 1px solid red;
}

.tamanho-fonte-18 {
  font-size: 18px;
}

.flex-end {
  display: flex;
  justify-content: flex-end;
}

.small img {
  height: 16px;
  width: 16px;
}

.medium img {
  height: 20px;
  width: 20px;
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