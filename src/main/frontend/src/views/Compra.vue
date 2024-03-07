<script>

import { useRouter } from 'vue-router';
import axios from 'axios';
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
      // Arrays auxiliares
      comprasInfo: [],
      obrasInfo: [],
      fornecedoresInfo: [],
      itensCompraInfo: [],
      produtosInfo: [],
      localUsoInfo: [],
      unidadeMedidaInfo: [],
      sociosInfo: [],
      selectedSociosInfo: [],
      selectedComprasByObra: [],
      selectedItensByCompra: [],
      // Variáveis auxiliares
      useRouter: useRouter(),
      httpStatus: '',
      selectedObraNome: '',
      selectedObraId: '',
      nomeObra: '',
      selectedFornecedorNome: '',
      selectedFornecedorCod: '',
      selectedProdutoNome: '',
      selectedProdutoCod: '',
      selectedLocalUsoNome: '',
      selectedLocalUsoCod: '',
      selectedUnidadeMedida: '',
      selectedUnidadeMedidaCod: '',
      selectedSocioNome: '',
      selectedSocioCod: '',
      selectedCompraID: '',
      getCompraInfo: {}, // Apenas informações da compra para serem usadas na página.
      compraCodForne: '',
      valorTotalCompra: 0,
      showItems: false,
      valueStatus: '',
      compraCustomToast: 'Compra',
      itemCustomToast: 'Item',
      // Variáveis para requisição
      localStorageToken: getLocalStorageToken(),
      codigo: '',
      obra: {},
      fornecedor: {},
      socio: {},
      dataCompra: '',
      dataEntrega: '',
      dataPagamento: '',
      dataVencimento: '',
      valorOriginal: '',
      valorDesconto: '',
      valorFinal: '',
      codigoItem: '',
      produto: {},
      localUso: {},
      compra: {},
      unidadeMedida: {},
      quantidade: '',
      valorUnitario: '',
      valorTotal: ''
    }
  },

  watch: {
    selectedFornecedorCod() {
      this.watchRequiredInsertFields();
    },
    selectedSocioCod() {
      this.watchRequiredInsertFields();
    },
    valorOriginal() {
      this.watchRequiredInsertFields();
    },
    selectedProdutoCod() {
      this.watchRequiredInsertItemsFields();
    },
    selectedLocalUsoCod() {
      this.watchRequiredInsertItemsFields();
    },
    selectedUnidadeMedidaCod() {
      this.watchRequiredInsertItemsFields();
    }
  },

  props: {
    alturaMenu: Number,
  },

  components: {
    SkeletonTableAndHeader
  },

  methods: {
    // Validações de Login:--------------------------------------------------------------\
    // Redireciona para a página de login.
    redirectToLogin () {
      this.useRouter.push('/login');
    },
    // Seta o 'this.httpStatus' com os casos de sucesso e erro.
    setHttpStatusCode (succesError) {
      this.httpStatus = succesError;
    },
    // Valida o login baseado no token.
    validateLogin () {
      !this.localStorageToken ? this.redirectToLogin() : null;
    },
    // Valida o StatusHttp da requisição. Casos de token expirado.
    validateHttpStatus (status) {
      this.setHttpStatusCode(status);
      this.httpStatus === 403 ? this.redirectToLogin(): null;
    },
    //////////////////////////////////////////////////////////////////////////////////////


    // Métodos para apagar dados que foram preenchidos e enviados ou não enviados: ------\
    cancel () {
      this.selectedFornecedorNome = '';
      this.selectedFornecedorCod = '';
      this.selectedSocioNome = '';
      this.selectedSocioCod = '';
      this.fornecedor = {};
      this.socio = {};
      this.dataCompra = '';
      this.dataEntrega = '';
      this.dataPagamento = '';
      this.dataVencimento = '';
      this.valorOriginal = '';
      this.valorDesconto = '';
      this.valorFinal = '';
    },
    cancelInsert () {
      this.cancel();

      removeElementClass('insert-compra-fornecedor-select', 'required-red-border');
      removeElementClass('insert-compra-fornecedor-label', 'campo-obrigatorio-warning');
      removeElementClass('insert-compra-socio-pagador-select', 'required-red-border');
      removeElementClass('insert-compra-socio-pagador-label', 'campo-obrigatorio-warning');
      removeElementClass('insert-compra-valor-original-input', 'required-red-border');
      removeElementClass( 'insert-compra-valor-original-label', 'campo-obrigatorio-warning');
      setAttributeSalvarButton('salvaNovaCompra', 'no-closing-modal');
    },
    cancelItem() {
      this.selectedProdutoNome = '';
      this.selectedProdutoCod = '';
      this.selectedLocalUsoNome = '';
      this.selectedLocalUsoCod = '';
      this.selectedUnidadeMedida = '';
      this.selectedUnidadeMedidaCod = '';
      // this.compra = {};
      this.unidadeMedida = {};
      this.produto = {};
      this.localUso = {};
      this.quantidade = '';
      this.valorUnitario = '';
      this.valorTotal = '';
    },
    cancelInsertItem() {
      this.cancelItem();

      removeElementClass('insert-item-produto-select', 'required-red-border');
      removeElementClass('insert-item-produto-label', 'campo-obrigatorio-warning');
      removeElementClass('insert-item-local-uso-select', 'required-red-border');
      removeElementClass('insert-item-local-uso-label', 'campo-obrigatorio-warning');
      removeElementClass('insert-item-unidade-medida-select', 'required-red-border');
      removeElementClass( 'insert-item-unidade-medida-label', 'campo-obrigatorio-warning');
      setAttributeSalvarButton('salvaNovoItem', 'no-closing-modal');
    },
    //////////////////////////////////////////////////////////////////////////////////////


    // Busca de dados - GET: ------------------------------------------------------------\
    // GET - Compras.
    fetchComprasInfoDB (callback) {
      axios.get("/api/compra",
      {
        headers: {
          Authorization: this.localStorageToken
        }
      }).then(res => {
        this.comprasInfo = res.data.sort((s1, s2) => s2.codigo - s1.codigo);
        this.setHttpStatusCode(res.status);
        if (callback) callback();
      }).catch(error => {
        this.validateHttpStatus(error.response.status);
      });
    },
    // GET - Obras.
    fetchObrasInfoDB (callback) {
      axios.get("/api/obra",
      {
        headers: {
          Authorization: this.localStorageToken
        }
      }).then(res => {
        this.obrasInfo = res.data.sort((s1, s2) => s1.codigo - s2.codigo)
        this.setHttpStatusCode(res.status);
        if (callback) callback();
      }).catch(error => {
        this.validateHttpStatus(error.response.status);
      });
    },
    // GET - Fornecedores.
    fetchFornecedoresInfoDB () {
      axios.get("/api/fornecedor",
      {
        headers: {
          Authorization: this.localStorageToken
        }
      }).then(res => {
        this.fornecedoresInfo = res.data.sort((s1, s2) => s1['nome'].localeCompare(s2['nome']));
        this.setHttpStatusCode(res.status);
      }).catch(error => {
        this.validateHttpStatus(error.response.status);
      })
    },
    // GET - Itens das compras.
    fetchItensCompraInfoDB (callback) {
      axios.get("/api/itemcompra",
      {
        headers: {
          Authorization: this.localStorageToken
        }
      }).then(res => {
        this.itensCompraInfo = res.data.sort((s1, s2) => s1.codigo - s2.codigo)
        this.setHttpStatusCode(res.status);
        // 1. if (callback) callback(): Verifica se uma função de retorno de chamada foi
        //    fornecida como argumento da função. Se uma 'callback' foi fornecida, ela é
        //    chamada aqui. Permitindo que quem chame a função 'fetchItensCompraInfoDB'
        //    especifique uma função a ser executada após a conclusa da busca e da
        //    ordenação dos itens de compra.
        if (callback) callback();
      }).catch(error => {
        this.validateHttpStatus(error.response.status);
      });
    },
    // GET - Produtos.
    fetchProdutosInfoDB() {
      axios.get("/api/produto",
      {
        headers: {
          Authorization: this.localStorageToken
        }
      }).then(res => {
        this.produtosInfo = res.data.sort((s1, s2) => s1['nome'].localeCompare(s2['nome']))
        this.setHttpStatusCode(res.status);
      }).catch(error => {
        this.validateHttpStatus(error.response.status);
      })
    },
    // GET - LocalUso.
    fetchLocalUsoInfoDB () {
      axios.get("/api/localuso",
      {
        headers: {
          Authorization: this.localStorageToken
        }
      }).then(res => {
        this.localUsoInfo = res.data.sort((s1, s2) => s1['nomeLocalUsoObra'].localeCompare(s2['nomeLocalUsoObra']))
        this.setHttpStatusCode(res.status);
      }).catch(error => {
        this.validateHttpStatus(error.response.status);
      });
    },
    // GET - UnidadeMedida.
    fetchUnidadeMedidaInfoDB () {
      axios.get("/api/unidademedida",
      {
        headers: {
          Authorization: this.localStorageToken
        }
      }).then(res => {
        this.unidadeMedidaInfo = res.data.sort((s1, s2) => s1['unidade'].localeCompare(s2['unidade']));
        this.setHttpStatusCode(res.status);
      }).catch(error => {
        this.validateHttpStatus(error.response.status);
      });
    },
    // GET - Sócios.
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
    //////////////////////////////////////////////////////////////////////////////////////


    // Métodos de comportamento: --------------------------------------------------------\
    // Esvazia o array de COMPRAS por OBRA.
    emptySelectedComprasByObraArray () {
      this.selectedComprasByObra = [];
    },
    // Popula o array de COMPRAS por OBRA.
    selectComprasByObra () {
      for (let chosenCompra of this.comprasInfo) {
        if (chosenCompra.obra.nome === this.selectedObraNome) {
          this.selectedComprasByObra.push(chosenCompra);
        }
      }
    },
    // Seta as chaves 'nomeObra' e 'codigoObra' no Local Storage.
    setObraInfoLocalStorage (nomeObra, codigoObra) {
      localStorage.setItem('nomeObra', nomeObra);
      localStorage.setItem('codigoObra', codigoObra);
    },
    // Seleciona 'this.selectedObraNome' e 'this.selectedCodigoObra'.
    selectObra (nomeObra, codigoObra) {
      this.selectedObraNome = nomeObra;
      this.selectedObraId = codigoObra;
      this.setObraInfoLocalStorage(nomeObra, codigoObra);
    },
    // Preenche o objeto "this.obra" com a obra correta para comportamentos da página.
    fillObraForBehavior () {
      for (let chosenObra of this.obrasInfo) {
        if (chosenObra.nome === this.selectedObraNome) {
          this.obra = chosenObra;
        }
      }
    },
    // Preenche o Array 'this.selectedSociosInfo' para popular os sócios corretos de cada obra.
    selectSociosInfoByObra () {
      this.fillObraForBehavior();
      this.selectedSociosInfo = [];
      this.selectedSociosInfo = this.obra.socios;
    },
    // Renderiza a lista de Compras por Obra selecionada.
    obrasDropDownActions (nomeObra, codigoObra) {
      this.selectObra(nomeObra, codigoObra);
      this.selectSociosInfoByObra();
      this.emptySelectedComprasByObraArray();
      this.selectComprasByObra();
    },
    // Limpa 'this.selectedCompraID' as retornar para a tela de COMPRAS.
    clearSelectedCompraID () {
      this.selectedCompraID = '';
    },
    // Limpa a lista de ITENS selecionados por COMPRA.
    clearSelectedItensByCompra () {
      this.selectedItensByCompra = [];
    },
    // Preenche a variavel 'selectedCompraID' para permitir renderizar seus itens.
    selectCompraID (cod) {
      this.selectedCompraID = cod;
    },
    // Popula o array 'selectedItensByCompra'.
    selectItensByCompra () {
      for (let item of this.itensCompraInfo) {
        if (item.compra.codigo === this.selectedCompraID) {
          this.selectedItensByCompra.push(item);
        }
      }
      // Ordena por nome de produto
      this.selectedItensByCompra.sort((s1, s2) => s1.produto['nome'].localeCompare(s2.produto['nome']))
    },
    // Recupera os itens corretos para a compra selecionada.
    getItensForThisCompra (cod) {
      this.switchItensCompras();
      this.selectCompraID(cod);
      this.fillCompraInfo();
      this.selectItensByCompra();
      this.sumValorTotalCompra();
    },
    //////////////////////////////////////////////////////////////////////////////////////


    // Métodos de CREATE/INSERT - POST: -------------------------------------------------\
    // Insere NOVA COMPRA.
    createCompra (callback) {
      axios.post("/api/compra",
        {
          obra: { codigo: this.selectedObraId },
          fornecedor: { codigo: this.selectedFornecedorCod },
          socio: { codigo: this.selectedSocioCod },
          dataCompra: this.dataCompra,
          dataEntrega: this.dataEntrega,
          dataPagamento: this.dataPagamento,
          dataVencimento: this.dataVencimento,
          valorOriginal: this.valorOriginal,
          valorDesconto: this.valorDesconto,
          valorFinal: this.valorFinal
        },
        {
          headers: {
            Authorization: this.localStorageToken
          }
        }
      ).then(res => {
        if (callback) callback();
        insertSuccessToast(this.compraCustomToast);
      }).catch(error => {
        this.validateHttpStatus(error.response.status)
        insertErrorToast(this.compraCustomToast);
      });
    },
    // Chama o método 'createCompra()', repopula a lista correta e reseta os dados de requisição.
    createC () {
      const self = this;
      this.createCompra(() => {
        self.fetchComprasInfoDB(() => {
          self.emptySelectedComprasByObraArray();
          self.selectComprasByObra();
        })
      });
      this.cancel();
    },
    watchRequiredInsertFields() {
      this.selectedFornecedorCod && this.selectedSocioCod && this.valorOriginal ?
        setAttributeSalvarButton('salvaNovaCompra', 'modal') :
        setAttributeSalvarButton('salvaNovaCompra', 'no-closing-modal');
    },
    createCompraInfoDB () {
      clickSavecheckRequiredInsertField(this.selectedFornecedorCod, 'insert-compra-fornecedor-select', 'insert-compra-fornecedor-label', 'salvaNovaCompra');
      clickSavecheckRequiredInsertField(this.selectedSocioCod, 'insert-compra-socio-pagador-select', 'insert-compra-socio-pagador-label', 'salvaNovaCompra');
      clickSavecheckRequiredInsertField(this.valorOriginal, 'insert-compra-valor-original-input', 'insert-compra-valor-original-label', 'salvaNovaCompra');

      if (this.selectedFornecedorCod && this.selectedSocioCod && this.valorOriginal) {
        this.createC();
      }
    },
    // Item: ----------------------------------------------------------------------------|
    // Insere NOVO ITEM a uma compra.
    createItem (callback) {
      axios.post("/api/itemcompra",
        {
          compra: { codigo: this.selectedCompraID },
          produto: { codigo: this.selectedProdutoCod },
          localUso: { codigoLocalUsoObra: this.selectedLocalUsoCod },
          unidadeMedida: { codigo: this.selectedUnidadeMedidaCod },
          quantidade: this.quantidade,
          valorUnitario: this.valorUnitario,
          valorTotal: this.valorTotal
        },
        {
          headers: {
            Authorization: this.localStorageToken
          }
        }).then(res => {
          if (callback) callback();
          insertSuccessToast(this.itemCustomToast);
        }).catch(error => {
          this.validateHttpStatus(error.response.status);
          insertErrorToast(this.itemCustomToast);
        });
    },
    // Chama o método 'createItem()', repopula a lista correta e reseta os dados da requisição.
    createI () {
      const self = this;
      this.createItem(() => {
        self.fetchItensCompraInfoDB(() => {
          self.clearSelectedItensByCompra();
          self.selectItensByCompra();
          self.sumValorTotalCompra();
        });
      });
      this.cancelItem();
    },
    watchRequiredInsertItemsFields () {
      this.selectedProdutoCod, this.selectedLocalUsoCod, this.selectedUnidadeMedidaCod ?
        setAttributeSalvarButton('salvaNovoItem', 'modal') :
        setAttributeSalvarButton('salvaNovoItem', 'no-closing-modal');
    },
    createItemInfoDB () {
      clickSavecheckRequiredInsertField(this.selectedProdutoCod, 'insert-item-produto-select', 'insert-item-produto-label', 'salvaNovoItem');
      clickSavecheckRequiredInsertField(this.selectedLocalUsoCod, 'insert-item-local-uso-select', 'insert-item-local-uso-label', 'salvaNovoItem');
      clickSavecheckRequiredInsertField(this.selectedUnidadeMedidaCod, 'insert-item-unidade-medida-select', 'insert-item-unidade-medida-label', 'salvaNovoItem');

      if (this.selectedProdutoCod, this.selectedLocalUsoCod, this.selectedUnidadeMedidaCod) {
        this.createI();
      }
    },
    //////////////////////////////////////////////////////////////////////////////////////


    // Métodos de UPDATE - PUT: ---------------------------------------------------------\
    // Preenche a modal UPDATE de COMPRA.
    fillUpdateCompraModal (cod, codigoObra, dataC, dataE, dataP, dataV, valorO, valorD, valorF, selForneNome, selSocioNome) {
      focusFirstModalInput('update-compra-socio-pagador-select');
      this.codigo = cod;
      this.selectedObraId = codigoObra;
      this.dataCompra = dataC;
      this.dataEntrega = dataE;
      this.dataPagamento = dataP;
      this.dataVencimento = dataV;
      this.valorOriginal = valorO;
      this.valorDesconto = valorD;
      this.valorFinal = valorF;
      this.selectedFornecedorNome = selForneNome;
      this.selectedSocioNome = selSocioNome;
      this.checkTipoFornecedor();
    },
    // Seta os códigos de Sócio e Fornecedor corretos escolhidos na modal.
    getCodigoSocioForCompraUpdate () {
      for (let socio of this.sociosInfo) {
        if (socio.nome === this.selectedSocioNome) {
          this.selectedSocioCod = socio.codigo;
        }
      }
    },
    getCodigoFornecedorForCompraUpdate () {
      for (let fornecedor of this.fornecedoresInfo) {
        if (fornecedor.nome === this.selectedFornecedorNome) {
          this.selectedFornecedorCod = fornecedor.codigo;
        }
      }
      this.checkTipoFornecedor();
    },
    // Atualiza COMPRA.
    updateCompra (callback) {
      axios.put("/api/compra",
      {
        codigo: this.codigo,
        obra: { codigo: this.selectedObraId },
        fornecedor: { codigo: this.selectedFornecedorCod },
        socio: { codigo: this.selectedSocioCod },
        dataCompra: this.dataCompra,
        dataEntrega: this.dataEntrega,
        dataPagamento: this.dataPagamento,
        dataVencimento: this.dataVencimento,
        valorOriginal: this.valorOriginal,
        valorDesconto: this.valorDesconto,
        valorFinal: this.valorFinal
      },
      {
        headers: {
          Authorization: this.localStorageToken
        }
      }).then(res => {
        if (callback) callback();
        updateSuccessToast(this.compraCustomToast);
      }).catch(error => {
        this.validateHttpStatus(error.response.status);
        updateErrorToast(this.compraCustomToast);
      });
    },
    // Chama o método 'updateCompra', repopula a lista corretamente e reseta os dados da requisição.
    updateCompraInfoDB () {
      this.getCodigoSocioForCompraUpdate();
      this.getCodigoFornecedorForCompraUpdate();
      const self = this;
      this.updateCompra(() => {
        self.fetchComprasInfoDB(() => {
          self.emptySelectedComprasByObraArray();
          self.selectComprasByObra();
        });
      });
      this.cancel();
    },
    // Item: ----------------------------------------------------------------------------|
    // Preenche a modal UPDATE de ITEM.
    fillUpdateItemModal (cod, qnt, valorU, valorT, selProdNome, selProdCod, selLocUsoNome, selUniMedida) {
      focusFirstModalInput('update-item-local-uso-select');
      this.codigoItem = cod;
      this.quantidade = qnt;
      this.valorUnitario = valorU;
      this.valorTotal = valorT;
      this.selectedProdutoNome = selProdNome;
      this.selectedProdutoCod = selProdCod;
      this.selectedLocalUsoNome = selLocUsoNome;
      this.selectedUnidadeMedida = selUniMedida;
    },
    // Seta os códigos de Local Uso e Unidade de Medida corretos para a requisição.
    getCodigoLocalUsoForItemUpdate () {
      for (let localUso of this.localUsoInfo) {
        if (localUso.nomeLocalUsoObra === this.selectedLocalUsoNome) {
          this.selectedLocalUsoCod = localUso.codigoLocalUsoObra;
        }
      }
    },
    getCodigoUnidadeMedidaForItemUpdate () {
      for (let unidadeMedida of this.unidadeMedidaInfo) {
        if (unidadeMedida.unidade === this.selectedUnidadeMedida) {
          this.selectedUnidadeMedidaCod = unidadeMedida.codigo;
        }
      }
    },
    // Atualiza o ITEM selecionado.
    updateItem (callback) {
      axios.put("/api/itemcompra",
      {
        codigo: this.codigoItem,
        compra: { codigo: this.selectedCompraID },
        produto: { codigo: this.selectedProdutoCod },
        localUso: { codigoLocalUsoObra: this.selectedLocalUsoCod },
        unidadeMedida: { codigo: this.selectedUnidadeMedidaCod },
        quantidade: this.quantidade,
        valorUnitario: this.valorUnitario,
        valorTotal: this.valorTotal
      },
      {
        headers: {
          Authorization: this.localStorageToken
        }
      }).then(res => {
        if (callback) callback();
        updateSuccessToast(this.itemCustomToast);
      }).catch(error => {
        this.validateHttpStatus(error.response.status);
        updateErrorToast(this.itemCustomToast);
      });
    },
    // Chama o método 'updateItem' e repopula a lista corretamente.
    updateItemInfoDB () {
      this.getCodigoLocalUsoForItemUpdate();
      this.getCodigoUnidadeMedidaForItemUpdate();
      const self = this;
      this.updateItem(() => {
        self.fetchItensCompraInfoDB(() => {
          self.clearSelectedItensByCompra();
          self.selectItensByCompra();
          self.sumValorTotalCompra();
        });
      });
      this.cancelItem();
    },
    //////////////////////////////////////////////////////////////////////////////////////


    // Métodos de DELETE - DELETE: ------------------------------------------------------\
    // Preenche a modal de DELETE de COMPRA.
    fillDeleteCompraModal(cod) {
      this.codigo = cod;
    },
    // Deleta COMPRA.
    removeCompra (codigo, callback) {
      axios.delete("/api/compra",
        {
          headers: {
            Authorization: this.localStorageToken
          },
          data: {
            codigo: Number(codigo)
          }
        }).then(res => {
          if (callback) callback();
          deleteSuccessToast(res.data);
        }).catch(error => {
          this.validateHttpStatus(error.response.status);
          deleteErrorToast(error.response.data.resposta);
        });
    },
    // Chama o método 'removeCompra', repopula a lista correta e reseta os dados da requisição.
    removeCompraFromDB (codigo) {
      let self = this;
      this.removeCompra(codigo, () => {
        self.fetchComprasInfoDB(() => {
          self.emptySelectedComprasByObraArray();
          self.selectComprasByObra();
        });
      });
      this.cancel();
    },
    // Item: ----------------------------------------------------------------------------|
    // Preenche a modal de DELETE de ITEM.
    fillDeleteItemModal (cod) {
      this.codigoItem = cod;
    },
    // Deleta ITEM.
    removeItem (cod, callback) {
      axios.delete("/api/itemcompra",
        {
          headers: {
            Authorization: this.localStorageToken
          },
          data: {
            codigo: Number(cod)
          }
        }).then(res => {
          if (callback) callback();
          deleteSuccessToast(res.data);
        }).catch(error => {
          this.validateHttpStatus(error.response.status);
          deleteErrorToast(error.response.status.resposta);
        });
    },
    // Chama o método 'removeItem', repopula a lista correta e reseta os dados da requisição.
    removeItemFromDB (cod) {
      let self = this;
      this.removeItem(cod, () => {
        self.fetchItensCompraInfoDB(() => {
          self.clearSelectedItensByCompra();
          self.selectItensByCompra();
          self.sumValorTotalCompra();
        });
      });
      this.cancelItem();
    },
    //////////////////////////////////////////////////////////////////////////////////////

    
    // Métodos de renderização: --------------------------------------------------------\
    // Alterna a renderização entre COMPRAS e ITENS.
    switchItensCompras () {
      this.showItems = !this.showItems;
      this.clearSelectedCompraID();
      this.clearSelectedItensByCompra();
    },
    // Preenche o 'this.getCompraInfo' para usar informações na página.
    fillCompraInfo () {
      for (let chosenCompra of this.comprasInfo) {
        if (chosenCompra.codigo === this.selectedCompraID) {
          this.getCompraInfo = chosenCompra;
        }
      }
    },
    // Verifica o tipo de fornecedor para renderizar as DATAS de acordo.
    checkTipoFornecedor () {
      checkInputValue(this.selectedFornecedorCod, 'insert-compra-fornecedor-select');
      for (let forne of this.fornecedoresInfo) {
        if (forne.codigo === this.selectedFornecedorCod | forne.nome === this.selectedFornecedorNome) {
          this.fornecedor = forne;
        }
      }
    },
    // Preenche o 'this.compraCodForne' para a VISUALIZAÇÃO no momento de inserção de novo Item.
    fillCompraCodForne () {
      this.compraCodForne = `${this.getCompraInfo.codigo} - ${this.getCompraInfo.fornecedor.nome}`;
      focusFirstModalInput('insert-item-produto-select');
    },
    // Renderiza apenas o primeiro e ultimo nome do sócio.
    firstLastName (nomeSocio) {
      const partes = nomeSocio.split(' ');

      return `${partes[0]} ${partes[partes.length - 1]}`
    },
    // Apresenta valores monetários BRL corretamente.
    fixCurrency (dinheiroDouble) {
      if (dinheiroDouble === null) {
        return null
      } else {
        const valorFormatado = dinheiroDouble.toLocaleString('pt-BR', {
          style: 'currency',
          currency: 'BRL'
        });

        return valorFormatado;
      }
    },
    // Ajusta para data brasileira.
    brazilDate (data) {
      if (data === null) {
        return null
      }

      let partes = data.split("-");

      if (partes.length === 3) {
          return `${partes[2]}/${partes[1]}/${partes[0]}`;
      } else {
          return null;
      }
    },
    //////////////////////////////////////////////////////////////////////////////////////
 

    // Métodos de refinamento: ----------------------------------------------------------\
    // HGPK = Handle Global Press Key: Lida com os cliques de ENTER na página.
    HGPKEnter (event) {
      // Confere se o botão apertado foi o 'ENTER' 
      const e = event;
      const ENTER = e.key === 'Enter';

      // Recupera botões e elementos da página.
      let body = document.getElementsByTagName('body');
      let novaCompraButton = document.getElementById('novaCompraButton');
      let novoItemButton = document.getElementById('novoItemButton');

      let salvaNovaCompra = document.getElementById('salvaNovaCompra');
      let atualizaCompra = document.getElementById('atualizaCompra');
      let deletaCompra = document.getElementById('deletaCompra');

      let salvaNovoItem = document.getElementById('salvaNovoItem');
      let atualizaItem = document.getElementById('atualizaItem');
      let deletaItem = document.getElementById('deletaItem');

      // Modais e comparações se elas estão ativas ou não.
      let deleteCompraModal = document.getElementById('deleteModal');
      let insertCompraModal = document.getElementById('insertModal');
      let updateCompraModal = document.getElementById('updateModal');

      let deleteItemModal = document.getElementById('deleteItemModal');
      let insertItemModal = document.getElementById('insertItemModal');
      let updateItemModal = document.getElementById('updateItemModal');

      const noModalOpen = body[0].classList.value === '';

      const isDeleteCompraModal = deleteCompraModal.classList.value === 'modal fade show';
      const isInsertCompraModal = insertCompraModal.classList.value === 'modal fade show';
      const isUpdateCompraModal = updateCompraModal.classList.value === 'modal fade show';

      const isDeleteItemModal = deleteItemModal.classList.value === 'modal fade show';
      const isInsertItemModal = insertItemModal.classList.value === 'modal fade show';
      const isUpdateItemModal = updateItemModal.classList.value === 'modal fade show';

      // Ativa o comportamento desejado baseado no momento que o usuário está na página:
      if (noModalOpen && !this.showItems && ENTER) {
        e.preventDefault();
        novaCompraButton.click();
      } else if (!noModalOpen && isInsertCompraModal && !this.showItems && ENTER) {
        e.preventDefault();
        salvaNovaCompra.click();
      } else if (!noModalOpen && isUpdateCompraModal && !this.showItems && ENTER) {
        e.preventDefault();
        atualizaCompra.click();
      } else if (!noModalOpen && isDeleteCompraModal && !this.showItems && ENTER) {
        e.preventDefault();
        deletaCompra.click();
      } else if (noModalOpen && this.showItems && ENTER) {
        e.preventDefault();
        novoItemButton.click();
      } else if (!noModalOpen && isInsertItemModal && this.showItems && ENTER) {
        e.preventDefault();
        salvaNovoItem.click();
      } else if (!noModalOpen && isUpdateItemModal && this.showItems && ENTER) {
        e.preventDefault();
        atualizaItem.click();
      } else if (!noModalOpen && isDeleteItemModal && this.showItems && ENTER) {
        e.preventDefault();
        deletaItem.click();
      }
    },
    addHGPKEnter () {
      window.addEventListener('keydown', this.HGPKEnter);
    },
    removeHGPKEnter () {
      window.removeEventListener('keydown', this.HGPKEnter);
    },
    // Soma o valor total dos itens de uma compra.
    sumValorTotalCompra () {
      this.valorTotalCompra = 0;
      for (let item of this.selectedItensByCompra) {
        this.valorTotalCompra += item.valorTotal
      }

      this.checkValueStatus();
    },
    // Calcula valor final ao inserir valor original e desconto no momento de inserir uma nova compra.
    calculaValorFinalCompra () {
      if (!this.valorDesconto) {
        this.valorDesconto = 0;
      }

      this.valorFinal = this.valorOriginal - this.valorDesconto
    },
    // Calcula o valor final do item.
    calculaValorItem() {
      !this.valorUnitario ? '' : this.valorTotal = this.quantidade * this.valorUnitario;
    },
    // Verifica se a soma dos itens passa do total da compra.
    checkValueStatus () {
      if (this.getCompraInfo.valorOriginal.toFixed(2) > this.valorTotalCompra.toFixed(2)) {
        this.valueStatus = 'grey-letter';
      } else if (this.getCompraInfo.valorOriginal.toFixed(2) < this.valorTotalCompra.toFixed(2)) {
        this.valueStatus = 'red-letter';
      } else {
        this.valueStatus = 'green-letter';
      }
    },
    focusFirstModalInput,
    checkInputValue,
    //////////////////////////////////////////////////////////////////////////////////////

  },

  mounted () {
    this.validateLogin();
    this.fetchComprasInfoDB(() => {
      this.obrasDropDownActions(
        localStorage.getItem('nomeObra') ? localStorage.getItem('nomeObra') : '',
        localStorage.getItem('codigoObra') ? localStorage.getItem('codigoObra') : 1
      );
    });
    this.fetchObrasInfoDB(() => {
      this.obrasDropDownActions(
        localStorage.getItem('nomeObra') ? localStorage.getItem('nomeObra') : '',
        localStorage.getItem('codigoObra') ? localStorage.getItem('codigoObra') : 1
      );
    });
    this.fetchFornecedoresInfoDB();
    this.fetchItensCompraInfoDB();
    this.fetchProdutosInfoDB();
    this.fetchLocalUsoInfoDB();
    this.fetchUnidadeMedidaInfoDB();
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
  <SkeletonTableAndHeader v-if="this.comprasInfo == ''" />
  
  <!-- Header com o DropDown 'Obras' -->
  <header v-if="this.comprasInfo != ''" v-show="!this.showItems" class="header middle-margin">
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
  </header>

  <!-- Botão 'Voltar' + informações + botão 'Novo Item' -->
  <div class="header middle-margin">
    <!-- Botão 'Voltar' + botão '+ Novo Item' -->
    <div v-show="this.showItems" class="column">
      <!-- Botão para voltar as compras -->
      <button
        type="button"
        class="btn btn-dark"
        title="Voltar às compras"
        @click="switchItensCompras"
      >
        Voltar
      </button>
      <!-- Botão para adicionar Novo Item à Compra. -->
      <button
        id="novoItemButton"
        @click="fillCompraCodForne"
        type="button"
        class="btn btn-success light-green"
        data-bs-toggle="modal"
        data-bs-target="#insertItemModal"
      >
        + Novo item
      </button>
    </div>

    <!-- Informações da compra  -->
    <div v-if="this.showItems" class="column">
      <h5>
        {{ getCompraInfo.codigo }} - {{ getCompraInfo.fornecedor.nome }}
      </h5>

      <div>
        <!-- Se tiver desconto -->
        <h6 v-if="this.getCompraInfo.valorDesconto">
          Valor da Compra: {{ fixCurrency(getCompraInfo.valorOriginal) }}
          - {{ fixCurrency(getCompraInfo.valorDesconto) }}
          = {{ fixCurrency(getCompraInfo.valorFinal) }}
        </h6>
        <!-- Se NÃO tiver DESCONTO -->
        <h6 v-if="!this.getCompraInfo.valorDesconto">
          Valor da Compra: {{ fixCurrency(getCompraInfo.valorOriginal) }}
        </h6>
        <p :class="this.valueStatus">
          Valor dos itens: {{ fixCurrency(valorTotalCompra) }}
        </p>
      </div>
    </div>

    <!-- div's extras que ajustam a visibilidade -->
    <div></div>
    <div></div>
  </div>

  <!-- Elementos condicionais baseados na escolha da Obra (Botão + Novo Custo) -->
  <div v-if="this.comprasInfo != ''" v-show="this.selectedObraNome" class="header middle-margin">
    <!-- Botão para adicionar Compra -->
    <button
      v-show="!this.showItems"
      id="novaCompraButton"
      type="button"
      class="btn btn-success light-green"
      data-bs-toggle="modal"
      data-bs-target="#insertModal"
      @click="focusFirstModalInput('insert-compra-socio-pagador-select')"
    >+ Novo Custo</button>
  </div>

  <!-- DeleteModalCompra -->
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
            id="deletaCompra"
            type="button"
            class="btn btn-success light-green"
            data-bs-dismiss="modal"
            @click="removeCompraFromDB(this.codigo)"
          >Sim</button>
        </div>
      </div>
    </div>
  </div>

  <!-- InsertModalCompra -->
  <div class="modal fade" id="insertModal" data-bs-backdrop="static" data-bs-keyboard="false" tabindex="-1" aria-labelledby="insertModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-dialog-scrollable">
      <div class="modal-content">
        <div class="modal-header">
          <h1 class="modal-title fs-5" id="insertModalLabel">Novo Custo</h1>
          <button @click="cancelInsert" type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
        </div>

        <div class="modal-body">

          <form action="POST">

            <!-- Obra -->
            <div class="mb-3">
              <label for="insert-compra-obra-nome-input" class="bold">Obra:</label>
              <input
                type="text"
                class="form-control"
                id="insert-compra-obra-nome-input"
                disabled
                v-model="selectedObraNome">
            </div>

            <!-- Sócio pagador -->
            <div class="mb-3">
              <label id="insert-compra-socio-pagador-label" for="insert-compra-socio-pagador-select" class="bold red-asterisk">Pagador:</label>
              <select
                class="form-select"
                id="insert-compra-socio-pagador-select"
                v-model="selectedSocioCod"
                @click="checkInputValue(selectedSocioCod, 'insert-compra-socio-pagador-select')"
              >
                <option
                  v-for="(socio, i) in selectedSociosInfo" :key="i" :value="socio.codigo"
                  >{{ socio.nome }}</option>
              </select>
            </div>

            <!-- Fornecedor -->
            <div class="mb-3">
              <label id="insert-compra-fornecedor-label" for="insert-compra-fornecedor-select" class="bold red-asterisk">Fornecedor:</label>
              <select
                class="form-select"
                id="insert-compra-fornecedor-select"
                v-model="selectedFornecedorCod"
                @click="checkTipoFornecedor"
              ><option
                  v-for="(fornecedor, i) in fornecedoresInfo" :key="i" :value="fornecedor.codigo"
                  >{{ fornecedor.nome }}</option>
              </select>
            </div>

            <!-- Data da Compra -->
            <div v-if="this.fornecedor.tipoFornecedor != 'Serviço'" class="mb-3">
              <label for="insert-compra-data-compra-input" class="form-label bold">Data da compra:</label>
              <input
                type="date"
                class="form-control"
                id="insert-compra-data-compra-input"
                v-model="dataCompra">
            </div>

            <!-- Data da Entrega -->
            <div v-if="this.fornecedor.tipoFornecedor != 'Serviço'" class="mb-3">
              <label for="insert-compra-data-entrega-input" class="form-label bold">Data da entrega:</label>
              <input
                type="date"
                class="form-control"
                id="insert-compra-data-entrega-input"
                v-model="dataEntrega">
            </div>

            <!-- Data de Pagamento -->
            <div class="mb-3">
              <label for="insert-compra-data-pagamento-input" class="form-label bold">Data de pagamento:</label>
              <input
                type="date"
                class="form-control"
                id="insert-compra-data-pagamento-input"
                v-model="dataPagamento">
            </div>

            <!-- Data de Vencimento -->
            <div v-if="this.fornecedor.tipoFornecedor != 'Serviço'" class="mb-3">
              <label for="insert-compra-data-vencimento-input" class="form-label bold">Data de vencimento:</label>
              <input
                type="date"
                class="form-control"
                id="insert-compra-data-vencimento-input"
                v-model="dataVencimento">
            </div>

            <!-- Valor Original -->
            <div class="mb-3">
              <label id="insert-compra-valor-original-label" for="insert-compra-valor-original-input" class="form-label bold red-asterisk">Valor original:</label>
              <input
                type="number"
                step="0.01"
                title=''
                class="form-control"
                id="insert-compra-valor-original-input"
                placeholder="R$... (inserir apenas números e ponto)"
                v-model="valorOriginal"
                maxlength="30"
                @change="calculaValorFinalCompra"
                @keyup="checkInputValue(valorOriginal, 'insert-compra-valor-original-input')"
              >
            </div>

            <!-- Valor Desconto -->
            <div class="mb-3">
              <label for="insert-compra-desconto-input" class="form-label bold">Desconto:</label>
              <input
                type="number"
                step="0.01"
                title=''
                class="form-control"
                id="insert-compra-desconto-input"
                placeholder="R$... (inserir apenas números e ponto)"
                v-model="valorDesconto"
                maxlength="30"
                @change="calculaValorFinalCompra">
            </div>

            <!-- Valor Final -->
            <div class="mb-3">
              <label for="insert-compra-valor-final-input" class="form-label bold">Valor total:</label>
              <input
                type="number"
                step="0.01"
                title=''
                class="form-control"
                id="insert-compra-valor-final-input"
                placeholder="R$... (inserir apenas números e ponto)"
                v-model="valorFinal"
                maxlength="30">
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
            id="salvaNovaCompra"
            type="button"
            class="btn btn-success  light-green"
            @click="createCompraInfoDB"
          >Salvar</button>
        </div>
      </div>
    </div>
  </div>

  <!-- UpdateModalCompra -->
  <div class="modal fade" id="updateModal" data-bs-backdrop="static" data-bs-keyboard="false" tabindex="-1" aria-labelledby="updateModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-dialog-scrollable">
      <div class="modal-content">
        <div class="modal-header">
          <h1 class="modal-title fs-5" id="updateModalLabel">Editar Custo</h1>
          <button @click="cancel" type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
        </div>

        <div class="modal-body">
          <form action="PUT">

            <!-- Código -->
            <div class="mb-3">
              <label for="update-compra-codigo-input" class="form-label bold">Código:</label>
              <input
                type="text"
                class="form-control"
                id="update-compra-codigo-input"
                disabled
                v-model="codigo">
            </div>

            <!-- Obra -->
            <div class="mb-3">
              <label for="update-compra-obra-nome-input" class="form-label bold">Obra:</label>
              <input
                type="text"
                class="form-control"
                id="update-compra-obra-nome-input"
                disabled
                v-model="selectedObraNome">
            </div>

            <!-- Sócio pagador -->
            <div class="mb-3">
              <label for="update-compra-socio-pagador-select" class="bold red-asterisk">Pagador:</label>
              <select
                class="form-select"
                id="update-compra-socio-pagador-select"
                v-model="selectedSocioNome"
                @click="getCodigoSocioForCompraUpdate"
              ><option
                  v-for="(socio, i) in selectedSociosInfo" :key="i" :value="socio.nome"
                  >{{ socio.nome }}</option>
              </select>
            </div>

            <!-- Fornecedor -->
            <div class="mb-3">
              <label for="update-compra-fornecedor-select" class="bold red-asterisk">Fornecedor:</label>
              <select
                class="form-select"
                id="update-compra-fornecedor-select"
                v-model="selectedFornecedorNome"
                @click="getCodigoFornecedorForCompraUpdate"
              ><option
                v-for="(fornecedor, i) in fornecedoresInfo" :key="i" :value="fornecedor.nome"
                >{{ fornecedor.nome }}</option>
              </select>
            </div>

            <!-- Data da Compra -->
            <div v-if="this.fornecedor.tipoFornecedor != 'Serviço'" class="mb-3">
              <label for="update-compra-data-compra-input" class="form-label bold">Data da compra:</label>
              <input
                type="date"
                class="form-control"
                id="update-compra-data-compra-input"
                v-model="dataCompra">
            </div>

            <!-- Data da Entrega -->
            <div v-if="this.fornecedor.tipoFornecedor != 'Serviço'" class="mb-3">
              <label for="update-compra-data-entrega-input" class="form-label bold">Data da entrega:</label>
              <input
                type="date"
                class="form-control"
                id="update-compra-data-entrega-input"
                v-model="dataEntrega">
            </div>

            <!-- Data de Pagamento -->
            <div class="mb-3">
              <label for="update-compra-data-pagamento-input" class="form-label bold">Data de pagamento:</label>
              <input
                type="date"
                class="form-control"
                id="update-compra-data-pagamento-input"
                v-model="dataPagamento">
            </div>

            <!-- Data de Vencimento -->
            <div v-if="this.fornecedor.tipoFornecedor != 'Serviço'" class="mb-3">
              <label for="update-compra-data-vencimento-input" class="form-label bold">Data de vencimento:</label>
              <input
                type="date"
                class="form-control"
                id="update-compra-data-vencimento-input"
                v-model="dataVencimento">
            </div>

            <!-- Valor Original -->
            <div class="mb-3">
              <label for="update-compra-valor-original-input" class="form-label bold red-asterisk">Valor original:</label>
              <input
                type="number"
                step="0.01"
                title=''
                class="form-control"
                id="update-compra-valor-original-input"
                placeholder="R$... (inserir apenas números e ponto)"
                v-model="valorOriginal"
                maxlength="30"
                @change="calculaValorFinalCompra">
            </div>

            <!-- Valor Desconto -->
            <div class="mb-3">
              <label for="update-compra-desconto-input" class="form-label bold">Desconto:</label>
              <input
                type="number"
                step="0.01"
                title=''
                class="form-control"
                id="update-compra-desconto-input"
                placeholder="R$... (inserir apenas números e ponto)"
                v-model="valorDesconto"
                maxlength="30"
                @change="calculaValorFinalCompra">
            </div>

            <!-- Valor Final -->
            <div class="mb-3">
              <label for="update-compra-valor-final-input" class="form-label bold">Valor total:</label>
              <input
                type="number"
                class="form-control"
                id="update-compra-valor-final-input"
                placeholder="R$... (inserir apenas números e ponto)"
                v-model="valorFinal"
                maxlength="30">
            </div>

          </form>
        </div>

        <div class="modal-footer">
          <button type="button" class="btn btn-secondary dark-grey" data-bs-dismiss="modal"
            @click="cancel"
          >Fechar</button>

          <button id="atualizaCompra" type="button" class="btn btn-success  light-green" data-bs-dismiss="modal"
            @click="updateCompraInfoDB()"
          >Salvar</button>
        </div>
      </div>
    </div>
  </div>

  <!-- DeleteModalItem -->
  <div class="modal fade" id="deleteItemModal" data-bs-backdrop="static" data-bs-keyboard="false" tabindex="-1" aria-labelledby="deleteItemModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-dialog-scrollable modal-sm">
      <div class="modal-content">
        <div class="modal-header">
          <h1 class="modal-title fs-5" id="deleteItemModalLabel">Realmente deseja excluir?</h1>
          <button @click="cancelItem" type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
        </div>

        <div class="modal-footer header">
          <button
            type="button"
            class="btn btn-secondary dark-grey"
            data-bs-dismiss="modal"
            @click="cancelItem"
          >Não</button>

          <button
            id="deletaItem"
            type="button"
            class="btn btn-success light-green"
            data-bs-dismiss="modal"
            @click="removeItemFromDB(this.codigoItem)"
          >Sim</button>
        </div>
      </div>
    </div>
  </div>

  <!-- InsertModalItem -->
  <div class="modal fade" id="insertItemModal" data-bs-backdrop="static" data-bs-keyboard="false" tabindex="-1" aria-labelledby="insertItemModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-dialog-scrollable">
      <div class="modal-content">
        <div class="modal-header">
          <h1 class="modal-title fs-5" id="insertItemModalLabel">Novo Item</h1>
          <button @click="cancelInsertItem" type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
        </div>

        <div class="modal-body">

          <form action="POST">

            <!-- Compra -->
            <div class="mb-3">
              <label for="insert-item-compra-codigo-input" class="bold">Compra:</label>
              <input
                type="text"
                class="form-control"
                id="insert-item-compra-codigo-input"
                disabled
                v-model="compraCodForne">
            </div>

            <!-- Produto -->
            <div class="mb-3">
              <label id="insert-item-produto-label" for="insert-item-produto-select" class="bold red-asterisk">Produto:</label>
              <select
                class="form-select"
                id="insert-item-produto-select"
                v-model="selectedProdutoCod"
                @click="checkInputValue(selectedProdutoCod, 'insert-item-produto-select')"
              ><option
                v-for="(produto, i) in produtosInfo" :key="i" :value="produto.codigo"
                >{{ produto.nome }}</option>
              </select>
            </div>

            <!-- localUso -->
            <div class="mb-3">
              <label id="insert-item-local-uso-label" for="insert-item-local-uso-select" class="bold red-asterisk">Local de uso:</label>
              <select
                class="form-select"
                id="insert-item-local-uso-select"
                v-model="selectedLocalUsoCod"
                @click="checkInputValue(selectedLocalUsoCod , 'insert-item-local-uso-select')"
              ><option
                v-for="(localUso, i) in localUsoInfo" :key="i" :value="localUso.codigoLocalUsoObra"
                >{{ localUso.nomeLocalUsoObra }}</option>
              </select>
            </div>

            <!-- Quantidade -->
            <div class="mb-3">
              <label for="insert-item-quantidade-input" class="form-label bold">Quantidade:</label>
              <input
                type="number"
                class="form-control"
                id="insert-item-quantidade-input"
                placeholder="(inserir apenas números)"
                v-model="quantidade"
                maxlength="30"
                @change="calculaValorItem"
              >
            </div>

            <!-- Unidade de medida -->
            <div class="mb-3">
              <label id="insert-item-unidade-medida-label" for="insert-item-unidade-medida-select" class="bold  red-asterisk">Unidade de medida:</label>
              <select
                class="form-select"
                id="insert-item-unidade-medida-select"
                v-model="selectedUnidadeMedidaCod"
                @click="checkInputValue(selectedUnidadeMedidaCod, 'insert-item-unidade-medida-select')"
              ><option
                v-for="(unidadeMedida, i) in unidadeMedidaInfo" :key="i" :value="unidadeMedida.codigo"
                >{{ unidadeMedida.unidade }}</option>
              </select>
            </div>

            <!-- Valor Unitario -->
            <div class="mb-3">
              <label for="insert-item-valor-unitario-input" class="form-label bold">Valor unitário:</label>
              <input
                type="number"
                step="0.01"
                title=''
                class="form-control"
                id="insert-item-valor-unitario-input"
                placeholder="R$... (inserir apenas números e ponto)"
                v-model="valorUnitario"
                maxlength="30"
                @change="calculaValorItem">
            </div>

            <!-- Valor Total -->
            <div class="mb-3">
              <label for="insert-item-valor-total-input" class="form-label bold">Valor total:</label>
              <input
                type="number"
                step="0.01"
                title=''
                class="form-control"
                id="insert-item-valor-total-input"
                placeholder="R$... (inserir apenas números e ponto)"
                v-model="valorTotal"
                maxlength="30"
              >
            </div>

          </form>
        </div>

        <div class="modal-footer">
          <button
            type="button"
            class="btn btn-secondary dark-grey"
            data-bs-dismiss="modal"
            @click="cancelInsertItem"
          >Fechar</button>
          <button
            id="salvaNovoItem"
            type="button"
            class="btn btn-success  light-green"
            @click="createItemInfoDB"
          >Salvar</button>
        </div>
      </div>
    </div>
  </div>

  <!-- UpdateModalItem -->
  <div class="modal fade" id="updateItemModal" data-bs-backdrop="static" data-bs-keyboard="false" tabindex="-1" aria-labelledby="updateItemModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-dialog-scrollable">
      <div class="modal-content">
        <div class="modal-header">
          <h1 class="modal-title fs-5" id="updateItemModalLabel">Editar Item</h1>
          <button @click="cancelItem" type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
        </div>

        <div class="modal-body">
          <form action="PUT">

            <!-- Código -->
            <div class="mb-3">
              <label for="update-item-codigo-input" class="form-label bold">Código:</label>
              <input
                type="text"
                class="form-control"
                id="update-item-codigo-input"
                disabled
                v-model="codigoItem">
            </div>

            <!-- Produto -->
            <div class="mb-3">
              <label for="update-item-produto-input" class="form-label bold">Produto:</label>
              <input
                type="text"
                class="form-control"
                id="update-item-produto-input"
                v-model="selectedProdutoNome"
                disabled>
            </div>

            <!-- Local de uso -->
            <div class="mb-3">
              <label for="update-item-local-uso-select" class="bold">Local de uso:</label>
              <select
                class="form-select"
                id="update-item-local-uso-select"
                v-model="selectedLocalUsoNome"
                @click="getCodigoLocalUsoForItemUpdate"
              ><option
                v-for="(localUso, i) in localUsoInfo" :key="i" :value="localUso.nomeLocalUsoObra"
                >{{ localUso.nomeLocalUsoObra }}</option>
              </select>
            </div>

            <!-- Quantidade -->
            <div class="mb-3">
              <label for="update-item-quantidade-input" class="form-label bold">Quantidade:</label>
              <input
                type="number"
                class="form-control"
                id="update-item-quantidade-input"
                placeholder="R$... (inserir apenas números e ponto)"
                v-model="quantidade"
                maxlength="30"
                @change="calculaValorItem">
            </div>

            <!-- Unidade de medida -->
            <div class="mb-3">
              <label for="update-item-unidade-medida-select" class="bold">Unidade de medida:</label>
              <select
                class="form-select"
                id="update-item-unidade-medida-select"
                v-model="selectedUnidadeMedida"
                @click="getCodigoUnidadeMedidaForItemUpdate"
              ><option
                  v-for="(unidadeMedida, i) in unidadeMedidaInfo" :key="i" :value="unidadeMedida.unidade"
                  >{{ unidadeMedida.unidade }}</option>
              </select>
            </div>        

            <!-- Valor Unitario -->
            <div class="mb-3">
              <label for="update-item-valor-unitario-input" class="form-label bold">Valor unitário:</label>
              <input
                type="number"
                step="0.01"
                title=""
                class="form-control"
                id="update-item-valor-unitario-input"
                placeholder="R$... (inserir apenas números e ponto)"
                v-model="valorUnitario"
                maxlength="30"
                @change="calculaValorItem">
            </div>

            <!-- Valor Total -->
            <div class="mb-3">
              <label for="update-item-valor-total-input" class="form-label bold">Valor total:</label>
              <input
                type="number"
                step="0.01"
                title=""
                class="form-control"
                id="update-item-valor-total-input"
                placeholder="R$... (inserir apenas números e ponto)"
                v-model="valorTotal"
                maxlength="30">
            </div>

          </form>
        </div>

        <div class="modal-footer">
          <button type="button" class="btn btn-secondary dark-grey" data-bs-dismiss="modal"
            @click="cancelItem"
          >Fechar</button>

          <button id="atualizaItem" type="button" class="btn btn-success  light-green" data-bs-dismiss="modal"
            @click="updateItemInfoDB"
          >Salvar</button>
        </div>
      </div>
    </div>
  </div>

  <!-- Tabelas condicionais baseadas na escolha de Itens ou Compras -->
  <!-- Tabela de Compras -->
  <main v-if="this.comprasInfo != ''" v-show="!showItems" class="middle-margin table-responsive">
    <table class="table table-hover">
      <thead>
        <tr>
          <th scope="col">Código</th>
          <th scope="col">Sócio</th>
          <th scope="col">Fornecedor</th>
          <!-- <th scope="col">Data da compra</th> -->
          <!-- <th scope="col">Data da entrega</th> -->
          <th scope="col">Data de pagamento</th>
          <!-- <th scope="col">Data de vencimento</th> -->
          <!-- <th scope="col">Valor original</th> -->
          <!-- <th scope="col">Desconto</th> -->
          <th scope="col">Valor total</th>
          <th></th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="(compra, i) in selectedComprasByObra" :key="i">
          <th scope="row">{{ compra.codigo }}</th>
          <td>{{ this.firstLastName(compra.socio.nome) }}</td>
          <td>{{ compra.fornecedor.nome }}</td>
          <!-- <td>{{ brazilDate(compra.dataCompra) }}</td> -->
          <!-- <td>{{ brazilDate(compra.dataEntrega) }}</td> -->
          <td>{{ brazilDate(compra.dataPagamento) }}</td>
          <!-- <td>{{ brazilDate(compra.dataVencimento) }}</td> -->
          <!-- <td>{{ fixCurrency(compra.valorOriginal) }}</td> -->
          <!-- <td>{{ fixCurrency(compra.valorDesconto) }}</td> -->
          <td>{{ fixCurrency(compra.valorFinal) }}</td>
          <td class="editar-excluir">
            <button
              type="button"
              class="btn btn-light btn-sm small"
              title="Editar"
              data-bs-toggle="modal"
              data-bs-target="#updateModal"
              @click="fillUpdateCompraModal(compra.codigo, compra.obra.codigo,
                compra.dataCompra, compra.dataEntrega, compra.dataPagamento,
                compra.dataVencimento, compra.valorOriginal, compra.valorDesconto,
                compra.valorFinal, compra.fornecedor.nome, compra.socio.nome)"
            ><img src="../assets/imagens/editar.png" alt="lata de lixo"></button>
            <button
              type="button"
              class="btn btn-light btn-sm small"
              title="Itens"
              @click="getItensForThisCompra(compra.codigo)"
            ><img src="../assets/imagens/lista_itens_2.png" alt="lista"></button>
            <button
              type="button"
              class="btn btn-light btn-sm small"
              title="Excluir"
              data-bs-toggle="modal"
              data-bs-target="#deleteModal"
              @click="fillDeleteCompraModal(compra.codigo)"
            ><img src="../assets/imagens/lata-de-lixo.png" alt="lata de lixo"></button>
          </td>
        </tr>
      </tbody>
    </table>
  </main>

  <!-- Tabela de Itens -->
  <main v-show="showItems" class="middle-margin table-responsive">
    <div></div>
    <table class="table table-hover">
      <thead>
        <tr>
          <th scope="col">Código</th>
          <th scope="col">Produto</th>
          <th scope="col">Quantidade</th>
          <th scope="col">Valor unitário</th>
          <th scope="col">Valor total</th>
          <th></th>
          <th></th>
          <th></th>
          <th></th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="(item, i) in selectedItensByCompra" :key="i">
          <th scope="row">{{ item.codigo }}</th>
          <td>{{ item.produto.nome }}</td>
          <td>{{ item.quantidade }} {{ item.unidadeMedida ? item.unidadeMedida.unidade : '' }}</td>
          <td>{{ fixCurrency(item.valorUnitario) }}</td>
          <td>{{ fixCurrency(item.valorTotal) }}</td>
          <td></td>
          <td></td>
          <td></td>
          <td class="editar-excluir">
            <button
              type="button"
              class="btn btn-light btn-sm small"
              title="Editar"
              data-bs-toggle="modal"
              data-bs-target="#updateItemModal"
              @click="fillUpdateItemModal(item.codigo, item.quantidade, item.valorUnitario,
              item.valorTotal, item.produto.nome, item.produto.codigo,
              item.localUso.nomeLocalUsoObra, item.unidadeMedida.unidade)"
            ><img src="../assets/imagens/editar.png" alt="lata de lixo"></button>
            <button
              type="button"
              class="btn btn-light btn-sm small"
              title="Excluir"
              data-bs-toggle="modal"
              data-bs-target="#deleteItemModal"
              @click="fillDeleteItemModal(item.codigo)"
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

.column {
  display: flex;
  justify-content: space-between;
  flex-direction: column;
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

.red-letter {
  color: red;
}

.green-letter {
  color:green;
}

.grey-letter {
  color: #212529;
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