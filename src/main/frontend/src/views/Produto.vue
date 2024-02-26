<script>
import { useRouter } from 'vue-router';
import axios from 'axios';
import { insertSuccessToast, updateSuccessToast, deleteSuccessToast,
  deleteErrorToast, insertErrorToast, updateErrorToast } from '../utils/toasts/index';

// Requisição de Novo Produto
// {
//   nome: String,
//   tipoProduto: String,
//   marca: {
//     codigo: Number
//   } 
// }

export default {
  data () {
    return {
      // Arrays auxiliares
      info: [],
      marcasInfo: [],
      // Variaveis auxiliares
      useRouter: useRouter(),
      httpStatus: '',
      customToastNotification: 'Produto',
      // Variaveis para requisição
      localStorageToken: null,
      codigo: '',
      nome: '',
      tipoProduto: '',
      marca: {},
      selectedMarcaCod: '',
      selectedMarcaNome: ''
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
      this.tipoProduto = '';
      this.marca = {};
      this.selectedMarcaNome = '';
      this.selectedMarcaCod = '';
    },
    ///////////////////////////////////////////////////////////////////////////////

    // Métodos de busca - GET: ---------------------------------------------------\
    fetchInfoDB (callback) {
      axios.get('/api/produto',
      {
        headers: {
          Authorization: this.localStorageToken
        }
      }).then(res => {
        this.info = res.data.sort((s1, s2) => s1['nome'].localeCompare(s2['nome']));
        this.setHttpStatusCode(res.status);
        if (callback) callback();
      }).catch(error => {
        this.validateHttpStatus(error.response.status);
      });
    },
    fetchMarcasInfoDB () {
      axios.get('/api/marca',
      {
        headers: {
          Authorization: this.localStorageToken
        }
      }).then(res => {
        this.marcasInfo = res.data.sort((s1, s2) => s1['nome'].localeCompare(s2['nome']));
        this.setHttpStatusCode(res.status);
      }).catch(error => {
        this.validateHttpStatus(error.response.status);
      });
    },
    ///////////////////////////////////////////////////////////////////////////////

    // Métodos de INSERT - POST: -------------------------------------------------\
    createProdutoInfoDB () {
      axios.post('/api/produto',
        {
          nome: this.nome,
          tipoProduto: this.tipoProduto,
          marca: {
            codigo: this.selectedMarcaCod
          }
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
    fillUpdateDeleteModal (cod, nome, tipo, marcaCod, marcaNome) {
      this.codigo = cod;
      this.nome = nome;
      this.tipoProduto = tipo;
      this.selectedMarcaCod = marcaCod;
      this.selectedMarcaNome = marcaNome;
    },
    fillCodigoMarcaForUpdate () {
      for (let marca of this.marcasInfo) {
        if (marca.nome === this.selectedMarcaNome) {
          this.selectedMarcaCod = marca.codigo
        }
      }
    },
    updateInfo () {
      axios.put('/api/produto',
        {
          codigo: this.codigo,
          nome: this.nome,
          tipoProduto: this.tipoProduto,
          marca: {
            codigo: this.selectedMarcaCod
          }
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
    updateInfoDB () {
      this.fillCodigoMarcaForUpdate();
      this.updateInfo();
    },
    ///////////////////////////////////////////////////////////////////////////////

    // Métodos de DELETE - DELETE: -----------------------------------------------\
    removeFromDB (codigo) {
      axios.delete('/api/produto',
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
          deleteErrorToast('ITENS DE COMPRA');
        });
      this.cancel();
    },
    ///////////////////////////////////////////////////////////////////////////////

    // Métodos de renderização: --------------------------------------------------\
    fixTaxasImpostos(tipo) {
      return tipo === "TaxasImpostos" ? "Taxas/Impostos" : tipo;
    },
    ///////////////////////////////////////////////////////////////////////////////

    // Métodos de refinamento: ---------------------------------------------------\
    HGPKEnter (event) {
      const e = event;
      const ENTER = e.key === 'Enter';

      // Recupera botões e elementos da tela.
      let body = document.getElementsByTagName('body');
      let novoProdutoButton = document.getElementById('novo-produto-Button');
      let salvaNovoButton =document.getElementById('salva-novo-produto-button');
      let atualizaButton = document.getElementById('atualiza-button');
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
        novoProdutoButton.click();
      } else if (!noModalOpen && isInsertModal && ENTER) {
        e.preventDefault();
        salvaNovoButton.click();
      } else if (!noModalOpen && isUpdateModal && ENTER) {
        e.preventDefault();
        atualizaButton.click();
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
    ///////////////////////////////////////////////////////////////////////////////

  },


  mounted () {
    this.getLocalStorageToken();
    this.validateLogin();
    this.fetchInfoDB();
    this.fetchMarcasInfoDB();
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
      id="novo-produto-Button"
      type="button"
      class="btn btn-success light-green"
      data-bs-toggle="modal"
      data-bs-target="#insertModal"
    >+ Novo Produto</button>
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
          <h1 class="modal-title fs-5" id="insertModalLabel">Novo Produto</h1>
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
                placeholder="Produto, mão de obra, etc..."
                v-model="nome"
                maxlength="100"
                >
            </div>

            <!-- Marca -->
            <div class="mb-3">
              <label for="marca-empresa-select" class="bold">Marca/Empresa:</label>
              <select
                class="form-select"
                id="marca-empresa-select"
                v-model="selectedMarcaCod">
                <option
                  v-for="(marca, i) in marcasInfo" :key="i" :value="marca.codigo"
                  >{{ marca.nome }}</option>
              </select>
            </div>

            <!-- Categoria -->
            <div class="mb-3">
              <label for="tipoProduto-select" class="bold">Categoria:</label>
              <select
                class="form-select"
                id="tipoProduto-select"
                v-model="tipoProduto">
                <option value="Material">Material</option>
                <option value="Serviço">Serviço</option>
                <option value="TaxasImpostos">Taxas/Impostos</option>
              </select>
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
            id="salva-novo-produto-button"
            type="button"
            class="btn btn-success  light-green"
            data-bs-dismiss="modal"
            @click="createProdutoInfoDB"
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
          <h1 class="modal-title fs-5" id="updateModalLabel">Editar Produto</h1>
          <button @click="cancel" type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
        </div>

        <div class="modal-body">

          <form action="PUT">

            <!-- Codigo -->
            <div class="mb-3">
              <label for="id-input" class="form-label bold">Código:</label>
              <input
                type="text"
                class="form-control"
                id="id-input"
                disabled
                v-model="codigo">
            </div>

            <!-- nome -->
            <div class="mb-3">
              <label for="nome-input" class="form-label bold">Nome:</label>
              <input
                type="text"
                class="form-control"
                id="nome-input"
                placeholder="Produto, mão de obra, etc..."
                v-model="nome"
                disabled>
            </div>

            <!-- Marca -->
            <div class="mb-3">
              <label for="marca-empresa-select" class="bold">Marca/Empresa:</label>
              <select
                class="form-select"
                id="marca-empresa-select"
                v-model="selectedMarcaNome">
                <option
                  v-for="(marca, i) in marcasInfo" :key="i" :value="marca.nome"
                  >{{ marca.nome }}</option>
              </select>
            </div>

            <!-- Categoria -->
            <div class="mb-3">
              <label for="tipoProduto-select" class="bold">Categoria:</label>
              <select
                class="form-select"
                id="tipoProduto-select"
                v-model="tipoProduto">
                <option value="Material">Material</option>
                <option value="Serviço">Serviço</option>
                <option value="TaxasImpostos">Taxas/Impostos</option>
              </select>
            </div>

          </form>

        </div>

        <div class="modal-footer">
          <button type="button" class="btn btn-secondary dark-grey" data-bs-dismiss="modal"
            @click="cancel"
          >Fechar</button>

          <button
            id="atualiza-button"
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
          <th scope="col">Marca/Empresa</th>
          <th></th>
          <th></th>
          <th></th>
          <th></th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="(produto, i) in info" :key="i">
          <th scope="row">{{ produto.codigo }}</th>
          <td>{{ produto.nome }}</td>
          <td>{{ fixTaxasImpostos(produto.tipoProduto) }}</td>
          <td>{{ produto.marca ? produto.marca.nome : null }}</td>
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
              @click="fillUpdateDeleteModal(produto.codigo, produto.nome, produto.tipoProduto, produto.marca.codigo, produto.marca.nome)"
            ><img src="../assets/imagens/editar.png" alt="lata de lixo"></button>
            <button
              type="button"
              class="btn btn-light btn-sm small"
              title="Excluir"
              data-bs-toggle="modal"
              data-bs-target="#deleteModal"
              @click="fillUpdateDeleteModal(produto.codigo)"
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