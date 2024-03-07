<script>

import axios from 'axios';
import { useRouter } from 'vue-router';
import { insertSuccessToast, updateSuccessToast, deleteSuccessToast,
  deleteErrorToast, insertErrorToast, updateErrorToast } from '../utils/toasts/index';
import { focusFirstModalInput } from '../utils/inputFocus';
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
      email: '',
      senha: '',
      role: '',
      customToastNotification: 'Usuário(a)'
      /////////////////////////////////////////////
    };
  },

  watch: {
    nome() {
      this.watchRequiredInsertFields();
    },
    email() {
      this.watchRequiredInsertFields();
      this.watchRequiredUpdateFields();
    },
    senha() {
      this.watchRequiredInsertFields();
    },
    role() {
      this.watchRequiredInsertFields();
    },
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
    // Método para limpar o localStorage.
    clearLocalStorage () {
      localStorage.clear();
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
    cancel () {
      this.codigo = '';
      this.nome = '';
      this.email = '';
      this.senha = '';
      this.role = '';
    },
    cancelInsert () {
      this.cancel();

      removeElementClass('insert-name-input', 'required-red-border');
      removeElementClass('insert-name-label', 'campo-obrigatorio-warning');
      removeElementClass('insert-email-input', 'required-red-border');
      removeElementClass('insert-email-label', 'campo-obrigatorio-warning');
      removeElementClass('insert-senha-input', 'required-red-border');
      removeElementClass('insert-senha-label', 'campo-obrigatorio-warning');
      removeElementClass('insert-tipo-perfil-select', 'required-red-border');
      removeElementClass('insert-tipo-perfil-label', 'campo-obrigatorio-warning');
      setAttributeSalvarButton('salvar-novo-button', 'no-closing-modal');
    },
    cancelUpdate () {
      this.cancel();

      removeElementClass('update-email-input', 'required-red-border');
      removeElementClass('update-email-label', 'campo-obrigatorio-warning');
      setAttributeSalvarButton('atualizar-button', 'no-closing-modal');
    },
    ///////////////////////////////////////////////////////////////////////////////

    // Métodos de busca - GET: ---------------------------------------------------\
    fetchInfoDB () {
      axios.get('/api/usuario',
      {
        headers: {
          Authorization: this.localStorageToken
        }
      }).then(res => {
        this.info = res.data;
        this.setHttpStatusCode(res.status)
      }).catch(error => {
        this.validateHttpStatus(error.response.status);
      });
    },
    ///////////////////////////////////////////////////////////////////////////////

    // Métodos de INSERT - POST: -------------------------------------------------\
    create () {
      axios.post('/api/auth/registrar',
        {
          nome: this.nome,
          email: this.email,
          senha: this.senha,
          role: this.role
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
          insertErrorToast("Usuário(a) já existe!");
        });
      this.cancelInsert();
    },
    watchRequiredInsertFields () {
      this.nome && this.email && this.senha && this.role ?
        setAttributeSalvarButton('salvar-novo-button', 'modal') :
        setAttributeSalvarButton('salvar-novo-button', 'no-closing-modal');
    },
    createInfoDB () {
      clickSavecheckRequiredInsertField(this.nome, 'insert-name-input', 'insert-name-label', 'salvar-novo-button');
      clickSavecheckRequiredInsertField(this.email, 'insert-email-input', 'insert-email-label', 'salvar-novo-button');
      clickSavecheckRequiredInsertField(this.senha, 'insert-senha-input', 'insert-senha-label', 'salvar-novo-button');
      clickSavecheckRequiredInsertField(this.role, 'insert-tipo-perfil-select', 'insert-tipo-perfil-label', 'salvar-novo-button');

      if (this.nome && this.email && this.senha && this.role) {
        this.create();
      }
    },
    ///////////////////////////////////////////////////////////////////////////////

    // Métodos de UPDATE - PUT: --------------------------------------------------\
    fillUpdateDeleteModal(cod, nome, email, senha, role) {
      focusFirstModalInput('update-email-input');
      this.codigo = cod;
      this.nome = nome;
      this.email = email;
      this.senha = senha;
      this.role = role;
    },
    update () {
      axios.put('/api/usuario',
        {
          codigo: Number(this.codigo),
          nome: this.nome,
          email: this.email,
          senha: this.senha,
          role: this.role
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
      this.cancelUpdate();
    },
    watchRequiredUpdateFields () {
      this.email ?
        setAttributeSalvarButton('atualizar-button', 'modal') :
        setAttributeSalvarButton('atualizar-button', 'no-closing-modal');
    },
    updateInfoDB () {
      clickSavecheckRequiredInsertField(this.email, 'update-email-input', 'update-email-label', 'atualizar-button');

      if (this.email) {
        this.update();
      }
    },
    ///////////////////////////////////////////////////////////////////////////////

    // Métodos de DELETE - DELETE: -----------------------------------------------\
    removeFromDB (codigo) {
      axios.delete('/api/usuario',
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
          deleteErrorToast('');
        });
      this.cancel();
    },
    ///////////////////////////////////////////////////////////////////////////////

    // Métodos de renderização: --------------------------------------------------\
    adminToGestor (role) {
      return role === "ADMIN" ? "Gestor" : "Operacional"
    },
    ///////////////////////////////////////////////////////////////////////////////

    // Métodos de refinamento: ---------------------------------------------------\
    HGPKEnter (event) {
      const e = event;
      const ENTER = e.key === 'Enter';

      // Recupera botões e elementos da página.
      let body = document.getElementsByTagName('body');
      let novoUsuarioButton = document.getElementById('novo-usuario-button');
      let salvarNovoButton = document.getElementById('salvar-novo-button');
      let atualizarButton = document.getElementById('atualizar-button');
      let confirmarDeleteButton = document.getElementById('confirmar-delete-button');

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
        novoUsuarioButton.click();
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

  <!-- Header com o botão de +Novo Usuário -->
  <header v-if="this.info != ''" class="header middle-margin">
    <button
      id="novo-usuario-button"
      type="button"
      class="btn btn-success light-green"
      data-bs-toggle="modal"
      data-bs-target="#insertModal"
      @click="focusFirstModalInput('insert-name-input')"
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
            id="confirmar-delete-button"
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
          <button @click="cancelInsert" type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
        </div>

        <div class="modal-body">

          <form action="POST">

            <!-- Nome -->
            <div class="mb-3">
              <label id="insert-name-label" for="insert-name-input" class="form-label bold red-asterisk">Nome:</label>
              <input
                type="text"
                class="form-control"
                id="insert-name-input"
                placeholder="Nome do usuário"
                v-model="nome"
                maxlength="100"
                @keyup="checkInputValue(nome, 'insert-name-input')"
              >
            </div>

            <!-- Email -->
            <div class="mb-3">
              <label id="insert-email-label" for="insert-email-input" class="form-label bold red-asterisk">Email:</label>
              <input
                type="text"
                class="form-control"
                id="insert-email-input"
                placeholder="usuario@gmail.com"
                v-model="email"
                maxlength="70"
                @keyup="checkInputValue(email, 'insert-email-input')"
              >
            </div>

            <!-- Senha -->
            <div class="mb-3">
              <label id="insert-senha-label" for="insert-senha-input" class="form-label bold red-asterisk">Senha:</label>
              <input
                type="password"
                class="form-control"
                id="insert-senha-input"
                placeholder="******"
                v-model="senha"
                maxlength="30"
                @keyup="checkInputValue(senha, 'insert-senha-input')"
              >
            </div>

            <!-- Perfil -->
            <div class="mb-3">
              <label id="insert-tipo-perfil-label" for="insert-tipo-perfil-select" class="bold red-asterisk">Perfil:</label>
              <select
                class="form-select"
                id="insert-tipo-perfil-select"
                v-model="role"
                @click="checkInputValue(role, 'insert-tipo-perfil-select')"
              >
                <option value="ADMIN">Gestor</option>
                <option value="USER">Operacional</option>
              </select>
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
          <button
            id="salvar-novo-button"
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
          <h1 class="modal-title fs-5" id="updateModalLabel">Editar Usuário</h1>
          <button @click="cancel" type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
        </div>

        <div class="modal-body">
          <form action="PUT">

            <!-- Código -->
            <div class="mb-3">
              <label for="update-codigo-input" class="form-label bold">Código:</label>
              <input
                type="text"
                class="form-control"
                id="update-codigo-input"
                disabled
                v-model="codigo">
            </div>

            <!-- Nome -->
            <div class="mb-3">
              <label for="update-name-input" class="form-label bold">Nome:</label>
              <input
                type="text"
                class="form-control"
                id="update-name-input"
                placeholder="Nome do usuário"
                v-model="nome"
                disabled>
            </div>

            <!-- Email -->
            <div class="mb-3">
              <label id="update-email-label" for="update-email-input" class="form-label red-asterisk bold">Email:</label>
              <input
                type="text"
                class="form-control"
                id="update-email-input"
                placeholder="usuario@gmail.com"
                v-model="email"
                maxlength="70"
                @keyup="checkInputValue(email, 'update-email-input')"
              >
            </div>

            <!-- Senha -->
            <div class="mb-3">
              <label for="update-password-input" class="form-label bold">Senha:</label>
              <input
                type="password"
                class="form-control"
                id="update-password-input"
                placeholder="******"
                v-model="senha"
                maxlength="30"
                disabled>
            </div>

            <!-- Perfil -->
            <div class="mb-3">
              <label for="update-tipo-perfil-select" class="bold">Perfil:</label>
              <select
                class="form-select"
                id="update-tipo-perfil-select"
                v-model="role">
                <option value="ADMIN">Gestor</option>
                <option value="USER">Operacional</option>
              </select>
            </div>

          </form>
        </div>

        <div class="modal-footer">
          <button type="button" class="btn btn-secondary dark-grey" data-bs-dismiss="modal"
            @click="cancel"
          >Fechar</button>

          <button
            id="atualizar-button"  
            type="button"
            class="btn btn-success light-green"
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
          <th scope="col">Email</th>
          <th scope="col">Perfil</th>
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
          <td>{{ usuario.email }}</td>
          <td>{{ adminToGestor(usuario.role) }}</td>
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
              @click="fillUpdateDeleteModal(usuario.codigo, usuario.nome, usuario.email, usuario.senha, usuario.role)"
            ><img src="../assets/imagens/editar.png" alt="editar"></button>
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
}

.light-green {
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