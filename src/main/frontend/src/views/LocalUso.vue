<script>
import axios from 'axios';
import { useRouter } from 'vue-router';

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
      codigoLocalUsoObra: '',
      nomeLocalUsoObra: '',
      dataDesativacao: null,
      /////////////////////////////////////////////
      // Variáveis de comportamento: -------------\
      recarregarPagina: true,
      /////////////////////////////////////////////
    };
  },

  props: {
    alturaMenu: Number,
  },

  // O vue percebe que estou usando algumas variaveis, qualquer atualização nas variaveis, ele atualiza
  // uma variável X
  computed: {

  },

  // Pode definir um comportamento a ser chamado quando uma variável mudar.
  watch: {
    // variavel() {

    // }
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
    ///////////////////////////////////////////////////////////////////////////////

    // Métodos de busca - GET: ---------------------------------------------------\
    fetchInfoDB () {
      axios.get('/api/localuso',
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
    ///////////////////////////////////////////////////////////////////////////////
    
    // Métodos de INSERT - POST: -------------------------------------------------\
    createInfoDB () {
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
        }).catch(error => {
          this.validateHttpStatus(error.response.status);
        }
      );
      this.cancel();
    },
    ///////////////////////////////////////////////////////////////////////////////

    // Métodos de UPDATE - PUT: --------------------------------------------------\
    fillUpdateDeleteModal (cod, nome, data) {
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
        }).catch(error => {
          this.validateHttpStatus(error.response.status);
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
        }).catch(error => {
          this.validateHttpStatus(error.response.status);
        });
      this.cancel();
    },
    ///////////////////////////////////////////////////////////////////////////////

    // Métodos de comportamento: -------------------------------------------------\
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
      // Confere se o botão apertado foi o 'ENTER'.
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

      // Se não houver nenhuma modal aberta, ao clicar ENTER deve ser acionado o
      // botão de '+ Novo Local'.
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
    // Precisa ser melhorado:
    // focusFirstInput () {
    //   setTimeout(() => {
    //     document.getElementById('nameInput').focus();
    //   }, 500);
    // }
    ///////////////////////////////////////////////////////////////////////////////

  },

  mounted () {
    this.getLocalStorageToken();
    this.validateLogin();
    this.fetchInfoDB();
    // Adiciona os escutadores de evento relacionados a tecla 'Enter'.
    this.addHGPKEnter();
  },

  unmounted () {
    // remove os escutadores de evento relacionados a tecla 'Enter'.
    this.removeHGPKEnter();
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
      id="novoLocalButton"
      @click="focusFirstInput"
      v-on:click=""
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
          <button @click="cancel" type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
        </div>

        <div class="modal-body">
          <form action="POST">

            <div class="mb-3">
              <label for="nameInput" class="form-label bold">Local:</label>
              <input
                type="text"
                class="form-control"
                id="nameInput"
                placeholder="Fundação, Hidráulica, etc..."
                v-model="nomeLocalUsoObra"
                maxlength="30">
            </div>

          </form>
        </div>

        <div class="modal-footer">
          <button
            id="fecharInsertButton"
            type="button"
            class="btn btn-secondary dark-grey"
            data-bs-dismiss="modal"
            @click="cancel"
          >Fechar</button>
          <button
            id="salvarNovoButton"
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
                disabled
                maxlength="30">
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