<script>

import { useRouter } from 'vue-router';
import axios from 'axios';

export default {
  data () {
    return {
      // Arrays auxiliares
      obrasInfo: [],
      usuarios: [],
      custoLocalUsoInfo: [],
      gastoAcumuladoInfo: [],
      gastoPorSocioInfo: [],
      gastoPorFornecedor: [],
      mdoOrcamentoInfo: [],
      // Variaveis auxiliares
      useRouter: useRouter(),
      httpStatus: '',
      selectedObraNome: '',
      selectedObraID: '',
      // Variaveis de requisição
      localStorageToken: null,
    }
  },

  methods: {
    // Métodos de validação e login
    redirectToLogin () {
      this.useRouter.push('/login')
    },
    setHttpStatusCode (successError) {
      this.httpStatus = successError
    },
    getLocalStorageToken () {
      this.localStorageToken = localStorage.getItem('token');
    },
    validateLogin () {
      !this.localStorageToken ? this.redirectToLogin() : null;
    },
    validateHttpStatus (status) {
      this.setHttpStatusCode(status);
      this.httpStatus === 403 ? this.redirectToLogin(): null;
    },
    fillObraForRequest () {
      for (let obra of this.obrasInfo) {
        if (obra.nome === this.selectedObraNome) {
          this.selectedObraID = obra.codigo;
        }
      }
    },
    // Métodos de comportamento da página
    graphGenerator () {
      const self = this;
      // this.fillObraForRequest();
      this.fetchCustoLocalUsoInfoDB(() => {
        self.genCustoLocalUsoGraph();
      });
      this.fetchGastoPorSocioInfo(() => {
        self.genGastoPorSocioGraph();
      });
      this.fetchGastoPorFornecedorInfo(() => {
        self.genGastoPorFornecedorGraph();
      });
      this.fetchMDOOrcamentoInfo(() => {
        self.genMDOOrcamentoGraph();
      });
    },
    selectObraNome(nomeObra) {
      this.selectedObraNome = nomeObra;
    },
    ObraDropDownActions(nomeObra) {
      this.selectObraNome(nomeObra);
      this.fillObraForRequest();
      this.graphGenerator();
    },
    // Métodos de BUSCA e GERAÇÂO de dados.
    fetchObrasInfoDB (callback) {
      axios.get('/api/dashboard/obracodnome',
      {
        headers: {
          Authorization: this.localStorageToken
        }
      }).then(res => {
        this.obrasInfo = res.data;
        if (callback) callback();
      }).catch(error => {

      });
    },
    fetchUsuarioInfoDB () {
      axios.get('/api/usuario',
      {
        headers: {
          Authorization: this.localStorageToken
        }
      }).then(({ data, status }) => {
          this.usuarios = data;
          this.setHttpStatusCode(status);
        }).catch(error => {
          this.validateHttpStatus(error.response.status);
        })
    },
    fetchCustoLocalUsoInfoDB (callback) {
      axios.get(`/api/dashboard/valorlocaluso?codigo=${this.selectedObraID}`,
      {
        headers: {
          Authorization: `Bearer ${this.localStorageToken}`
        }
      }).then(res => {
        this.custoLocalUsoInfo = res.data;
        if (callback) callback();
      }).catch(error => {
        this.validateHttpStatus(error.response.status);
      })
    },
    fetchGastoAcumuladoInfo (callback) {
      axios.get('/api/dashboard/acumuladogastos',
      {
        headers: {
          Authorization: this.localStorageToken
        }
      }).then(res => {
        this.gastoAcumuladoInfo = res.data;
        if (callback) callback();
      }).catch(error => {

      })
    },
    fetchGastoPorSocioInfo (callback) {
      axios.get(`/api/dashboard/gastosocio?codigo=${this.selectedObraID}`,
      {
        headers: {
          Authorization: this.localStorageToken
        }
      }).then(res => {
        this.gastoPorSocioInfo = res.data;
        if (callback) callback();
      }).catch(error => {
        this.validateHttpStatus(error.response.status);
      })
    },
    fetchGastoPorFornecedorInfo (callback) {
      axios.get(`/api/dashboard/gastofornecedor?codigo=${this.selectedObraID}`,
      {
        headers: {
          Authorization: this.localStorageToken
        }
      }).then(res => {
        this.gastoPorFornecedor = res.data;
        if (callback) callback();
      }).catch(error => {
        this.validateHttpStatus(error.response.status);
      })
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
    genCustoLocalUsoGraph () {
      const locais = [];
      const valores = [];
      const par = [];
      const text = [];

      for (let obj of this.custoLocalUsoInfo) {
        locais.push(obj['nomeLocalUsoObra']);
        valores.push(obj['valorTotal']);
        par.push('')
        text.push(this.fixCurrency(obj['valorTotal']))
      }

      const data = [{
        type: "treemap",
        labels: locais,
        values: valores,
        parents: par,
        text: text,
        textinfo: "label+text+percent parent",
        hoverinfo: "label+text"
      }];

      const layout = {
        // paper_bgcolor: "#f0f0f0",
        // width: 600,
        // height: 400,
        margin: {
          t: 0,
          l: 5,
          b: 5,
          r: 5
        },
        // title: {
        //   text: "Custo por local",
        //   font: {
        //     color: 'black',
        //     size: 22
        //   },
        //   pad: {
        //     t: 10
        //   }
        // },
      };

      const config = {
        responsive: true,
        displayModeBar: false
      };

      Plotly.newPlot('custoLocalUso', data, layout, config)
    },
    genGastoPorSocioGraph () {
      const values = [];
      const labels = [];
      const text = [];

      for (let obj of this.gastoPorSocioInfo) {
        values.push(obj['valorFinal']);
        labels.push(obj['nomeSocio'].split(' ')[0]);
        text.push(this.fixCurrency(obj['valorFinal']));
      }

      const data = [{
        values: values,
        labels: labels,
        type: 'pie',
        text: text,
        textinfo: "label+text+percent",
        hoverinfo: "label+text"
      }];

      const layout = {
        // width: 600,
        // height: 400,
        showlegend: false,
        margin: {
          "t": 20,
          "b": 10,
          "l": 20,
          "r": 20
        }
      };

      const config = {
        responsive: true,
        displayModeBar: false
      };

      Plotly.newPlot('gastoPorSocio', data, layout, config);
    },
    genGastoPorFornecedorGraph () {
      const locais = [];
      const valores = [];
      const par = [];
      const text = [];

      for (let obj of this.gastoPorFornecedor) {
        locais.push(obj['nomeFornecedor']);
        valores.push(obj['valorFinal']);
        par.push('')
        text.push(this.fixCurrency(obj['valorFinal']))
      }

      const data = [{
        type: "treemap",
        labels: locais,
        values: valores,
        parents: par,
        text: text,
        textinfo: "label+text+percent parent",
        hoverinfo: "label+text"
      }];

      const layout = {
        // paper_bgcolor: "#f0f0f0",
        // width: 600,
        // height: 400,
        margin: {
          t: 0,
          l: 5,
          b: 5,
          r: 5
        },
        // title: {
        //   text: "Custo por local",
        //   font: {
        //     color: 'black',
        //     size: 22
        //   },
        //   pad: {
        //     t: 10
        //   }
        // },
      };

      const config = {
        responsive: true,
        displayModeBar: false
      };

      Plotly.newPlot('gastoPorFornecedor', data, layout, config)
    },
    genMDOOrcamentoGraph () {
      const dadosOrcamento = this.mdoOrcamentoInfo[0];
      const custoPrevisto = dadosOrcamento.custoPrevisto;
      const custoMaoDeObra = dadosOrcamento.custoMaoDeObra;
      const orcamentoMaoDeObra = dadosOrcamento.orcamentoMaoDeObra;
      const valorGastos = dadosOrcamento.valorTotal;
      const comprometido = orcamentoMaoDeObra + valorGastos;
      const disponivel = custoPrevisto - comprometido;

      const trace1 = {
        x: ['Custo previsto'],
        y: [orcamentoMaoDeObra],
        name: 'Orçamento mão de obra',
        type: 'bar',
        text: [this.fixCurrency(orcamentoMaoDeObra)],
        hoverinfo: "name+text+percent",
        textinfo: "name+text",
        // marker: {
        //   color: 'black'
        // }
      };

      const trace2 = {
        x: ['Custo previsto'],
        y: [valorGastos],
        name: 'Gastos',
        type: 'bar',
        text: [this.fixCurrency(valorGastos)],
        hoverinfo: "name+text",
      };

      const trace3 = {
        x: ['Custo previsto'],
        y: [disponivel],
        name: 'Disponivel',
        type: 'bar',
        text: [this.fixCurrency(disponivel)],
        hoverinfo: "name+text",
      }

      const data = [trace1, trace2, trace3];

      const layout = {
        barmode: 'stack',
        // showlegend: false
      };

      const config = {
        responsive: true,
        displayModeBar: false
      }

      Plotly.newPlot('orcamento', data, layout, config);

    },
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
    }
  },

  mounted () {
    this.getLocalStorageToken();
    this.validateLogin();
    this.fetchObrasInfoDB();
    // this.fetchCustoLocalUsoInfoDB(() => {
    //   this.genCustoLocalUsoGraph();
    // });
    // this.fetchGastoPorSocioInfo(() => {
    //   this.genGastoPorSocioGraph();
    // });
    // this.fetchGastoPorFornecedorInfo(() => {
    //   this.genGastoPorFornecedorGraph();
    // });
    // this.fetchMDOOrcamentoInfo(() => {
    //   this.genMDOOrcamentoGraph();
    // });
  }
}


</script>

<template>

  <main>

    <!-- DropDown com as opções de Obra para selecionar e renderizar os gráficos relativos aquela obra -->
    <header class="header middle-margin">
      <div class="dropdown">
        <button class="btn btn-secondary dropdown-toggle" type="button" data-bs-toggle="dropdown" aria-expanded="false">
          {{ selectedObraNome ? selectedObraNome : 'Obras' }}
        </button>
        <ul class="dropdown-menu">
          <li v-for="(obra, i) in obrasInfo" :key="i">
            <button
              class="dropdown-item"
              type="button"
              @click="ObraDropDownActions(obra.nome)"
            >{{ obra.nome }}</button>
          </li>
        </ul>
      </div>
    </header>

    <div v-if="this.selectedObraNome">
      <div class="row">
        <div class="col-sm-6 mb-3 mb-sm-0 pad-10">
          <div class="card">
            <div class="card-header title">
              Orçamento
            </div>
            <div class="card-body border">
              <div id="orcamento"></div>
            </div>
          </div>
        </div>
        <div class="col-sm-6 pad-10">
          <div class="card">
            <div class="card-header title">
              Gasto por sócio
            </div>
            <div class="card-body">
              <div id="gastoPorSocio"></div>
            </div>
          </div>
        </div>
      </div>

      <div class="row">
        <div class="col-sm-6 mb-3 mb-sm-0 pad-10">
          <div class="card">
            <div class="card-header title">
              Custo por local
            </div>
            <div class="card-body">
              <div id="custoLocalUso"></div>
            </div>
          </div>
        </div>
        <div class="col-sm-6 pad-10">
          <div class="card">
            <div class="card-header title">
              Custo por fornecedor
            </div>
            <div class="card-body">
              <div id="gastoPorFornecedor"></div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </main>
  
</template>

<style setup>

.title {
  display: flex;
  justify-content: center;
  font-weight: bold;
  font-family: monospace;
}

.border {
  border: 1px solid rgba(153, 153, 153, 1);
}

.margin-10 {
  margin: 10px;
}

.pad-10 {
  padding-left: 25px;
  padding-right: 25px;
  padding-top: 10px;
  padding-bottom: 10px;
}

.graph {
  border: 1px solid rgba(153, 153, 153, 1);
  width: 602px;
  /* border-radius: 20px; */
  margin-top: 15px;
  margin-left: 15px;
}

@media only screen and (max-width: 600px) {
  .graphics {
    width: 300px;
    height: 200px;
  }
}

.header {
  display: flex;
  justify-content: space-between;
  padding-bottom: 5px;
  /* width: 100%; */
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