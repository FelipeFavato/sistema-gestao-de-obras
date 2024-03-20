<script>
import axios from 'axios';
import { useRouter } from 'vue-router';
import { focusFirstModalInput } from '../utils/inputFocus';
import { insertSuccessToast, updateSuccessToast, deleteSuccessToast,
  deleteErrorToast, insertErrorToast, updateErrorToast } from '../utils/toasts/index';
import { brazilCurrency } from '../utils/visualization';
import { getLocalStorageToken, getCodObraLocalStorage, getNomeObraLS } from '../utils/userLoginValidations';
import SkeletonTableAndHeader from '../components/skeletonLoading/SkeletonTableAndHeader.vue';
import { clickSavecheckRequiredInsertField, checkInputValue,
  removeElementClass, setAttributeSalvarButton } from '../utils/inputValidations';

// {
//   "codigoObra": {
//     "codigo": 1
//   },
//   "descricaoProdutoServico": "Porta X",
//   "quantidade": 5,
//   "valorUnitario": 10,
//   "valorTotalPrevisto": 50,
//   "compraPrevistaRealizada": false
// }

export default {
  data () {
    return {
      // Variáveis de autenticação/autorização: --\
      useRouter: useRouter(),
      localStorageToken: getLocalStorageToken(),
      httpStatus: '',
      /////////////////////////////////////////////
      // Variáveis de requisição/auxiliares: -----\
      obrasInfo: [],
      info: [],
      mdoOrcamentoInfo: [],
      codigo: '',
      codigoObra: '',
      descricaoProdutoServico: '',
      quantidade: '',
      valorUnitario: '',
      valorTotalPrevisto: '',
      compraPrevistaRealizada: false,
      /////////////////////////////////////////////
      // Variáveis de comportamento: -------------\
      selectedObraID: getCodObraLocalStorage(),
      selectedObraNome: '',
      selectedPrevisoesByObra: [],
      selectedFiltro: 'Não Comprado',
      customToastNotification: 'Previsão',
      /////////////////////////////////////////////
    }
  },

  props: {
    alturaMenu: Number,
  },

  components: {
    SkeletonTableAndHeader,
  },

  watch: {
    descricaoProdutoServico () {
      this.watchRequiredInsertFields();
    },
    quantidade () {
      this.watchRequiredInsertFields();
      this.watchUpdateRequiredFields();
    },
    valorUnitario () {
      this.watchRequiredInsertFields();
      this.watchUpdateRequiredFields();
    },
    valorTotalPrevisto () {
      this.watchRequiredInsertFields();
      this.watchUpdateRequiredFields();
    },
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
    cancel () {
      this.descricaoProdutoServico = '';
      this.quantidade = '';
      this.valorUnitario = '';
      this.valorTotalPrevisto = '';
      this.compraPrevistaRealizada = false;
    },
    cancelInsert () {
      this.cancel();

      removeElementClass('insert-descricao-input', 'required-red-border');
      removeElementClass('insert-descricao-label', 'campo-obrigatorio-warning');
      removeElementClass('insert-quantidade-input', 'required-red-border');
      removeElementClass('insert-quantidade-label', 'campo-obrigatorio-warning');
      removeElementClass('insert-valor-unitario-input', 'required-red-border');
      removeElementClass('insert-valor-unitario-label', 'campo-obrigatorio-warning');
      removeElementClass('insert-valor-total-previsto-input', 'required-red-border');
      removeElementClass('insert-valor-total-previsto-label', 'campo-obrigatorio-warning');
      setAttributeSalvarButton('salvarNovoButton', 'no-closing-modal');
    },
    cancelUpdate () {
      this.cancel();
      removeElementClass('update-quantidade-input', 'required-red-border');
      removeElementClass('update-quantidade-label', 'campo-obrigatorio-warning');
      removeElementClass('update-valor-unitario-input', 'required-red-border');
      removeElementClass('update-valor-unitario-label', 'campo-obrigatorio-warning');
      removeElementClass('update-valor-total-previsto-input', 'required-red-border');
      removeElementClass('update-valor-total-previsto-label', 'campo-obrigatorio-warning');
      setAttributeSalvarButton('salvarNovoButton', 'no-closing-modal');
    },
    ///////////////////////////////////////////////////////////////////////////////

    // Métodos de busca - GET: ---------------------------------------------------\
    fetchInfoDB (callback) {
      axios.get('/api/previsao-compra',
      {
        headers: {
          Authorization: this.localStorageToken
        }
      }).then(res => {
        this.info = res.data.sort((s1, s2) => s2['codigo'] - s1['codigo']);
        this.setHttpStatusCode(res.status);
        if (callback) callback();
      }).catch(error => {
        this.validateHttpStatus(error.response.status);
      });
    },
    fetchObrasInfoDB (callback) {
      axios.get('/api/obra',
      {
        headers: {
          Authorization: this.localStorageToken
        }
      }).then(res => {
        this.obrasInfo = res.data.sort((s1, s2) => s2.codigo - s1.codigo)
        this.setHttpStatusCode(res.status);
        if (callback) callback();
      }).catch(error => {
        this.validateHttpStatus(error.response.status);
      });
    },
    fetchMDOOrcamentoInfo (callback) {
      axios.get(`/api/dashboard/mdogastoorcamento?codigo=${this.selectedObraID}`,
      {
        headers: {
          Authorization: this.localStorageToken
        },
      }).then(res => {
        this.mdoOrcamentoInfo = res.data;
        if (callback) callback();
      }).catch(error => {
        this.validateHttpStatus(error.response);
      })
    },
        learning () {
      const orcamento = this.mdoOrcamentoInfo[0];
      const gasto = orcamento.custoMaoDeObra + orcamento.valorGastos;
      const orc = orcamento.custoPrevisto - gasto;
      console.log(orcamento);
      console.log(gasto);
      console.log(brazilCurrency(orc))
    },
    ///////////////////////////////////////////////////////////////////////////////

    // Métodos de insert - POST: -------------------------------------------------\
    create () {
      axios.post('/api/previsao-compra',
        {
          codigoObra: {
            codigo: this.selectedObraID
          },
          descricaoProdutoServico: this.descricaoProdutoServico,
          quantidade: this.quantidade,
          valorUnitario: this.valorUnitario,
          valorTotalPrevisto: this.valorTotalPrevisto,
          compraPrevistaRealizada: false
        },
        {
          headers: {
            Authorization: this.localStorageToken
          }
        }).then((res) => {
          this.fetchObrasInfoDB(() => {
            this.obrasDropDownActions(this.selectedObraNome, this.selectedObraID);
            this.filtroDropDownActions(this.selectedFiltro);
          });
          this.setHttpStatusCode(res.status);
          insertSuccessToast(this.customToastNotification);
        }).catch(error => {
          this.validateHttpStatus(error.response.status);
          insertErrorToast(error.response.data.resposta);
        });
      this.cancelInsert();
    },
    watchRequiredInsertFields () {
      this.descricaoProdutoServico && this.quantidade && this.valorUnitario && this.valorTotalPrevisto ?
      setAttributeSalvarButton('salvarNovoButton', 'modal'):
      setAttributeSalvarButton('salvarNovoButton', 'no-closing-modal');
    },
    createInfoDB () {
      clickSavecheckRequiredInsertField(this.descricaoProdutoServico, 'insert-descricao-input', 'insert-descricao-label', 'salvarNovoButton');
      clickSavecheckRequiredInsertField(this.quantidade, 'insert-quantidade-input', 'insert-quantidade-label', 'salvarNovoButton');
      clickSavecheckRequiredInsertField(this.valorUnitario, 'insert-valor-unitario-input', 'insert-valor-unitario-label', 'salvarNovoButton');
      clickSavecheckRequiredInsertField(this.valorTotalPrevisto, 'insert-valor-total-previsto-input', 'insert-valor-total-previsto-label', 'salvarNovoButton');

      if (this.descricaoProdutoServico && this.quantidade && this.valorUnitario && this.valorTotalPrevisto) {
        this.create();
      }
    },
    ///////////////////////////////////////////////////////////////////////////////

    // Métodos de atualização - UPDATE: ------------------------------------------\
    fillUpdateModal (cod, descr, qnt, valorU, valorT, compraPR) {
      this.codigo = cod;
      this.descricaoProdutoServico = descr;
      this.quantidade = qnt;
      this.valorUnitario = valorU;
      this.valorTotalPrevisto = valorT;
      this.compraPrevistaRealizada = compraPR;
    },
    updateCompraPrevistaRealizada(cod, descr, qnt, vu, vt, compraPR) {
      this.fillUpdateModal(cod, descr, qnt, vu, vt, compraPR);
      axios.put('/api/previsao-compra',
        {
          codigo: this.codigo,
          codigoObra: { codigo: this.selectedObraID },
          descricaoProdutoServico: this.descricaoProdutoServico,
          quantidade : this.quantidade,
          valorUnitario: this.valorUnitario,
          valorTotalPrevisto: this.valorTotalPrevisto,
          compraPrevistaRealizada: this.compraPrevistaRealizada 
        },
        {
          headers: {
            Authorization: this.localStorageToken
          }
        }).then(res => {
          updateSuccessToast(this.customToastNotification);
        }).catch(error => {
          updateErrorToast(this.customToastNotification);
        }
      );
      this.cancel();
    },
    update () {
      axios.put('/api/previsao-compra',
      {
        codigo: this.codigo,
        codigoObra: { codigo: this.selectedObraID },
        descricaoProdutoServico: this.descricaoProdutoServico,
        quantidade : this.quantidade,
        valorUnitario: this.valorUnitario,
        valorTotalPrevisto: this.valorTotalPrevisto,
        compraPrevistaRealizada: this.compraPrevistaRealizada 
      },
      {
        headers: {
          Authorization: this.localStorageToken
        }
      }).then(res => {
        this.fetchObrasInfoDB(() => {
            this.obrasDropDownActions(this.selectedObraNome, this.selectedObraID);
            this.filtroDropDownActions(this.selectedFiltro);
          });
        this.setHttpStatusCode(res.status);
        updateSuccessToast(this.customToastNotification);
      }).catch(error => {

      })
    },
    watchUpdateRequiredFields () {
      this.quantidade && this.valorUnitario && this.valorTotalPrevisto ?
        setAttributeSalvarButton('atualizarButton', 'modal'):
        setAttributeSalvarButton('atualizarButton', 'no-closing-modal');
    },
    updateInfoDB () {
      clickSavecheckRequiredInsertField(this.quantidade, 'update-quantidade-input', 'update-quantidade-label', 'atualizarButton');
      clickSavecheckRequiredInsertField(this.valorUnitario, 'update-valor-unitario-input', 'update-valor-unitario-label', 'atualizarButton');
      clickSavecheckRequiredInsertField(this.valorTotalPrevisto, 'update-valor-total-previsto-input', 'update-valor-total-previsto-label', 'atualizarButton');
      
      if (this.quantidade && this.valorUnitario && this.valorTotalPrevisto) {
        this.update();
      }
    },
    ///////////////////////////////////////////////////////////////////////////////

    // Métodos de deleção - UPDATE: ----------------------------------------------\
    removeFromDB (codigo) {
      axios.delete('/api/previsao-compra',
        {
          headers: {
            Authorization: this.localStorageToken
          },
          data: {
            codigo: Number(codigo)
          }
        }).then((res) => {
          this.fetchInfoDB();
          this.fetchObrasInfoDB(() => {
            this.obrasDropDownActions(this.selectedObraNome, this.selectedObraID);
            this.filtroDropDownActions(this.selectedFiltro);
          });
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
    selectPrevisoesByObra (obraCod) {
      let selectedPrevisoes = [];
      for (let obra of this.obrasInfo) {
        if (obra.codigo === obraCod) {
          selectedPrevisoes = obra.previsoes;
        }
      }

      this.selectedPrevisoesByObra = selectedPrevisoes.sort((s1, s2) => s2['codigo'] - s1['codigo']);
    },
    obrasDropDownActions(obraNome, obraCod) {
      this.selectedObraNome = obraNome;
      this.selectedObraID = obraCod;
      this.selectPrevisoesByObra(Number(obraCod));

      this.filtroDropDownActions(this.selectedFiltro);
    },
    filtroDropDownActions(selected) {
      this.selectedFiltro = selected;

      if (selected === 'Não Comprado') {
        this.selectPrevisoesByObra(Number(this.selectedObraID));
        this.selectedPrevisoesByObra = this.selectedPrevisoesByObra.filter(prev => prev.compraPrevistaRealizada === false);
      } else if (selected === 'Comprado') {
        this.selectPrevisoesByObra(Number(this.selectedObraID));
        this.selectedPrevisoesByObra = this.selectedPrevisoesByObra.filter(prev => prev.compraPrevistaRealizada === true);
      } else if (selected === 'Todas') {
        this.selectPrevisoesByObra(Number(this.selectedObraID));
      }
    },
    ///////////////////////////////////////////////////////////////////////////////

    // Métodos de refinamento: ---------------------------------------------------\
    calculaValorTotalPrevisto(data, field) {
      !this.valorUnitario ? this.valorTotalPrevisto = 0 : this.valorTotalPrevisto = this.quantidade * this.valorUnitario;
      checkInputValue(data, field);
      checkInputValue(this.valorTotalPrevisto, 'insert-valor-total-previsto-input');

    },
    focusFirstModalInput,
    checkInputValue,
    brazilCurrency,
    // HGPK = Handle Global Press Key
    HGPKEnter (event) {
      const e = event;
      const ENTER = e.key === 'Enter';

      // Recupera botões e elementos da página.
      let body = document.getElementsByTagName('body');
      let novoLocalButton = document.getElementById('nova-previsao-button');
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
    ///////////////////////////////////////////////////////////////////////////////
  },

  mounted () {
    this.validateLogin();
    this.fetchInfoDB();
    this.fetchObrasInfoDB(() => {
      this.obrasDropDownActions(getNomeObraLS(), getCodObraLocalStorage());
      this.filtroDropDownActions(this.selectedFiltro);
    });
    this.fetchMDOOrcamentoInfo(() => {
      this.learning();
    })
    this.addHGPKEnter();
  },

  unmounted() {
    this.removeHGPKEnter();
  }
}


</script>

<template>

  <!-- Header com o DropDown 'Obras' -->
  <!-- v-if="this.info != ''" -->
  <header
    class="header middle-margin">
    <!-- DropDown 'Obras' -->
    <div class="dropdown">
      <button class="btn btn-secondary dropdown-toggle" type="button" data-bs-toggle="dropdown" aria-expanded="false">
        {{ selectedObraNome ? selectedObraNome : 'Obras' }}
      </button>
      <ul class="dropdown-menu">
        <li v-for="(obra, i) in obrasInfo" :key="i">
          <button
            class="dropdown-item"
            type="button"
            @click="obrasDropDownActions(obra.nome, obra.codigo)"
            >{{ obra.nome }}</button>
        </li>
        <li><hr class="dropdown-divider"></li>
        <li>
          <button
            class="dropdown-item"
            type="button"
            @click="obrasDropDownActions('', '')"
          >Limpar</button>
        </li>
      </ul>
    </div>

    <!-- Filtro Previsão concluida ou não -->
    <div class="dropdown">
      <button class="btn btn-secondary dropdown-toggle" type="button" data-bs-toggle="dropdown" aria-expanded="false">
        {{ this.selectedFiltro ? this.selectedFiltro : 'Não Comprado' }}
      </button>
      <ul class="dropdown-menu">
        <li class="vermelho-transparente">
          <button
            class="dropdown-item"
            type="button"
            @click="filtroDropDownActions('Não Comprado')"
            >Não Comprado</button>
        </li>
        <li class="verde-transparente">
          <button
            class="dropdown-item"
            type="button"
            @click="filtroDropDownActions('Comprado')"
            >Comprado</button>
        </li>
        <li><hr class="dropdown-divider"></li>
        <li>
          <button
            class="dropdown-item"
            type="button"
            @click="filtroDropDownActions('Todas')"
          >Todas</button>
        </li>
      </ul>
    </div>
  </header>

  <!-- Header com o botão de +Novo -->
  <!-- v-if="this.info != ''" -->
  <header
    class="middle-margin">
    <button
      id="nova-previsao-button"
      type="button"
      class="btn btn-success light-green"
      data-bs-toggle="modal"
      data-bs-target="#insertModal"
      @click="focusFirstModalInput('insert-descricao-input')"
    >+ Nova Previsão</button>
  </header>

  <!-- <SkeletonTableAndHeader v-if="this.info == ''" /> -->

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
          <h1 class="modal-title fs-5" id="insertModalLabel">Nova previsão</h1>
          <button @click="cancelInsert" type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
        </div>

        <div class="modal-body">
          <form action="POST">

            <!-- Descrição -->
            <div class="mb-3">
              <label id="insert-descricao-label" for="insert-descricao-input" class="form-label bold red-asterisk">Descrição:</label>
              <input
              type="text"
              class="form-control"
              id="insert-descricao-input"
              placeholder="Janela, porta, conexão..."
              v-model="descricaoProdutoServico"
              maxlength="100"
              @keyup="checkInputValue(descricaoProdutoServico, 'insert-descricao-input')"
              >
            </div>
            
            <!-- Quantidade -->
            <div class="mb-3">
              <label id="insert-quantidade-label" for="insert-quantidade-input"
                class="form-label bold red-asterisk">Quantidade:</label>
                <!-- @change="calculaValorItem" -->
                <input
                type="number"
                class="form-control"
                id="insert-quantidade-input"
                placeholder="(inserir apenas números)"
                v-model="quantidade"
                maxlength="15"
                @keyup="calculaValorTotalPrevisto(quantidade, 'insert-quantidade-input')"
              >
            </div>

            <!-- Valor Unitário -->
            <div class="mb-3">
              <label id="insert-valor-unitario-label" for="insert-valor-unitario-input" class="form-label bold red-asterisk">Valor unitário:</label>
              <!-- @change="calculaValorFinalCompra"
              @keyup="checkInputValue(valorOriginal, 'insert-valor-unitario-input')" -->
              <input
                type="number"
                step="0.01"
                title=''
                class="form-control"
                id="insert-valor-unitario-input"
                placeholder="R$... (inserir apenas números e ponto)"
                v-model="valorUnitario"
                maxlength="15"
                @keyup="calculaValorTotalPrevisto(valorUnitario, 'insert-valor-unitario-input')"
              >
            </div>

            <!-- Valor Total Previsto -->
            <div class="mb-3">
              <label id="insert-valor-total-previsto-label" for="insert-valor-total-previsto-input"
                class="form-label bold red-asterisk">Valor total previsto:</label>
              <input
                type="number"
                step="0.01"
                title=''
                class="form-control"
                id="insert-valor-total-previsto-input"
                placeholder="R$... (inserir apenas números e ponto)"
                v-model="valorTotalPrevisto"
                maxlength="15"
                @change="checkInputValue(valorTotalPrevisto, 'insert-valor-total-previsto-input')"
              >
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
          <h1 class="modal-title fs-5" id="updateModalLabel">Editar Previsão de Custo</h1>
          <button @click="cancelUpdate" type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
        </div>

        <div class="modal-body">
          <form action="PUT">

            <div class="mb-3">
              <label id="update-codigo-label" for="update-codigo-input" class="form-label bold">Código:</label>
              <input
                type="text"
                class="form-control"
                id="update-codigo-input"
                disabled
                v-model="codigo">
            </div>

            <div class="mb-3">
              <label id="update-descricao-label" for="update-descricao-input" class="form-label bold">Descrição:</label>
              <input
                type="text"
                class="form-control"
                id="update-descricao-input"
                placeholder="Alvenaria, Ferragens, etc..."
                v-model="descricaoProdutoServico"
                disabled
                maxlength="100">
            </div>

            <!-- Quantidade -->
            <div class="mb-3">
              <label id="update-quantidade-label" for="update-quantidade-input"
                class="form-label bold red-asterisk">Quantidade:</label>
                <input
                type="number"
                class="form-control"
                id="update-quantidade-input"
                placeholder="(inserir apenas números)"
                v-model="quantidade"
                maxlength="15"
                @keyup="calculaValorTotalPrevisto(quantidade, 'update-quantidade-input')"
              >
            </div>

            <!-- Valor Unitário -->
            <div class="mb-3">
              <label id="update-valor-unitario-label" for="update-valor-unitario-input" class="form-label bold red-asterisk">Valor unitário:</label>
              <input
                type="number"
                step="0.01"
                title=''
                class="form-control"
                id="update-valor-unitario-input"
                placeholder="R$... (inserir apenas números e ponto)"
                v-model="valorUnitario"
                maxlength="15"
                @keyup="calculaValorTotalPrevisto(valorUnitario, 'update-valor-unitario-input')"
              >
            </div>

            <!-- Valor Total Previsto -->
            <div class="mb-3">
              <label id="update-valor-total-previsto-label" for="update-valor-total-previsto-input"
                class="form-label bold red-asterisk">Valor total previsto:</label>
              <input
                type="number"
                step="0.01"
                title=''
                class="form-control"
                id="update-valor-total-previsto-input"
                placeholder="R$... (inserir apenas números e ponto)"
                v-model="valorTotalPrevisto"
                maxlength="15"
                @change="checkInputValue(valorTotalPrevisto, 'update-valor-total-previsto-input')"
              >
            </div>

          </form>
        </div>

        <div class="modal-footer">
          <button id="fecharUpdateButton" type="button" class="btn btn-secondary dark-grey" data-bs-dismiss="modal"
            @click="cancelUpdate"
          >Fechar</button>

          <!-- data-bs-dismiss="modal" -->
          <button
            id="atualizarButton"
            type="button"
            class="btn btn-success light-green"
            @click="updateInfoDB()"
          >Salvar</button>
        </div>
      </div>
    </div>
  </div>

  <!-- Tabela Previsões -->
  <!-- v-if="this.info != ''" -->
  <main
    class="middle-margin table-responsive">
    <table class="table table-hover">
      <thead>
        <tr>
          <th scope="col">Código</th>
          <th scope="col">Descrição</th>
          <th scope="col">Quantidade</th>
          <th scope="col">Valor unitário</th>
          <th scope="col">Valor total previsto</th>
          <th scope="col">Compra realizada</th>
          <th></th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="(previsaoCompra, i) in this.selectedPrevisoesByObra" :key="i">
          <th scope="row">{{ previsaoCompra.codigo }}</th>
          <td>{{ previsaoCompra.descricaoProdutoServico }}</td>
          <td>{{ previsaoCompra.quantidade }}</td>
          <td>{{ brazilCurrency(previsaoCompra.valorUnitario) }}</td>
          <td>{{ brazilCurrency(previsaoCompra.valorTotalPrevisto) }}</td>
          <td class="">
            <input
              class="checkbox"
              type="checkbox"
              :id="'compra-prevista-realizada-checkbox-input' + previsaoCompra.codigo"
              v-model="previsaoCompra.compraPrevistaRealizada"
              @change="updateCompraPrevistaRealizada(previsaoCompra.codigo, previsaoCompra.descricaoProdutoServico,
                previsaoCompra.quantidade, previsaoCompra.valorUnitario, previsaoCompra.valorTotalPrevisto, previsaoCompra.compraPrevistaRealizada)"
            />
            <label
              class="checkbox-label"
              id="checkbox-label"
              :for="'compra-prevista-realizada-checkbox-input' + previsaoCompra.codigo"
            ></label>
          </td>
          <td class="editar-excluir">
            <button
              type="button"
              class="btn btn-light btn-sm small"
              title="Editar"
              data-bs-toggle="modal"
              data-bs-target="#updateModal"
              @click="fillUpdateModal(previsaoCompra.codigo, previsaoCompra.descricaoProdutoServico,
                previsaoCompra.quantidade, previsaoCompra.valorUnitario, previsaoCompra.valorTotalPrevisto, previsaoCompra.compraPrevistaRealizada)"
            ><img src="../assets/imagens/editar.png" alt="editar" ></button>
            
            <button
              type="button"
              class="btn btn-light btn-sm small"
              title="Editar"
              data-bs-toggle="modal"
              data-bs-target="#deleteModal"
              @click="fillUpdateModal(previsaoCompra.codigo)"
            ><img src="../assets/imagens/lata-de-lixo.png" alt="lata de lixo" ></button>
          </td>
        </tr>
      </tbody>
    </table>
  </main>
</template>

<style>

.checkbox {
  padding-left: 130px;
}

input[type="checkbox"] {
  position: relative;
  width: 28px;
  height: 28px;
  background-color: #cfcfcf;
  border-radius: .25rem;
  appearance: none;
  cursor: pointer;
}

input[type="checkbox"]:checked {
  background-color: limegreen;
}

input[type="checkbox"]::before {
  content: '';
  position: absolute;
  top: 2px;
  left: 63px;
  width: 9px;
  height: 17px;
  border: 4px solid transparent;
  border-left: none;
  border-top: none;
  transform: rotate(45deg) scale(1.5);
}

input[type="checkbox"]:checked:before {
  border-color: #fff;
  animation: checkAnim 0.5s ease;
}

@keyframes checkAnim {
  from {
    transform: rotate(45deg) scale(0);
  }
}

.verde-transparente {
  background-color: rgba(0, 255, 0, 0.2); /* Verde com 20% de opacidade */
}

.vermelho-transparente {
  background-color: rgba(255, 0, 0, 0.2); /* Vermelho com 20% de opacidade */
}

.red-border {
  border: 1px solid red;
}

</style>