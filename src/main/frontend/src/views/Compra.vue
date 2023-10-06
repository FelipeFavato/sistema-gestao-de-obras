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
import axios from 'axios';

export default {
  data () {
    return {
      comprasInfo: [],
      obrasInfo: [],
      fornecedoresInfo: [],
      selectedComprasByObra: [],
      selectedObraNome: '',
      selectedFornecedorNome: '',
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
    fetchComprasInfoDB () {
      axios.get("/api/compra").then(
        (res) => this.comprasInfo = res.data.sort((s1, s2) => s1.codigo - s2.codigo))
    },
    // Método GET - Obras.
    fetchObrasInfoDB () {
      axios.get("/api/obra").then(
        (res) => this.obrasInfo = res.data.sort((s1, s2) => s1.codigo - s2.codigo))
    },
    // Método GET - Fornecedores.
    fetchFornecedoresInfoDB () {
      axios.get("/api/fornecedor").then(
        (res) => this.fornecedoresInfo = res.data.sort((s1, s2) => s1.codigo - s2.codigo))
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
    // Método para preencher o objeto "this.fornecedor" com o fornecedor correto
    // para a requisição.
    fillFornecedorForRequest () {
      for (let chosenFornecedor of this.fornecedoresInfo) {
        if (chosenFornecedor.nome === this.selectedFornecedorNome) {
          this.fornecedor = chosenFornecedor;
        }
      }
    },
    // Método para popular o array "selectedComprasByObra".
    selectComprasByObra () {
      for (let compra of this.comprasInfo) {
        if (compra.obra.nome === this.selectedObraNome) {
          this.selectedComprasByObra.push(compra);
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
    createCompra () {
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
        }).then(() => this.fetchComprasInfoDB());
    },
    // Chama o método 'createCompra' e repopula a lista correta.
    createCompraInfoDB () {
      this.fillObraForRequest();
      this.fillFornecedorForRequest();
      this.createCompra();
      this.cancel();
      setTimeout(() => {
        this.emptySelectedComprasByObraArray();
        this.selectComprasByObra();
      }, 2000);
    },
    // Remove Compra selecionada.
    removeCompra (codigo) {
      axios.delete("/api/compra",
        {
          headers: {
            Authorization: ''
          },
          data: {
            codigo: Number(codigo)
          }
        }).then(() => this.fetchComprasInfoDB());
    },
    // Chama o método 'removeCompra' e repopula a lista correta.
    removeCompraFromDB (codigo) {
      this.removeCompra(codigo);
      this.cancel();
      setTimeout(() => {
        this.emptySelectedComprasByObraArray();
        this.selectComprasByObra();
      }, 2000);
    },
    // Método para atualizar uma Compra selecionada.
    updateCompra (cod, dataC, dataE, dataP, dataV, valorO, valorD, valorF) {
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
      }).then(() => this.fetchComprasInfoDB());
    },
    // Chama o método 'updateCompra' e repopula a lista corretamente.
    updateCompraInfoDB (cod, dataC, dataE, dataP, dataV, valorO, valorD, valorF) {
      this.fillObraForRequest();
      this.fillFornecedorForRequest();
      this.updateCompra(cod, dataC, dataE, dataP, dataV, valorO, valorD, valorF);
      this.cancel();
      setTimeout(() => {
        this.emptySelectedComprasByObraArray();
        this.selectComprasByObra();
      }, 2000);
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
    }
  },

  mounted () {
    this.fetchComprasInfoDB();
    this.fetchObrasInfoDB();
    this.fetchFornecedoresInfoDB();
  }
}
</script>

<template>
  
  <!-- Header com o DropDown 'Obras' -->
  <header class="header middle-margin">
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

  <!-- Elementos condicionais baseado na escolha da Obra -->
  <div class="header middle-margin">
    <!-- Botão para adicionar Compra -->
    <button
      v-show="this.selectedObraNome"
      type="button"
      class="btn btn-success light-green"
      data-bs-toggle="modal"
      data-bs-target="#insertModal"
    >+ Nova Compra</button>
  </div>

  <!-- DeleteModal -->
  <div class="modal" id="deleteModal" tabindex="-1" aria-labelledby="deleteModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-dialog-scrollable modal-sm">
      <div class="modal-content">
        <div class="modal-header">
          <h1 class="modal-title fs-5" id="deleteModalLabel">Realmente deseja excluir?</h1>
          <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
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

  <!-- InsertModal -->
  <div class="modal" id="insertModal" tabindex="-1" aria-labelledby="insertModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-dialog-scrollable modal-xl">
      <div class="modal-content">
        <div class="modal-header">
          <h1 class="modal-title fs-5" id="insertModalLabel">Nova Compra</h1>
          <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
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
                type="text"
                class="form-control"
                id="valor-original-input"
                placeholder="R$... (inserir apenas números e ponto)"
                v-model="valorOriginal">
            </div>

            <!-- Valor Desconto -->
            <div class="mb-3">
              <label for="desconto-input" class="form-label bold">Desconto:</label>
              <input
                type="text"
                class="form-control"
                id="desconto-input"
                placeholder="R$... (inserir apenas números e ponto)"
                v-model="valorDesconto">
            </div>

            <!-- Valor Final -->
            <div class="mb-3">
              <label for="valor-final-input" class="form-label bold">Valor total:</label>
              <input
                type="text"
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

  <!-- UpdateModal -->
  <div class="modal" id="updateModal" tabindex="-1" aria-labelledby="updateModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-dialog-scrollable">
      <div class="modal-content">
        <div class="modal-header">
          <h1 class="modal-title fs-5" id="updateModalLabel">Editar Compra</h1>
          <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
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
                type="text"
                class="form-control"
                id="valor-original-input"
                placeholder="R$... (inserir apenas números e ponto)"
                v-model="valorOriginal">
            </div>

            <!-- Valor Desconto -->
            <div class="mb-3">
              <label for="desconto-input" class="form-label bold">Desconto:</label>
              <input
                type="text"
                class="form-control"
                id="desconto-input"
                placeholder="R$... (inserir apenas números e ponto)"
                v-model="valorDesconto">
            </div>

            <!-- Valor Final -->
            <div class="mb-3">
              <label for="valor-final-input" class="form-label bold">Valor total:</label>
              <input
                type="text"
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

  <!-- Tabela -->
  <main class="middle-margin table-responsive">
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