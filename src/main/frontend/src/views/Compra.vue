<script>
// Requisição de NOVA COMPRA:
// {
//   "obra":  {
//     "codigo": 2,
//     "nome": "Obra Teste 2",
//     "endereco": "Rua Obra Teste, 2000",
//     "dataInicio": "2023-10-10",
//     "dataPrevistaFim": "2023-10-15",
//     "dataRealFim": "2023-10-25",
//     "custoPrevisto": 45000
//   },
//   "fornecedor": {
//     "codigo": 15,
//     "nome": "Fornecedor Teste 1",
//     "telefone": "(11) 97557-8998",
//     "endereco": "Rua Fornecedor Teste, 1000",
//     "tipoFornecedor": "Material"
//   },
//   "dataCompra": "2023-05-30",
//   "dataEntrega": "2023-05-30",
//   "dataPagamento": "",
//   "dataVencimento": "",
//   "valorOriginal": 90,
//   "valorDesconto": 0,
//   "valorFinal": 90
// }

// Requisição de NOVO ITEM DE COMPRA:
// {
//   "compra": {
//     "codigo": 89,
//     "dataCompra": "2006-11-27",
//     "dataEntrega": "2017-04-20",
//     "dataPagamento": "2015-06-09",
//     "dataVencimento": "1971-05-17",
//     "valorOriginal": 100.0,
//     "valorDesconto": 0.0,
//     "valorFinal": 100.0
//   },
//   "produto": {
//     "codigo": 1,
//     "nome": "Areia",
//     "tipoProduto": "Material"
//   },
//   "localUso": {
//     "codigoLocalUsoObra": 2,
//     "nomeLocalUsoObra": "Fundação",
//     "dataDesativacao": null
//   },
//   "quantidade": 10,
//   "valorUnitario": 1.50,
//   "valorTotal": 15
// }

import axios from 'axios';

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
      selectedComprasByObra: [],
      selectedItensByCompra: [],
      // Variáveis auxiliares
      selectedObraNome: '',
      selectedFornecedorNome: '',
      selectedProdutoNome: '',
      selectedLocalUsoNome: '',
      selectedUnidadeMedida: '',
      selectedCompraID: '',
      getCompraInfo: {}, // Apenas informações da compra para serem usadas na página.
      compraCodForne: '',
      valorTotalCompra: 0,
      showItems: false,
      valueStatus: '',
      // Variáveis para requisição
      codigo: '',
      obra: {},
      fornecedor: {},
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

  methods: {
    // Método para apagar dados que foram preenchidos e enviados ou não enviados.
    cancel () {
      this.selectedFornecedorNome = '';
      this.fornecedor = {};
      this.dataCompra = '';
      this.dataEntrega = '';
      this.dataPagamento = '';
      this.dataVencimento = '';
      this.valorOriginal = '';
      this.valorDesconto = '';
      this.valorFinal = '';
    },
    // Método GET - Compras.
    fetchComprasInfoDB (callback) {
      axios.get("/api/compra").then(
        (res) => {
          this.comprasInfo = res.data.sort((s1, s2) => s1.codigo - s2.codigo);
          if (callback) callback();
        })
    },
    // Método GET - Obras.
    fetchObrasInfoDB () {
      axios.get("/api/obra").then(
        (res) => this.obrasInfo = res.data.sort((s1, s2) => s1.codigo - s2.codigo))
    },
    // Método GET - Fornecedores.
    fetchFornecedoresInfoDB () {
      axios.get("/api/fornecedor").then(
        (res) => this.fornecedoresInfo = res.data.sort((s1, s2) => s1['nome'].localeCompare(s2['nome'])))
    },
    // Método GET - Itens das compras.
    fetchItensCompraInfoDB (callback) {
      axios.get("/api/itemcompra").then(
        (res) => {
          this.itensCompraInfo = res.data.sort((s1, s2) => s1.codigo - s2.codigo)
          // 1. if (callback) callback(): Verifica se uma função de retorno de chamada foi
          //    fornecida como argumento da função. Se uma 'callback' foi fornecida, ela é
          //    chamada aqui. Permitindo que quem chame a função 'fetchItensCompraInfoDB'
          //    especifique uma função a ser executada após a conclusa da busca e da
          //    ordenação dos itens de compra.
          if (callback) callback();
        })
    },
    // Método GET - Produtos.
    fetchProdutosInfoDB() {
      axios.get("/api/produto").then(
        (res) => this.produtosInfo = res.data.sort((s1, s2) => s1['nome'].localeCompare(s2['nome'])))
    },
    // Método GET - LocalUso.
    fetchLocalUsoInfoDB () {
      axios.get("/api/localuso").then(
        (res) => this.localUsoInfo = res.data.sort((s1, s2) => s1['nomeLocalUsoObra'].localeCompare(s2['nomeLocalUsoObra'])))
    },
    // Método GET - UnidadeMedida.
    fetchUnidadeMedidaInfoDB () {
      axios.get("/api/unidademedida").then(
        (res) => this.unidadeMedidaInfo = res.data.sort((s1, s2) => s1['unidade'].localeCompare(s2['unidade'])))
    },
    // Método para esvaziar o array que guarda as Compras selecionadas por Obra.
    emptySelectedComprasByObraArray () {
      this.selectedComprasByObra = [];
    },
    // Método para preencher a Obra Selecionada (variavel obraSelected).
    selectObra (nomeObra) {
      this.selectedObraNome = nomeObra;
    },
    // Método para preencher o objeto "this.obra" com a obra correta para a requisição.
    fillObraForRequest () {
      for (let chosenObra of this.obrasInfo) {
        if (chosenObra.nome === this.selectedObraNome) {
          this.obra = chosenObra;
        }
      }
    },
    // Método para preencher o objeto "this.fornecedor" com o fornecedor correto para a requisição.
    fillFornecedorForRequest () {
      for (let chosenFornecedor of this.fornecedoresInfo) {
        if (chosenFornecedor.nome === this.selectedFornecedorNome) {
          this.fornecedor = chosenFornecedor;
        }
      }
    },
    // Método para popular o array "selectedComprasByObra".
    selectComprasByObra () {
      for (let chosenCompra of this.comprasInfo) {
        if (chosenCompra.obra.nome === this.selectedObraNome) {
          this.selectedComprasByObra.push(chosenCompra);
        }
      }
    },
    // Método para renderizar a lista de Compras por Obra.
    obrasDropDownActions (nomeObra) {
      this.selectObra(nomeObra);
      this.fillObraForRequest();
      this.emptySelectedComprasByObraArray();
      this.selectComprasByObra();
    },
    // Método para preencher a modal de DELETE e UPDATE.
    fillUpdateDeleteModal (cod, obra, fornecedor, dataC, dataE, dataP,
      dataV, valorO, valorD, valorF, selectedFornecedorNome) {
      this.codigo = cod;
      this.obra = obra;
      this.fornecedor = fornecedor;
      this.dataCompra = dataC;
      this.dataEntrega = dataE;
      this.dataPagamento = dataP;
      this.dataVencimento = dataV;
      this.valorOriginal = valorO;
      this.valorDesconto = valorD;
      this.valorFinal = valorF;
      this.selectedFornecedorNome = selectedFornecedorNome;
    },
    // Método para inserir Nova Compra.
    createCompra (callback) {
      axios.post("/api/compra",
        {
          obra: this.obra,
          fornecedor: this.fornecedor,
          dataCompra: this.dataCompra,
          dataEntrega: this.dataEntrega,
          dataPagamento: this.dataPagamento,
          dataVencimento: this.dataVencimento,
          valorOriginal: this.valorOriginal,
          valorDesconto: this.valorDesconto,
          valorFinal: this.valorFinal
        }).then(() => callback());
    },
    // Chama o método 'createCompra' e repopula a lista correta.
    createCompraInfoDB () {
      let self = this;
      this.fillObraForRequest();
      this.fillFornecedorForRequest();
      this.createCompra(() => {
        self.fetchComprasInfoDB(() => {
          self.emptySelectedComprasByObraArray();
          self.selectComprasByObra();
        })
      });
      this.cancel();
    },
    // Remove Compra selecionada.
    removeCompra (codigo, callback) {
      axios.delete("/api/compra",
        {
          headers: {
            Authorization: ''
          },
          data: {
            codigo: Number(codigo)
          }
        }).then(() => callback());
    },
    // Chama o método 'removeCompra' e repopula a lista correta.
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
    // Método para atualizar uma Compra selecionada.
    updateCompra (cod, dataC, dataE, dataP, dataV, valorO, valorD, valorF, callback) {
      axios.put("/api/compra",
      {
        codigo: Number(cod),
        obra: this.obra,
        fornecedor: this.fornecedor,
        dataCompra: dataC,
        dataEntrega: dataE,
        dataPagamento: dataP,
        dataVencimento: dataV,
        valorOriginal: valorO,
        valorDesconto: valorD,
        valorFinal: valorF
      }).then(() => callback());
    },
    // Chama o método 'updateCompra' e repopula a lista corretamente.
    updateCompraInfoDB (cod, dataC, dataE, dataP, dataV, valorO, valorD, valorF) {
      let self = this;
      this.fillObraForRequest();
      this.fillFornecedorForRequest();
      this.updateCompra(cod, dataC, dataE, dataP, dataV, valorO, valorD, valorF, () => {
        self.fetchComprasInfoDB(() => {
          self.emptySelectedComprasByObraArray();
          self.selectComprasByObra();
        });
      });
      this.cancel();
    },
    // Método para apresentar valores monetários BRL corretamente.
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
    // Método para ajustar para data brasileira.
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
    // Método para limpar a requisição caso não seja enviada.
    cancelItem() {
      this.selectedProdutoNome = '';
      this.selectedLocalUsoNome = '';
      this.selectedUnidadeMedida = '';
      // this.compra = {};
      this.unidadeMedida = {};
      this.produto = {};
      this.localUso = {};
      this.quantidade = '';
      this.valorUnitario = '';
      this.valorTotal = '';
    },
    // Método para alternar a renderização de Compras e Itens.
    switchItensCompras () {
      this.showItems = !this.showItems;
      this.clearSelectedCompraID();
      this.clearSelectedItensByCompra();
    },
    // Método para limpar a lista de itens selecionados por compra.
    clearSelectedItensByCompra () {
      this.selectedItensByCompra = [];
    },
    // Método para retornar às compras, limpando 'selectedCompraID'.
    clearSelectedCompraID () {
      this.selectedCompraID = '';
    },
    // Método para preencher a variavel 'selectedCompraID'.
    selectCompraID (cod) {
      this.selectedCompraID = cod;
    },
    // Recupera os itens corretos para a compra selecionada.
    getItensForThisCompra (cod) {
      this.switchItensCompras();
      this.selectCompraID(cod);
      this.fillCompraInfo();
      this.fillCompraForRequest();
      this.selectItensByCompra();
      this.sumValorTotalCompra();
    },
    // Método para popular o array 'selectedItensByCompra'.
    selectItensByCompra () {
      for (let item of this.itensCompraInfo) {
        if (item.compra.codigo === this.selectedCompraID) {
          this.selectedItensByCompra.push(item);
        }
      }
      // Ordena por nome de produto
      this.selectedItensByCompra.sort((s1, s2) => s1.produto['nome'].localeCompare(s2.produto['nome']))
    },
    // Método para preencher o 'this.getCompraInfo' para usar informações na página.
    fillCompraInfo () {
      for (let chosenCompra of this.comprasInfo) {
        if (chosenCompra.codigo === this.selectedCompraID) {
          this.getCompraInfo = chosenCompra;
        }
      }
    },
    // Método para preencher o 'this.compra' com o formato correto para a requisição.
    fillCompraForRequest () {
      for (let chosenCompra of this.comprasInfo) {
        if (chosenCompra.codigo === this.selectedCompraID) {
          this.compra = chosenCompra;
        }
      }
    },
    // Método para preencher o 'this.produto' com o formato correto para a requisição.
    fillProdutoForRequest () {
      for (let chosenProduto of this.produtosInfo) {
        if (chosenProduto.nome === this.selectedProdutoNome) {
          this.produto = chosenProduto;
        }
      }
    },
    // Método para preencher 'this.localUso' com o formato correto para a requisição.
    fillLocalUsoForRequest () {
      for (let chosenLocalUso of this.localUsoInfo) {
        if (chosenLocalUso.nomeLocalUsoObra === this.selectedLocalUsoNome) {
          this.localUso = chosenLocalUso;
        }
      }
    },
    // Método para preencher 'this.unidadeMedida' com o formato correto para a requisição.
    fillUnidadeMedidaForRequest() {
      for (let chosenUnidadeMedida of this.unidadeMedidaInfo) {
        if (chosenUnidadeMedida.unidade === this.selectedUnidadeMedida) {
          this.unidadeMedida = chosenUnidadeMedida;
        }
      }
    },
    // Método para preencher o 'this.compraCodForne' para a inserção de novo Item.
    fillCompraCodForne () {
      this.compraCodForne = `${this.getCompraInfo.codigo} - ${this.getCompraInfo.fornecedor.nome}`;
    },
    // Método para inserir um novo Item a uma Compra.
    createItem (callback) {
      axios.post("/api/itemcompra",
        {
          compra: this.compra,
          produto: this.produto,
          localUso: this.localUso,
          unidadeMedida: this.unidadeMedida,
          quantidade: this.quantidade,
          valorUnitario: this.valorUnitario,
          valorTotal: this.valorTotal
        }).then(() => callback());
    },
    // Método que chama o método 'createItem' e realiza a requisição.
    createItemInfoDB () {
      let self = this;
      this.fillCompraForRequest();  // Teoricamente não precisa dessa chamada.
      this.fillProdutoForRequest();
      this.fillLocalUsoForRequest();
      this.fillUnidadeMedidaForRequest();
      this.createItem(() => {
        self.fetchItensCompraInfoDB(() => {
          self.clearSelectedItensByCompra();
          self.selectItensByCompra();
          self.sumValorTotalCompra();
        });
      });
      this.cancelItem();
    },
    // Método para preencher a ItemModal de DELETE e UPDATE.
    fillUpdateDeleteItemModal (cod, comp, prod, locUso, uniMedida, qnt, valorU,
      valorT, selectedProdNome, selectedLocUsoNome, selectedUniMedida) {
      this.codigoItem = cod;
      this.compra = comp;
      this.produto = prod;
      this.localUso = locUso;
      this.unidadeMedida = uniMedida;
      this.quantidade = qnt;
      this.valorUnitario = valorU;
      this.valorTotal = valorT;
      this.selectedProdutoNome = selectedProdNome;
      this.selectedLocalUsoNome = selectedLocUsoNome;
      this.selectedUnidadeMedida = selectedUniMedida;
    },
    // Método que exclui um item.
    // 1. .then(() => callback()): função de retorno de chamada a ser executada
    //    após a remoção bem-sucedida do item. Permite que a função que chame
    //    'removeItem' defina essa callback a ser executada após a exclusão.
    removeItem (cod, callback) {
      axios.delete("/api/itemcompra",
        {
          headers: {
            Authorization: ''
          },
          data: {
            codigo: Number(cod)
          }
        }).then(() => callback());
    },
    // Método que chama 'removeItem' e renderiza a lista.
    // Função organizada por ordem de execução usando callbacks.
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
    // Método para atualizar um Item selecionado.
    updateItem (cod, qnt, valorU, valorT, callback) {
      axios.put("/api/itemcompra",
      {
        codigo: Number(cod),
        compra: this.compra,
        produto: this.produto,
        localUso: this.localUso,
        unidadeMedida: this.unidadeMedida,
        quantidade: qnt,
        valorUnitario: valorU,
        valorTotal: valorT
      }).then(() => callback());
    },
    // Chama o método 'updateItem' e repopula a lista corretamente.
    updateItemInfoDB (cod, qnt, valorU, valorT) {
      let self = this;
      this.fillCompraForRequest();
      this.fillProdutoForRequest();
      this.fillLocalUsoForRequest();
      this.fillUnidadeMedidaForRequest();
      this.updateItem(cod, qnt, valorU, valorT, () => {
        self.fetchItensCompraInfoDB(() => {
          self.clearSelectedItensByCompra();
          self.selectItensByCompra();
          self.sumValorTotalCompra();
        });
      });
      this.cancelItem();
    },
    // Método que traz o valor total dos itens de uma compra.
    sumValorTotalCompra () {
      this.valorTotalCompra = 0;
      for (let item of this.selectedItensByCompra) {
        this.valorTotalCompra += item.valorTotal
      }

      this.checkValueStatus();
    },
    // Método para verificar se a soma dos itens passa do total da compra.
    checkValueStatus () {
      if (this.getCompraInfo.valorOriginal > this.valorTotalCompra) {
        this.valueStatus = 'grey-letter';
      } else if (this.getCompraInfo.valorOriginal < this.valorTotalCompra) {
        this.valueStatus = 'red-letter';
      } else {
        this.valueStatus = 'green-letter';
      }
    }
  },

  mounted () {
    this.fetchComprasInfoDB();
    this.fetchObrasInfoDB();
    this.fetchFornecedoresInfoDB();
    this.fetchItensCompraInfoDB();
    this.fetchProdutosInfoDB();
    this.fetchLocalUsoInfoDB();
    this.fetchUnidadeMedidaInfoDB();
    // setTimeout(() => {
    //   console.log(this.unidadeMedidaInfo);
    // }, 1000);
  }
}
</script>

<template>
  
  <!-- Header com o DropDown 'Obras' -->
  <header v-show="!this.showItems" class="header middle-margin">
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
            @click="obrasDropDownActions(obra.nome)"
            >{{ obra.nome }}</button>
        </li>
        <li><hr class="dropdown-divider"></li>
        <li>
          <button
            class="dropdown-item"
            type="button"
            @click="obrasDropDownActions('')"
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
  <div v-show="this.selectedObraNome" class="header middle-margin">
    <!-- Botão para adicionar Compra -->
    <button
      v-show="!this.showItems"
      type="button"
      class="btn btn-success light-green"
      data-bs-toggle="modal"
      data-bs-target="#insertModal"
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
          <button @click="cancel" type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
        </div>

        <div class="modal-body">

          <form action="POST">

            <!-- Obra -->
            <div class="mb-3">
              <label for="obra-input" class="bold">Obra:</label>
              <input
                type="text"
                class="form-control"
                id="obra-input"
                disabled
                v-model="selectedObraNome">
            </div>

            <!-- Fornecedor -->
            <div class="mb-3">
              <label for="fornecedor-select" class="bold">Fornecedor:</label>
              <select
                class="form-select"
                id="fornecedor-select"
                v-model="selectedFornecedorNome">
                <option
                  v-for="(fornecedor, i) in fornecedoresInfo" :key="i" :value="fornecedor.nome"
                  >{{ fornecedor.nome }}</option>
              </select>
            </div>

            <!-- Data da Compra -->
            <div class="mb-3">
              <label for="data-compra-input" class="form-label bold">Data da compra:</label>
              <input
                type="date"
                class="form-control"
                id="data-compra-input"
                v-model="dataCompra">
            </div>

            <!-- Data da Entrega -->
            <div class="mb-3">
              <label for="data-entrega-input" class="form-label bold">Data da entrega:</label>
              <input
                type="date"
                class="form-control"
                id="data-entrega-input"
                v-model="dataEntrega">
            </div>

            <!-- Data de Pagamento -->
            <div class="mb-3">
              <label for="data-pagamento-input" class="form-label bold">Data de pagamento:</label>
              <input
                type="date"
                class="form-control"
                id="data-pagamento-input"
                v-model="dataPagamento">
            </div>

            <!-- Data de Vencimento -->
            <div class="mb-3">
              <label for="data-vencimento-input" class="form-label bold">Data de vencimento:</label>
              <input
                type="date"
                class="form-control"
                id="data-vencimento-input"
                v-model="dataVencimento">
            </div>

            <!-- Valor Original -->
            <div class="mb-3">
              <label for="valor-original-input" class="form-label bold">Valor original:</label>
              <input
                type="number"
                step="0.01"
                title=''
                class="form-control"
                id="valor-original-input"
                placeholder="R$... (inserir apenas números e ponto)"
                v-model="valorOriginal">
            </div>

            <!-- Valor Desconto -->
            <div class="mb-3">
              <label for="desconto-input" class="form-label bold">Desconto:</label>
              <input
                type="number"
                step="0.01"
                title=''
                class="form-control"
                id="desconto-input"
                placeholder="R$... (inserir apenas números e ponto)"
                v-model="valorDesconto">
            </div>

            <!-- Valor Final -->
            <div class="mb-3">
              <label for="valor-final-input" class="form-label bold">Valor total:</label>
              <input
                type="number"
                step="0.01"
                title=''
                class="form-control"
                id="valor-final-input"
                placeholder="R$... (inserir apenas números e ponto)"
                v-model="valorFinal">
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
            type="button"
            class="btn btn-success  light-green"
            data-bs-dismiss="modal"
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
              <label for="id-input" class="form-label bold">Código:</label>
              <input
                type="text"
                class="form-control"
                id="id-input"
                disabled
                v-model="codigo">
            </div>

            <!-- Obra -->
            <div class="mb-3">
              <label for="obra-select" class="bold">Obra:</label>
              <select
                class="form-select"
                id="obra-select"
                v-model="selectedObraNome">
                <option
                  v-for="(obra, i) in obrasInfo" :key="i" :value="obra.nome"
                  >{{ obra.nome }}</option>
              </select>
            </div>

            <!-- Fornecedor -->
            <div class="mb-3">
              <label for="fornecedor-select" class="bold">Fornecedor:</label>
              <select
                class="form-select"
                id="fornecedor-select"
                v-model="selectedFornecedorNome">
                <option
                  v-for="(fornecedor, i) in fornecedoresInfo" :key="i" :value="fornecedor.nome"
                  >{{ fornecedor.nome }}</option>
              </select>
            </div>

            <!-- Data da Compra -->
            <div class="mb-3">
              <label for="data-compra-input" class="form-label bold">Data da compra:</label>
              <input
                type="date"
                class="form-control"
                id="data-compra-input"
                v-model="dataCompra">
            </div>

            <!-- Data da Entrega -->
            <div class="mb-3">
              <label for="data-entrega-input" class="form-label bold">Data da entrega:</label>
              <input
                type="date"
                class="form-control"
                id="data-entrega-input"
                v-model="dataEntrega">
            </div>

            <!-- Data de Pagamento -->
            <div class="mb-3">
              <label for="data-pagamento-input" class="form-label bold">Data de pagamento:</label>
              <input
                type="date"
                class="form-control"
                id="data-pagamento-input"
                v-model="dataPagamento">
            </div>

            <!-- Data de Vencimento -->
            <div class="mb-3">
              <label for="data-vencimento-input" class="form-label bold">Data de vencimento:</label>
              <input
                type="date"
                class="form-control"
                id="data-vencimento-input"
                v-model="dataVencimento">
            </div>

            <!-- Valor Original -->
            <div class="mb-3">
              <label for="valor-original-input" class="form-label bold">Valor original:</label>
              <input
                type="number"
                step="0.01"
                title=''
                class="form-control"
                id="valor-original-input"
                placeholder="R$... (inserir apenas números e ponto)"
                v-model="valorOriginal">
            </div>

            <!-- Valor Desconto -->
            <div class="mb-3">
              <label for="desconto-input" class="form-label bold">Desconto:</label>
              <input
                type="number"
                step="0.01"
                title=''
                class="form-control"
                id="desconto-input"
                placeholder="R$... (inserir apenas números e ponto)"
                v-model="valorDesconto">
            </div>

            <!-- Valor Final -->
            <div class="mb-3">
              <label for="valor-final-input" class="form-label bold">Valor total:</label>
              <input
                type="number"
                class="form-control"
                id="valor-final-input"
                placeholder="R$... (inserir apenas números e ponto)"
                v-model="valorFinal">
            </div>

          </form>
        </div>

        <div class="modal-footer">
          <button type="button" class="btn btn-secondary dark-grey" data-bs-dismiss="modal"
            @click="cancel"
          >Fechar</button>

          <button type="button" class="btn btn-success  light-green" data-bs-dismiss="modal"
            @click="updateCompraInfoDB(this.codigo, this.dataCompra, this.dataEntrega,
              this.dataPagamento, this.dataVencimento,
              this.valorOriginal, this.valorDesconto, this.valorFinal)"
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
          <button @click="cancelItem" type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
        </div>

        <div class="modal-body">

          <form action="POST">

            <!-- Compra -->
            <div class="mb-3">
              <label for="compra-input" class="bold">Compra:</label>
              <input
                type="text"
                class="form-control"
                id="compra-input"
                disabled
                v-model="compraCodForne">
            </div>

            <!-- Produto -->
            <div class="mb-3">
              <label for="produto-select" class="bold">Produto:</label>
              <select
                class="form-select"
                id="produto-select"
                v-model="selectedProdutoNome">
                <option
                  v-for="(produto, i) in produtosInfo" :key="i" :value="produto.nome"
                  >{{ produto.nome }}</option>
              </select>
            </div>

            <!-- localUso -->
            <div class="mb-3">
              <label for="local-uso-select" class="bold">Local de uso:</label>
              <select
                class="form-select"
                id="local-uso-select"
                v-model="selectedLocalUsoNome">
                <option
                  v-for="(localUso, i) in localUsoInfo" :key="i" :value="localUso.nomeLocalUsoObra"
                  >{{ localUso.nomeLocalUsoObra }}</option>
              </select>
            </div>

            <!-- Quantidade -->
            <div class="mb-3">
              <label for="quantidade-input" class="form-label bold">Quantidade:</label>
              <input
                type="number"
                class="form-control"
                id="quantidade-input"
                placeholder="(inserir apenas números)"
                v-model="quantidade">
            </div>

            <!-- Unidade de medida -->
            <div class="mb-3">
              <label for="unidade-medida-select" class="bold">Unidade de medida:</label>
              <select
                class="form-select"
                id="unidade-medida-select"
                v-model="selectedUnidadeMedida">
                <option
                  v-for="(unidadeMedida, i) in unidadeMedidaInfo" :key="i" :value="unidadeMedida.unidade"
                  >{{ unidadeMedida.unidade }}</option>
              </select>
            </div>

            <!-- Valor Unitario -->
            <div class="mb-3">
              <label for="valor-unitario-input" class="form-label bold">Valor unitário:</label>
              <input
                type="number"
                step="0.01"
                title=''
                class="form-control"
                id="valor-unitario-input"
                placeholder="R$... (inserir apenas números e ponto)"
                v-model="valorUnitario">
            </div>

            <!-- Valor Total -->
            <div class="mb-3">
              <label for="valor-total-input" class="form-label bold">Valor total:</label>
              <input
                type="number"
                step="0.01"
                title=''
                class="form-control"
                id="valor-total-input"
                placeholder="R$... (inserir apenas números e ponto)"
                v-model="valorTotal">
            </div>

          </form>
        </div>

        <div class="modal-footer">
          <button
            type="button"
            class="btn btn-secondary dark-grey"
            data-bs-dismiss="modal"
            @click="cancelItem"
          >Fechar</button>
          <button
            type="button"
            class="btn btn-success  light-green"
            data-bs-dismiss="modal"
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
              <label for="codigo-input" class="form-label bold">Código:</label>
              <input
                type="text"
                class="form-control"
                id="codigo-input"
                disabled
                v-model="codigoItem">
            </div>

            <!-- Produto -->
            <div class="mb-3">
              <label for="produto-input" class="form-label bold">Produto:</label>
              <input
                type="text"
                class="form-control"
                id="produto-input"
                v-model="selectedProdutoNome"
                disabled>
            </div>

            <!-- Local de uso -->
            <div class="mb-3">
              <label for="local-uso-select" class="bold">Local de uso:</label>
              <select
                class="form-select"
                id="local-uso-select"
                v-model="selectedLocalUsoNome">
                <option
                  v-for="(localUso, i) in localUsoInfo" :key="i" :value="localUso.nomeLocalUsoObra"
                  >{{ localUso.nomeLocalUsoObra }}</option>
              </select>
            </div>

            <!-- Quantidade -->
            <div class="mb-3">
              <label for="quantidade-input" class="form-label bold">Quantidade:</label>
              <input
                type="number"
                class="form-control"
                id="quantidade-input"
                placeholder="R$... (inserir apenas números e ponto)"
                v-model="quantidade">
            </div>

            <!-- Unidade de medida -->
            <div class="mb-3">
              <label for="unidade-medida-select" class="bold">Unidade de medida:</label>
              <select
                class="form-select"
                id="unidade-medida-select"
                v-model="selectedUnidadeMedida">
                <option
                  v-for="(unidadeMedida, i) in unidadeMedidaInfo" :key="i" :value="unidadeMedida.unidade"
                  >{{ unidadeMedida.unidade }}</option>
              </select>
            </div>        

            <!-- Valor Unitario -->
            <div class="mb-3">
              <label for="valor-unitario-input" class="form-label bold">Valor unitário:</label>
              <input
                type="number"
                step="0.01"
                title=""
                class="form-control"
                id="valor-unitario-input"
                placeholder="R$... (inserir apenas números e ponto)"
                v-model="valorUnitario">
            </div>

            <!-- Valor Total -->
            <div class="mb-3">
              <label for="valor-total-input" class="form-label bold">Valor total:</label>
              <input
                type="number"
                step="0.01"
                title=""
                class="form-control"
                id="valor-total-input"
                placeholder="R$... (inserir apenas números e ponto)"
                v-model="valorTotal">
            </div>

          </form>
        </div>

        <div class="modal-footer">
          <button type="button" class="btn btn-secondary dark-grey" data-bs-dismiss="modal"
            @click="cancelItem"
          >Fechar</button>

          <button type="button" class="btn btn-success  light-green" data-bs-dismiss="modal"
            @click="updateItemInfoDB(this.codigoItem, this.quantidade, this.valorUnitario, this.valorTotal)"
          >Salvar</button>
        </div>
      </div>
    </div>
  </div>

  <!-- Tabelas condicionais baseadas na escolha de Itens ou Compras -->
  <!-- Tabela de Compras -->
  <main v-show="!showItems" class="middle-margin table-responsive">
    <table class="table table-hover">
      <thead>
        <tr>
          <th scope="col">Código</th>
          <!-- <th scope="col">Obra</th> -->
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
          <!-- <td>{{ compra.obra.nome }}</td> -->
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
              @click="fillUpdateDeleteModal(compra.codigo, compra.obra, compra.fornecedor,
                compra.dataCompra, compra.dataEntrega, compra.dataPagamento, compra.dataVencimento,
                compra.valorOriginal, compra.valorDesconto, compra.valorFinal, compra.fornecedor.nome)"
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
              @click="fillUpdateDeleteModal(compra.codigo)"
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
              @click="fillUpdateDeleteItemModal(item.codigo, item.compra, item.produto, item.localUso,
                item.unidadeMedida, item.quantidade, item.valorUnitario, item.valorTotal,
                item.produto.nome, item.localUso.nomeLocalUsoObra, item.unidadeMedida.unidade)"
            ><img src="../assets/imagens/editar.png" alt="lata de lixo"></button>
            <button
              type="button"
              class="btn btn-light btn-sm small"
              title="Excluir"
              data-bs-toggle="modal"
              data-bs-target="#deleteItemModal"
              @click="fillUpdateDeleteItemModal(item.codigo)"
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

.red-letter {
  color: red;
}

.green-letter {
  color:green;
}

.grey-letter {
  color: #212529;
}
</style>