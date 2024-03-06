<script>
import { useRouter } from 'vue-router';
import axios from 'axios';
import { insertSuccessToast, updateSuccessToast, deleteSuccessToast,
  deleteErrorToast, insertErrorToast, updateErrorToast } from '../utils/toasts/index';
import { focusFirstModalInput } from '../utils/inputFocus';
import { getLocalStorageToken } from '../utils/userLoginValidations';
import SkeletonTableAndHeader from '../components/skeletonLoading/SkeletonTableAndHeader.vue';
import { checkInputValue, clickSavecheckRequiredInsertField, addElementClass,
  removeElementClass, setAttributeSalvarButton } from '../utils/inputValidations';

// Campos obrigatorios: 'nome' e 'tipoFornecedor'.

export default {
  data () {
    return {
      // Variáveis de autenticação/autorização: --\
      useRouter: useRouter(),
      localStorageToken: getLocalStorageToken(),
      httpStatus: '',
      /////////////////////////////////////////////
      // Variáveis auxiliares: -------------------\
      info: [],
      customToastNotification: 'Fornecedor',
      /////////////////////////////////////////////
      // Variáveis de requisição: ----------------\
      codigo: '',
      nome: '',
      tipoFornecedor: '',
      telefone: '',
      endereco: '',
      email: ''
      /////////////////////////////////////////////
    };
  },

  watch: {
    nome () {
      this.watchRequiredInsertFields();
    },
    tipoFornecedor () {
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
      this.telefone = '';
      this.endereco = '';
      this.email = '';
      this.tipoFornecedor = '';
    },
    cancelInsert () {
      this.nome = '';
      this.telefone = '';
      this.endereco = '';
      this.email = '';
      this.tipoFornecedor = '';

      removeElementClass('insert-name-input', 'required-red-border');
      removeElementClass('insert-name-label', 'campo-obrigatorio-warning');
      removeElementClass('insert-tipo-fornecedor-select', 'required-red-border');
      removeElementClass('insert-tipo-fornecedor-label', 'campo-obrigatorio-warning');
      setAttributeSalvarButton('salvar-novo-button', 'no-closing-modal');
    },
    ///////////////////////////////////////////////////////////////////////////////

    // Métodos de busca - GET: ---------------------------------------------------\
    fetchInfoDB () {
      axios.get('/api/fornecedor',
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
      axios.post('/api/fornecedor',
        {
          nome: this.nome,
          tipoFornecedor: this.tipoFornecedor,
          telefone: this.telefone,
          endereco: this.endereco,
          email: this.email
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
        });
      this.cancelInsert();
    },
    watchRequiredInsertFields () {
      if (this.nome && this.tipoFornecedor) {
        setAttributeSalvarButton('salvar-novo-button', 'modal');
      } else {
        setAttributeSalvarButton('salvar-novo-button', 'no-closing-modal');
      }
    },
    createInfoDB () {
      clickSavecheckRequiredInsertField(this.nome, 'insert-name-input', 'insert-name-label', 'salvar-novo-button');
      clickSavecheckRequiredInsertField(this.tipoFornecedor, 'insert-tipo-fornecedor-select', 'insert-tipo-fornecedor-label', 'salvar-novo-button');

      if (this.nome && this.tipoFornecedor) {
        this.create();
      }
    },
    ///////////////////////////////////////////////////////////////////////////////

    // Métodos de UPDATE - PUT: --------------------------------------------------\
    fillUpdateDeleteModal (cod, nome, tipoF, tel, endereco, email) {
      focusFirstModalInput('update-tipo-fornecedor-select');
      this.codigo = cod;
      this.nome = nome;
      this.tipoFornecedor = tipoF;
      this.telefone = tel,
      this.endereco = endereco;
      this.email = email;
    },
    updateInfoDB () {
      axios.put('/api/fornecedor',
        {
          codigo: Number(this.codigo),
          nome: this.nome,
          tipoFornecedor: this.tipoFornecedor,
          telefone: this.telefone,
          endereco: this.endereco,
          email: this.email
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
        });
      this.cancel();
    },
    ///////////////////////////////////////////////////////////////////////////////

    // Métodos de DELETE - DELETE: -----------------------------------------------\
    removeFromDB (codigo) {
      axios.delete('/api/fornecedor',
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
          deleteSuccessToast(res.data);
        }).catch(error => {
          this.validateHttpStatus(error.response.status);
          deleteErrorToast(error.response.data.resposta);
        });
      this.cancel();
    },
    ///////////////////////////////////////////////////////////////////////////////

    // Métodos de comportamento: -------------------------------------------------\
    ///////////////////////////////////////////////////////////////////////////////

    // Métodos de renderização: --------------------------------------------------\
    fixTelNumber(numero) {
      if (numero === null) {
        return ''
      }
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
      if (numeroLimpo.length === 9) {
        return (`${numeroLimpo.slice(0, 5)}-${numeroLimpo.slice(5)}`)
      }
      if (numeroLimpo.length === 8) {
        return (`${numeroLimpo.slice(0, 4)}-${numeroLimpo.slice(4)}`)
      }
    },
    ///////////////////////////////////////////////////////////////////////////////

    // Métodos de refinamento: ---------------------------------------------------\
    HGPKEnter (event) {
      const e = event;
      const ENTER = e.key === 'Enter';

      // Recupera botões e elementos da página.
      let body = document.getElementsByTagName('body');
      let novoFornecedorButton = document.getElementById('novo-fornecedor-button');
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

      // Comparações e ações.
      if (noModalOpen && ENTER) {
        e.preventDefault();
        novoFornecedorButton.click();
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
    focusFirstModalInput,
    checkInputValue,
    setAttributeSalvarButton,
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
      id="novo-fornecedor-button"
      type="button"
      class="btn btn-success light-green"
      data-bs-toggle="modal"
      data-bs-target="#insertModal"
      @click="focusFirstModalInput('insert-name-input')"
    >+ Novo Fornecedor</button>
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
          <h1 class="modal-title fs-5" id="insertModalLabel">Novo Fornecedor</h1>
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
                placeholder="Nome do Fornecedor"
                v-model="nome"
                maxlength="70"
                @keyup="checkInputValue(nome, 'insert-name-input')"
              >
            </div>

            <!-- Categoria -->
            <div class="mb-3">
              <label id="insert-tipo-fornecedor-label" for="insert-tipo-fornecedor-select" class="bold red-asterisk">Categoria:</label>
              <select
                class="form-select"
                id="insert-tipo-fornecedor-select"
                v-model="tipoFornecedor"
                @click="checkInputValue(tipoFornecedor, 'insert-tipo-fornecedor-select')">
                <option value="Material">Material</option>
                <option value="Serviço">Serviço</option>
                <option value="Ambos">Ambos</option>
              </select>
            </div>

            <!-- Telefone -->
            <div class="mb-3">
              <label for="insert-telefone-input" class="form-label bold">Telefone:</label>
              <input
                type="number"
                class="form-control"
                id="insert-telefone-input"
                placeholder="Telefone (com DDD)"
                v-model="telefone"
                maxlength="20">
            </div>

            <!-- Endereço -->
            <div class="mb-3">
              <label for="insert-endereco-input" class="form-label bold">Endereço:</label>
              <input
                type="text"
                class="form-control"
                id="insert-endereco-input"
                placeholder="Avenida dos Fornecedores, 1000"
                v-model="endereco"
                maxlength="150">
            </div>

            <!-- Email -->
            <div class="mb-3">
              <label for="insert-email-input" class="form-label bold">Email:</label>
              <input
                type="text"
                class="form-control"
                id="insert-email-input"
                placeholder="fornecedor@gmail.com"
                v-model="email"
                maxlength="100">
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
          <h1 class="modal-title fs-5" id="updateModalLabel">Editar Fornecedor</h1>
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
                v-model="nome"
                disabled
                maxlength="70">
            </div>

            <!-- Categoria -->
            <div class="mb-3">
              <label for="update-tipo-fornecedor-select" class="bold">Categoria:</label>
              <select
                class="form-select"
                id="update-tipo-fornecedor-select"
                v-model="tipoFornecedor">
                <option value="Material">Material</option>
                <option value="Serviço">Serviço</option>
                <option value="Ambos">Ambos</option>
              </select>
            </div>

            <!-- Telefone -->
            <div class="mb-3">
              <label for="update-telefone-input" class="form-label bold">Telefone:</label>
              <input
                type="number"
                class="form-control"
                id="update-telefone-input"
                placeholder="Telefone (com DDD)"
                v-model="telefone"
                maxlength="20">
            </div>

            <!-- Endereço -->
            <div class="mb-3">
              <label for="update-endereco-input" class="form-label bold">Endereço:</label>
              <input
                type="text"
                class="form-control"
                id="update-endereco-input"
                placeholder="Avenida dos Fornecedores, 1000"
                v-model="endereco"
                maxlength="150">
            </div>

            <!-- Email -->
            <div class="mb-3">
              <label for="update-email-input" class="form-label bold">Email:</label>
              <input
                type="text"
                class="form-control"
                id="update-email-input"
                placeholder="fornecedor@gmail.com"
                v-model="email"
                maxlength="100">
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
          <th scope="col">Categoria</th>
          <th scope="col">Telefone</th>
          <th scope="col">Endereço</th>
          <th scope="col">Email</th>
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
          <td>{{ fornecedor.email }}</td>
          <td></td>
          <td class="editar-excluir">
            <button
              type="button"
              class="btn btn-light btn-sm small"
              title="Editar"
              data-bs-toggle="modal"
              data-bs-target="#updateModal"
              @click="fillUpdateDeleteModal(fornecedor.codigo, fornecedor.nome,
              fornecedor.tipoFornecedor, fornecedor.telefone, fornecedor.endereco, fornecedor.email)"
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