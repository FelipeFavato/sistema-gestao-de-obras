<script>

import axios from 'axios';
import { useRouter } from 'vue-router';
import { insertSuccessToast, updateSuccessToast, deleteSuccessToast,
  deleteErrorToast, insertErrorToast, updateErrorToast ,assignSuccessToast,
  unAssignSuccessToast } from '../utils/toasts/index';
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
      // Arrays auxiliares
      obrasInfo: [],
      sociosInfo: [],
      selectedSociosInfo: [],
      selectedSocioByObra: [],
      // Variaveis auxiliares
      showSocios: false,
      getObraInfo: {},
      selectedSocioID: '',
      sociosNotYetAssigned: [],
      customToastNotification: 'Obra',
      // Variáveis para requisição
      codigo: '',
      nome: '',
      endereco: '',
      dataInicio: '',
      dataPrevistaFim: '',
      dataRealFim: '',
      custoMaoDeObra: '',
      custoPrevisto: '',
      obra: {},
    };
  },

  components: {
    SkeletonTableAndHeader
  },

  // O vue percebe que estou usando algumas variaveis, qualquer atualização nas variaveis, ele atualiza
  // uma variável X
  computed: {

  },

  // Pode definir um comportamento a ser chamado quando uma variável mudar.
  watch: {
    nome() {
      this.watchRequiredInsertFields();
    },
    selectedSocioID() {
      this.wacthAssignRequiredFields();
    }
  },

  props: {
    alturaMenu: Number,
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
      this.httpStatus === 403 ? this.redirectToLogin(): null;
      // this.httpStatus === 500 ? alert("Essa obra não pode ser excluída, pois possui COMPRAS vinculadas.") : null;
    },
    validateLogin () {
      !this.localStorageToken ? this.redirectToLogin() : null;
    },
    ///////////////////////////////////////////////////////////////////////////////

    // Métodos para apagar os dados que foram preenchidos e enviados ou não: -----\
    cancel () {
      this.codigo = '';
      this.nome = '';
      this.endereco = '';
      this.dataInicio = '';
      this.dataPrevistaFim = '';
      this.dataRealFim = '';
      this.custoMaoDeObra = '';
      this.custoPrevisto = '';
    },
    cancelInsert () {
      this.cancel();

      removeElementClass('insert-name-input', 'required-red-border');
      removeElementClass('insert-name-label', 'campo-obrigatorio-warning');
      setAttributeSalvarButton('salva-nova-obra-button', 'no-closing-modal');
    },
    cancelAtribuir () {
      this.selectedSocioID = '';

      removeElementClass('atribuir-socio-select', 'required-red-border');
      removeElementClass('atribuir-socio-label', 'campo-obrigatorio-warning');
      setAttributeSalvarButton('salva-nova-atribuicao-button', 'no-closing-modal');
    },
    ///////////////////////////////////////////////////////////////////////////////

    // Métodos de busca - GET: ---------------------------------------------------\
    fetchSociosInfoDB () {
      axios.get('/api/socio',
      {
        headers: {
          Authorization: this.localStorageToken
        }
      }).then(res => {
        this.sociosInfo = res.data.sort((s1, s2) => s1['nome'].localeCompare(s2['nome']));
        this.setHttpStatusCode(res.status);
      }).catch(error => {
        this.validateHttpStatus(error.response.status);
      })
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
      })
    },
    ///////////////////////////////////////////////////////////////////////////////

    // Métodos de INSERT - POST: -------------------------------------------------\
    create () {
      axios.post('/api/obra',
        {
          nome: this.nome,
          endereco: this.endereco,
          dataInicio: this.dataInicio,
          dataPrevistaFim: this.dataPrevistaFim,
          dataRealFim: this.dataRealFim,
          custoMaoDeObra: this.custoMaoDeObra,
          custoPrevisto: this.custoPrevisto
        },
        {
          headers: {
            Authorization: this.localStorageToken
          }
        }).then((res) => {
          this.fetchObrasInfoDB();
          this.setHttpStatusCode(res.status);
          insertSuccessToast(this.customToastNotification);
        }).catch(error => {
          this.validateHttpStatus(error.response.status);
          insertErrorToast(error.response.data.resposta);
        });
      this.cancel();
    },
    watchRequiredInsertFields() {
      this.nome ?
        setAttributeSalvarButton('salva-nova-obra-button', 'modal') :
        setAttributeSalvarButton('salva-nova-obra-button', 'no-closing-modal');
    },
    createInfoDB () {
      clickSavecheckRequiredInsertField(this.nome, 'insert-name-input', 'insert-name-label', 'salva-nova-obra-button');

      if (this.nome) {
        this.create();
      }
    },
    assign (codigoSocio) {
      axios.post('/api/obra/associar-socio-obra',
      {
        idSocio: codigoSocio,
        idObra: this.getObraInfo.codigo
      },
      {
        headers: {
          Authorization: this.localStorageToken
        }
      }).then(res => {
        const self = this;
        this.fetchObrasInfoDB(() => {
          self.fillObraForRequest(this.getObraInfo.codigo);
          self.fillSociosSelecionadosByObra();
          self.selectNotYetAssignedSocios();
        });
        assignSuccessToast();
      }).catch(error => {
        console.log(error);
      });
    this.cancelAtribuir();
    },
    wacthAssignRequiredFields () {
      this.selectedSocioID ?
        setAttributeSalvarButton('salva-nova-atribuicao-button', 'modal') :
        setAttributeSalvarButton('salva-nova-atribuicao-button', 'no-closing-modal');
    },
    assignSocio () {
      clickSavecheckRequiredInsertField(this.selectedSocioID, 'atribuir-socio-select', 'atribuir-socio-label', 'salva-nova-atribuicao-button');

      if (this.selectedSocioID) {
        this.assign(this.selectedSocioID);
      }
    },
    fillDesatribuirModal(codigoSocio) {
      this.selectedSocioID = codigoSocio;
    },
    unAssignSocio () {
      axios.post('/api/obra/desassociar-socio-obra',
      {
        idSocio: this.selectedSocioID,
        idObra: this.getObraInfo.codigo
      },
      {
        headers: {
          Authorization: this.localStorageToken
        }
      }).then(res => {
        const self = this;
        this.fetchObrasInfoDB(() => {
          self.fillObraForRequest(this.getObraInfo.codigo);
          self.fillSociosSelecionadosByObra();
          self.selectNotYetAssignedSocios();
        });
        unAssignSuccessToast();
      }).catch(error => {
        console.log(error);
      });
      this.cancelAtribuir();
    },
    ///////////////////////////////////////////////////////////////////////////////

    // Métodos de UPDATE - PUT: --------------------------------------------------\
    fillUpdateDeleteModal (cod, nome, endereco, dataI, dataPFim, dataRFim, custoMDO, custoP) {
      focusFirstModalInput('update-name-input');
      this.codigo = cod;
      this.nome = nome;
      this.endereco = endereco;
      this.dataInicio = dataI;
      this.dataPrevistaFim = dataPFim;
      this.dataRealFim = dataRFim;
      this.custoMaoDeObra = custoMDO;
      this.custoPrevisto = custoP;
    },
    updateInfoDB () {
      axios.put('/api/obra',
        {
          codigo: this.codigo,
          nome: this.nome,
          endereco: this.endereco,
          dataInicio: this.dataInicio,
          dataPrevistaFim: this.dataPrevistaFim,
          dataRealFim: this.dataRealFim,
          custoMaoDeObra: this.custoMaoDeObra,
          custoPrevisto: this.custoPrevisto
        },
        {
          headers: {
            Authorization: this.localStorageToken
          }
        }).then((res) => {
          this.fetchObrasInfoDB();
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
      axios.delete('/api/obra',
        {
          headers: {
            Authorization: this.localStorageToken
          },
          data: {
            codigo: Number(codigo)
          }
        }).then((res) => {
          this.fetchObrasInfoDB();
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
    // Método para limpar a lista de sócios selecionados por obra.
    clearSelectedSocioByObra () {
      this.selectedSocioByObra = [];
    },
    // Método para limpar 'this.obra' ao clicar em voltar;
    clearSelectedObra () {
      this.obra = {};
    },
    // Método para alternar a renderização de Obras e Sócios.
    // Clicando no botão de 'Sócios':
    //  - Troca o valor de 'showSocios' para True, mostrando os sócios;
    //  - Limpa os Sócios selecionados por obra para que possa repopular com os sócios corretos da nova Obra selecionada;
    //  - Limpa a Obra selecionada para que possa repopular com a nova obra selecionada.
    // Clicando no botão de 'Voltar':
    //  - Troca o valor de 'showSocios' para False, escondendo os sócios;
    //  - Apenas limpa os sócios selecionados e a Obra selecionada.
    switchObrasSocios () {
      this.showSocios = !this.showSocios;
      this.clearSelectedSocioByObra();
      this.clearSelectedObra();
    },
    // Método para preencher a obra para a atribuição de sócios.
    fillObraForRequest (cod) {
      for (let chosenObra of this.obrasInfo) {
        if (chosenObra.codigo === cod) {
          this.obra = chosenObra;
          this.getObraInfo = chosenObra;
        }
      }
    },
    // Método para preencher a lista de Sócios de acordo com a obra selecionada.
    fillSociosSelecionadosByObra () {
      this.selectedSocioByObra =  this.getObraInfo.socios;
    },
    // Métodos para gerar uma lista mais adequada para a atribuição de sócios.
    diffObjectsArray(arr1, arr2) {
      // Filtrar objetos exclusivos em arr1
      let uniqueInArr1 = arr1.filter(obj1 => !arr2.some(obj2 => obj1.codigo === obj2.codigo));

      // Filtrar objetos exclusivos em arr2
      let uniqueInArr2 = arr2.filter(obj2 => !arr1.some(obj1 => obj2.codigo === obj1.codigo));

      // Combinar os resultados
      let result = uniqueInArr1.concat(uniqueInArr2);

      return result;
    },
    selectNotYetAssignedSocios () {
      this.sociosNotYetAssigned = this.diffObjectsArray(this.sociosInfo, this.selectedSocioByObra);
    },
    // Recupera os sócios corretos para a obra selecionada.
    // Clicando no botão 'Sócios':
    //  - Limpa tudo no método 'switchObrasSocios' e troca 'showSocios' para True;
    //  - Popula a obra correta com 'fillObraForRequest';
    //  - Popula os sócios corretos daquela obra com 'fillSociosSelecionadosByObra'.
    //  - Popula os sócios ainda não atribuidos para a obra selecionada.
    getSociosForThisObra (cod) {
      this.switchObrasSocios(); 
      this.fillObraForRequest(cod);
      this.fillSociosSelecionadosByObra();
      this.selectNotYetAssignedSocios();
    },
    ///////////////////////////////////////////////////////////////////////////////

    // Métodos de renderização: --------------------------------------------------\
    brazilDate (data) {
      if (data === null) {
        return null;
      }

      let partes = data.split("-");

      return partes.length === 3 ? `${partes[2]}/${partes[1]}/${partes[0]}` : null;

    },
    fixMoney (dinheiroInt) {
      if (dinheiroInt === null) {
        return null
      } else {
        const valorFormatado = dinheiroInt.toFixed(2);
  
        // Adiciona o símbolo da moeda e separadores de milhares e decimais
        const partes = valorFormatado.toString().split('.');
        const parteInteira = partes[0].replace(/\B(?=(\d{3})+(?!\d))/g, '.');
        const parteDecimal = partes[1];
  
        return `R$${parteInteira},${parteDecimal}`;
      }
    },
    ///////////////////////////////////////////////////////////////////////////////

    // Métodos de refinamento: ---------------------------------------------------\
    HGPKEnter (event) {
      const e = event;
      const ENTER = e.key === 'Enter';

      // Recupera botões e elementos da tela.
      let body = document.getElementsByTagName('body');
      let novaObraButton = document.getElementById('nova-obra-button');
      let atribuirSocioButton = document.getElementById('atribuir-socio-button');

      let salvaNovaObra = document.getElementById('salva-nova-obra-button');
      let atualizaObra = document.getElementById('atualiza-obra-button');
      let deletaObra = document.getElementById('deleta-obra-button');

      let salvaNovaAtribuicao = document.getElementById('salva-nova-atribuicao-button');
      let desatribuir = document.getElementById('desatribuicao-button');

      // Modais e comparações se elas estão ativas ou não.
      let deleteObraModal = document.getElementById('deleteModal');
      let insertObraModal = document.getElementById('insertModal');
      let updateObraModal = document.getElementById('updateModal');

      let atribuirModal = document.getElementById('atribuirSocioModal');
      let desatribuirModal = document.getElementById('desatribuirSocioModal');
      
      const noModalOpen = body[0].classList.value === '';

      const isDeleteObraModal = deleteObraModal.classList.value === 'modal fade show';
      const isInsertObraModal = insertObraModal.classList.value === 'modal fade show';
      const isUpdateObraModal = updateObraModal.classList.value === 'modal fade show';

      const isAtribuirSocioModal = atribuirModal.classList.value === 'modal fade show';
      const isDesatribuirSocioModal = desatribuirModal.classList.value === 'modal fade show';

      // Ativa o comportamento desejado baseado no momento que o usuário está na página:
      if (noModalOpen && !this.showSocios && ENTER) {
        e.preventDefault();
        novaObraButton.click();
      } else if (!noModalOpen && !this.showSocios && isInsertObraModal && ENTER) {
        e.preventDefault();
        salvaNovaObra.click();
      } else if (!noModalOpen && !this.showSocios && isUpdateObraModal && ENTER) {
        e.preventDefault();
        atualizaObra.click();
      } else if (!noModalOpen && !this.showSocios && isDeleteObraModal && ENTER) {
        e.preventDefault();
        deletaObra.click();
      } else if (noModalOpen && this.showSocios && ENTER) {
        e.preventDefault();
        atribuirSocioButton.click();
      } else if (!noModalOpen && this.showSocios && isAtribuirSocioModal && ENTER) {
        e.preventDefault();
        salvaNovaAtribuicao.click();
      } else if (!noModalOpen && this.showSocios && isDesatribuirSocioModal && ENTER) {
        e.preventDefault();
        desatribuir.click();
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
    this.fetchObrasInfoDB();
    this.fetchSociosInfoDB();
    this.addHGPKEnter();
  },

  unmounted () {
    this.removeHGPKEnter();
  }
}
</script>

<template>

  <!-- Tabela e Header Esqueleto - Carregamento -->
  <SkeletonTableAndHeader v-if="this.obrasInfo == ''" />

  <!-- Header com o botão de + Nova Obra -->
  <header v-if="this.obrasInfo != ''" v-show="!this.showSocios" class="header middle-margin">
    <button
      id="nova-obra-button"
      type="button"
      class="btn btn-success light-green"
      data-bs-toggle="modal"
      data-bs-target="#insertModal"
      @click="focusFirstModalInput('insert-name-input')"
    >+ Nova Obra</button>
  </header>

  <!-- Botão 'Voltar' + botão '+ Atribuir sócio' -->
  <div v-if="this.showSocios" class="header middle-margin">
    <!-- Botão 'Voltar' + botão '+ Novo Item' -->
    <div class="column">
      <!-- Botão para voltar as compras -->
      <button
        type="button"
        class="btn btn-dark margin-5px"
        title="Voltar às obras"
        @click="switchObrasSocios"
      >
        Voltar
      </button>
      <!-- Botão para atribuir socio a obra. -->
      <button
        id="atribuir-socio-button"
        type="button"
        class="btn btn-success light-green margin-5px"
        data-bs-toggle="modal"
        data-bs-target="#atribuirSocioModal"
        @click="focusFirstModalInput('atribuir-socio-select')"
      >
        + Atribuir sócio
      </button>
    </div>

    <!-- Informações da Obra  -->
    <div v-if="this.showSocios" class="column">
      <h5>
        {{ getObraInfo.codigo }} - {{ getObraInfo.nome }}
      </h5>
    </div>

    <!-- div's extras que ajustam a visibilidade -->
    <div></div>
    <div></div>
  </div>

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
            id="deleta-obra-button"
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
          <h1 class="modal-title fs-5" id="insertModalLabel">Nova Obra</h1>
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
                placeholder="Projeto: "
                v-model="nome"
                maxlength="70"
                @keyup="checkInputValue(nome, 'insert-name-input')"
              >
            </div>

            <div class="mb-3">
              <label for="insert-endereco-input" class="form-label bold">Endereço:</label>
              <input
                type="text"
                class="form-control"
                id="insert-endereco-input"
                placeholder="Rua da Obra, 1000"
                v-model="endereco"
                maxlength="150">
            </div>

            <div class="mb-3">
              <label for="insert-data-inicio-input" class="form-label bold">Data início:</label>
              <input
                type="date"
                class="form-control"
                id="insert-data-inicio-input"
                placeholder=""
                v-model="dataInicio">
            </div>

            <div class="mb-3">
              <label for="insert-data-prevista-fim-input" class="form-label bold">Data entrega prevista:</label>
              <input
                type="date"
                class="form-control"
                id="insert-data-prevista-fim-input"
                placeholder=""
                v-model="dataPrevistaFim">
            </div>

            <div class="mb-3">
              <label for="insert-data-real-fim-input" class="form-label bold">Data entrega:</label>
              <input
                type="date"
                class="form-control"
                id="insert-data-real-fim-input"
                placeholder=""
                v-model="dataRealFim">
            </div>

            <div class="mb-3">
              <label for="insert-custo-mao-de-obra-input" class="form-label bold">Custo mão de obra:</label>
              <input
                type="number"
                class="form-control"
                id="insert-custo-mao-de-obra-input"
                placeholder="R$... (inserir apenas números)"
                v-model="custoMaoDeObra"
                maxlength="50">
            </div>

            <div class="mb-3">
              <label for="insert-custo-previsto-input" class="form-label bold">Custo previsto:</label>
              <input
                type="number"
                class="form-control"
                id="insert-custo-previsto-input"
                placeholder="R$... (inserir apenas números)"
                v-model="custoPrevisto"
                maxlength="50">
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
            id="salva-nova-obra-button"
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
          <h1 class="modal-title fs-5" id="updateModalLabel">Editar Obra</h1>
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
                placeholder="Nome da Obra"
                v-model="nome"
                disabled
                maxlength="70">
            </div>

            <div class="mb-3">
              <label for="update-endereco-input" class="form-label bold">Endereço:</label>
              <input
                type="text"
                class="form-control"
                id="update-endereco-input"
                placeholder="Rua da Obra, 1000"
                v-model="endereco"
                maxlength="150">
            </div>

            <div class="mb-3">
              <label for="update-data-inicio-input" class="form-label bold">Data início:</label>
              <input
                type="date"
                class="form-control"
                id="update-data-inicio-input"
                v-model="dataInicio">
            </div>

            <div class="mb-3">
              <label for="update-data-prevista-fim-input" class="form-label bold">Data entrega prevista:</label>
              <input
                type="date"
                class="form-control"
                id="update-data-prevista-fim-input"
                placeholder=""
                v-model="dataPrevistaFim">
            </div>

            <div class="mb-3">
              <label for="update-data-real-fim-input" class="form-label bold">Data entrega:</label>
              <input
                type="date"
                class="form-control"
                id="update-data-real-fim-input"
                placeholder=""
                v-model="dataRealFim">
            </div>

            <div class="mb-3">
              <label for="update-custo-mao-de-obra-input" class="form-label bold">Custo mão de obra:</label>
              <input
                type="number"
                class="form-control"
                id="update-custo-mao-de-obra-input"
                placeholder="R$... (inserir apenas números)"
                v-model="custoMaoDeObra"
                maxlength="50">
            </div>
          
            <div class="mb-3">
              <label for="update-custo-previsto-input" class="form-label bold">Custo previsto:</label>
              <input
                type="number"
                class="form-control"
                id="update-custo-previsto-input"
                placeholder="R$... (inserir apenas números)"
                v-model="custoPrevisto"
                maxlength="50">
            </div>

          </form>
        </div>

        <div class="modal-footer">
          <button type="button" class="btn btn-secondary dark-grey" data-bs-dismiss="modal"
            @click="cancel"
          >Fechar</button>

          <button
            id="atualiza-obra-button"
            type="button"
            class="btn btn-success light-green"
            data-bs-dismiss="modal"
            @click="updateInfoDB()"
          >Salvar</button>
        </div>
      </div>
    </div>
  </div>

  <!-- AtribuirSocioModal -->
  <div class="modal fade" id="atribuirSocioModal" data-bs-backdrop="static" data-bs-keyboard="false" tabindex="-1" aria-labelledby="atribuirSocioModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-dialog-scrollable">
      <div class="modal-content">
        <div class="modal-header">
          <h1 class="modal-title fs-5" id="atribuirSocioModalLabel">Atribuir Sócio</h1>
          <button @click="cancelAtribuir" type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
        </div>

        <div class="modal-body">
          <form action="POST">

            <!-- Código - Obra -->
            <div class="mb-3">
              <label for="atribuir-socio-obra-codigo" class="form-label bold">Código:</label>
              <input
                type="text"
                class="form-control"
                id="atribuir-socio-obra-codigo"
                disabled
                v-model="obra.codigo">
            </div>

            <!-- Nome - Obra -->
            <div class="mb-3">
              <label for="atribuir-socio-name-input" class="form-label bold">Nome:</label>
              <input
                type="text"
                class="form-control"
                id="atribuir-socio-name-input"
                placeholder="Nome da Obra"
                disabled
                v-model="obra.nome">
            </div>

            <!-- Sócios -->
            <div class="mb-3">
              <label id="atribuir-socio-label" for="atribuir-socio-select" class="bold">Sócio:</label>
              <select
                class="form-select"
                id="atribuir-socio-select"
                v-model="selectedSocioID"
                @click="checkInputValue(selectedSocioID, 'atribuir-socio-select')"
              ><option
                v-for="(socio, i) in sociosNotYetAssigned" :key="i" :value="socio.codigo"
              >{{ socio.nome }}</option>
              </select>
            </div>

          </form>
        </div>

        <div class="modal-footer">
          <button type="button" class="btn btn-secondary dark-grey" data-bs-dismiss="modal"
            @click="cancelAtribuir"
          >Fechar</button>

          <button
            id="salva-nova-atribuicao-button"
            type="button"
            class="btn btn-success light-green"
            @click="assignSocio(this.selectedSocioID)"
          >Salvar</button>
        </div>
      </div>
    </div>
  </div>

  <!-- DesAtribuirSocioModal -->
  <div class="modal fade" id="desatribuirSocioModal" data-bs-backdrop="static" data-bs-keyboard="false" tabindex="-1" aria-labelledby="desatribuirSocioModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-dialog-scrollable modal-sm">
      <div class="modal-content">
        <div class="modal-header">
          <h1 class="modal-title fs-5" id="desatribuirSocioModalLabel">Realmente deseja excluir?</h1>
          <button @click="cancelAtribuir" type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
        </div>

        <div class="modal-footer header">
          <button
            type="button"
            class="btn btn-secondary dark-grey"
            data-bs-dismiss="modal"
            @click="cancelAtribuir"
          >Não</button>

          <button
            id="desatribuicao-button"
            type="button"
            class="btn btn-success light-green"
            data-bs-dismiss="modal"
            @click="unAssignSocio()"
          >Sim</button>
        </div>
      </div>
    </div>
  </div>

  <!-- Tabela Obras -->
  <main v-if="this.obrasInfo != ''" v-show="!showSocios" class="middle-margin table-responsive">
    <table class="table table-hover">
      <thead>
        <tr>
          <th scope="col">Código</th>
          <th scope="col">Nome</th>
          <th scope="col">Endereço</th>
          <th scope="col">Data início</th>
          <th scope="col">Data entrega prevista</th>
          <th scope="col">Data entrega</th>
          <th scope="col">Custo mão de obra</th>
          <th scope="col">Custo previsto</th>
          <th></th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="(obra, i) in obrasInfo" :key="i">
          <th scope="row">{{ obra.codigo }}</th>
          <td>{{ obra.nome }}</td>
          <td>{{ obra.endereco }}</td>
          <td>{{ brazilDate(obra.dataInicio) }}</td>
          <td>{{ brazilDate(obra.dataPrevistaFim) }}</td>
          <td>{{ brazilDate(obra.dataRealFim) }}</td>
          <td>{{ fixMoney(obra.custoMaoDeObra) }}</td>
          <td>{{ fixMoney(obra.custoPrevisto) }}</td>
          <td class="editar-excluir">
            <button
              type="button"
              class="btn btn-light btn-sm small"
              title="Editar"
              data-bs-toggle="modal"
              data-bs-target="#updateModal"
              @click="fillUpdateDeleteModal(obra.codigo, obra.nome, obra.endereco, obra.dataInicio,
                obra.dataPrevistaFim, obra.dataRealFim, obra.custoMaoDeObra, obra.custoPrevisto)"
            ><img src="../assets/imagens/editar.png" alt="lata de lixo"></button>
            <button
              type="button"
              class="btn btn-light btn-sm small"
              title="Sócios"
              @click="getSociosForThisObra(obra.codigo)"
            ><img src="../assets/imagens/perfil-preto.jpg" alt="socios"></button>
            <button
              type="button"
              class="btn btn-light btn-sm small"
              title="Excluir"
              data-bs-toggle="modal"
              data-bs-target="#deleteModal"
              @click="fillUpdateDeleteModal(obra.codigo)"
            ><img src="../assets/imagens/lata-de-lixo.png" alt="lata de lixo"></button>
          </td>
        </tr>
      </tbody>
    </table>
  </main>

  <!-- Tabela Sócios atribuidos -->
  <main v-show="showSocios" class="middle-margin table-responsive">
    <table class="table table-hover">
      <thead>
        <tr>
          <th scope="col">Código</th>
          <th scope="col">Nome</th>
          <th></th>
          <th></th>
          <th></th>
          <th></th>
          <th></th>
          <th></th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="(socio, i) in selectedSocioByObra" :key="i">
          <th scope="row">{{ socio.codigo }}</th>
          <td>{{ socio.nome }}</td>
          <td></td>
          <td></td>
          <td></td>
          <td></td>
          <td></td>
          <td class="editar-excluir">
            <button
              type="button"
              class="btn btn-light btn-sm small"
              title="Excluir"
              data-bs-toggle="modal"
              data-bs-target="#desatribuirSocioModal"
              @click="fillDesatribuirModal(socio.codigo)"
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

.column {
  display: flex;
  justify-content: space-between;
  flex-direction: column;
  padding-bottom: 5px;
}

.red-border {
  border: 1px solid red;
}

.margin-5px {
  margin-bottom: 5px;
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