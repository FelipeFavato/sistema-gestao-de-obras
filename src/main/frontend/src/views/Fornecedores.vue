<script>
import { useRouter } from 'vue-router';
import axios from 'axios';

export default {
  data () {
    return {
      // Variáveis de autenticação/autorização: --\
      useRouter: useRouter(),
      localStorageToken: null,
      httpStatus: '',
      /////////////////////////////////////////////
      // Variáveis auxiliares: -------------------\
      info: [],
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

  props: {
    alturaMenu: Number,
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
      this.telefone = '';
      this.endereco = '';
      this.email = '';
      this.tipoFornecedor = '';
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
    createInfoDB () {
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
        }).catch(error => {
          this.validateHttpStatus(error.response.status);
        });
      this.cancel();
    },
    ///////////////////////////////////////////////////////////////////////////////

    // Métodos de UPDATE - PUT: --------------------------------------------------\
    fillUpdateDeleteModal (cod, nome, tipoF, tel, endereco, email) {
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
        }).catch(error => {
          this.validateHttpStatus(error.response.status);
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
        }).catch(error => {
          this.validateHttpStatus(error.response.status);
          console.log(error)
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

  <!-- Header com o botão de +Novo -->
  <header class="header middle-margin">
    <button
      id="novo-fornecedor-button"
      type="button"
      class="btn btn-success light-green"
      data-bs-toggle="modal"
      data-bs-target="#insertModal"
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
                placeholder="Nome do Fornecedor"
                v-model="nome"
                maxlength="70">
            </div>

            <!-- Categoria -->
            <div class="mb-3">
              <label for="tipoFornecedor-select" class="bold">Categoria:</label>
              <select
                class="form-select"
                id="tipoFornecedor-select"
                v-model="tipoFornecedor">
                <option value="Material">Material</option>
                <option value="Serviço">Serviço</option>
                <option value="Ambos">Ambos</option>
              </select>
            </div>

            <!-- Telefone -->
            <div class="mb-3">
              <label for="telefone-input" class="form-label bold">Telefone:</label>
              <input
                type="text"
                class="form-control"
                id="telefone-input"
                placeholder="Telefone (com DDD)"
                v-model="telefone"
                maxlength="20">
            </div>

            <!-- Endereço -->
            <div class="mb-3">
              <label for="address-input" class="form-label bold">Endereço:</label>
              <input
                type="text"
                class="form-control"
                id="address-input"
                placeholder="Avenida dos Fornecedores, 1000"
                v-model="endereco"
                maxlength="150">
            </div>

            <!-- Email -->
            <div class="mb-3">
              <label for="email-input" class="form-label bold">Email:</label>
              <input
                type="text"
                class="form-control"
                id="email-input"
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
            @click="cancel"
          >Fechar</button>
          <button
            id="salvar-novo-button"
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
          <h1 class="modal-title fs-5" id="updateModalLabel">Editar Fornecedor</h1>
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
                v-model="nome"
                disabled
                maxlength="70">
            </div>

            <!-- Categoria -->
            <div class="mb-3">
              <label for="tipoFornecedor-select" class="bold">Categoria:</label>
              <select
                class="form-select"
                id="tipoFornecedor-select"
                v-model="tipoFornecedor">
                <option value="Material">Material</option>
                <option value="Serviço">Serviço</option>
                <option value="Ambos">Ambos</option>
              </select>
            </div>

            <!-- Telefone -->
            <div class="mb-3">
              <label for="telefone-input" class="form-label bold">Telefone:</label>
              <input
                type="text"
                class="form-control"
                id="telefone-input"
                placeholder="Telefone (com DDD)"
                v-model="telefone"
                maxlength="20">
            </div>

            <!-- Endereço -->
            <div class="mb-3">
              <label for="endereco-input" class="form-label bold">Endereço:</label>
              <input
                type="text"
                class="form-control"
                id="endereco-input"
                placeholder="Avenida dos Fornecedores, 1000"
                v-model="endereco"
                maxlength="150">
            </div>

            <!-- Email -->
            <div class="mb-3">
              <label for="email-input" class="form-label bold">Email:</label>
              <input
                type="text"
                class="form-control"
                id="email-input"
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
  <main class="middle-margin table-responsive">
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