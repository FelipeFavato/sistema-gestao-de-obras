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
      descontoInfo: [],
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
    // Métodos de comportamento da página
    fillObraForRequest () {
      for (let obra of this.obrasInfo) {
        if (obra.nome === this.selectedObraNome) {
          this.selectedObraID = obra.codigo;
        }
      }
    },
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
      this.fetchGastoAcumuladoInfo(() => {
        self.genGastoAcumuladoGraph();
        self.genGastoMensalGraph();
      })
    },
    selectObraNome(nomeObra) {
      this.selectedObraNome = nomeObra;
    },
    ObraDropDownActions(nomeObra) {
      this.selectObraNome(nomeObra);
      this.fillObraForRequest();
      this.graphGenerator();
    },
    // Métodos de BUSCA e GERAÇÃO de dados.
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
      axios.get(`/api/dashboard/acumuladogastos?codigo=${this.selectedObraID}`,
      {
        headers: {
          Authorization: this.localStorageToken
        }
      }).then(res => {
        this.gastoAcumuladoInfo = res.data;
        if (callback) callback();
      }).catch(error => {
        this.validateHttpStatus(error.response.status);
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
    fetchDesconto (callback) {
      axios.get(`/api/dashboard/somadesconto?codigo=${this.selectedObraID}`,
      {
        headers: {
          Authorization: this.localStorageToken
        },
      }).then(res => {
        this.descontoInfo = res.data;
        if (callback) callback();
      }).catch(error => {
        this.validateHttpStatus(error.response.status);
      })
    },
    genCustoLocalUsoGraph () {
      const locais = [];
      const valores = [];
      const par = [];
      const text = [];

      for (let obj of this.custoLocalUsoInfo) {
        locais.push(`${obj['nomeLocalUsoObra']} - ${obj['nomeFornecedor']}`);
        valores.push(obj['valorTotal']);
        par.push(obj['nomeLocalUsoObra']);
        text.push(this.fixCurrency(obj['valorTotal']))
      }

      const resultado = this.custoLocalUsoInfo.reduce((acc, item) => {
        let found = acc.find(element => element.nomeLocalUsoObra === item.nomeLocalUsoObra);

        if (found) {
            found.valorTotal += item.valorTotal;
        } else {
            acc.push({
                nomeLocalUsoObra: item.nomeLocalUsoObra,
                valorTotal: item.valorTotal
            });
        }

        return acc;
      }, []);

      for (let object of resultado) {
        locais.push(object['nomeLocalUsoObra']);
        valores.push(0);
        par.push('');
        text.push(this.fixCurrency(object['valorTotal']));
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
        locais.push(`${obj['nomeFornecedor']} - ${obj['nomeProduto']}`);
        valores.push(obj['valorTotal']);
        par.push(obj['nomeFornecedor'])
        text.push(this.fixCurrency(obj['valorTotal']))
      }

      const resultado = this.gastoPorFornecedor.reduce((acc, item) => {
        let found = acc.find(element => element.nomeFornecedor === item.nomeFornecedor);

        if (found) {
            found.valorTotal += item.valorTotal;
        } else {
            acc.push({
                nomeFornecedor: item.nomeFornecedor,
                valorTotal: item.valorTotal
            });
        }

        return acc;
      }, []);

      for (let object of resultado) {
        locais.push(object['nomeFornecedor']);
        valores.push(0);
        par.push('');
        text.push(this.fixCurrency(object['valorTotal']));
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
      const custoMDO = dadosOrcamento.custoMaoDeObra;
      const custoP = dadosOrcamento.custoPrevisto;
      const pagoMDO = dadosOrcamento.pagoMaoDeObra;
      const valorD = dadosOrcamento.valorDesconto;
      const valorG = dadosOrcamento.valorGastos;
      const comprometidoMDO = custoMDO - pagoMDO;
      const disponivel = custoP - (valorG + custoMDO);

      const trace1 = {
        x: ['Custo previsto'],
        y: [pagoMDO],
        name: 'Pago - Mão de obra',
        type: 'bar',
        text: [this.fixCurrency(pagoMDO)],
        hoverinfo: "name+text",
        marker: {
          color: '#1f77b4'
        }
      };
      
      const trace2 = {
        x: ['Custo previsto'],
        y: [comprometidoMDO],
        name: 'Comprometido - Mão de obra',
        type: 'bar',
        text: [this.fixCurrency(comprometidoMDO)],
        hoverinfo: "name+text+percent",
        textinfo: "name+text",
        marker: {
          color: 'lightblue'
        }
      };

      const trace3 = {
        x: ['Custo previsto'],
        y: [valorD],
        name: 'Desconto',
        type: 'bar',
        text: [this.fixCurrency(valorD)],
        hoverinfo: "name+text",
        marker: {
          color: '#d62728'
        }
      };

      const trace4 = {
        x: ['Custo previsto'],
        y: [valorG],
        name: 'Custos - Material/Serviço',
        type: 'bar',
        text: [this.fixCurrency(valorG)],
        hoverinfo: "name+text",
        marker: {
          color: '#ff7f0e'
        }
      };

      const trace5 = {
        x: ['Custo previsto'],
        y: [disponivel],
        name: 'Saldo para investimento',
        type: 'bar',
        text: [this.fixCurrency(disponivel)],
        hoverinfo: "name+text",
        marker: {
          color: '#2ca02c'
        }
      };

      const data = [trace1, trace2, trace3, trace4, trace5];

      const layout = {
        barmode: 'stack',
        legend: {
          orientation: 'h'
        }
        // showlegend: false
      };

      const config = {
        responsive: true,
        displayModeBar: false,
      }

      Plotly.newPlot('orcamento', data, layout, config);

    },
    genGastoAcumuladoGraph () {
      const mesesVisualizacao = [];
      let meses = [];
      let somaGasMes = 0;
      let gas = [];
      let gasText = [];
      let somaMaoDeObraMes = 0;
      let maoDeObra = [];
      let maoDeObraText = [];
      let somaGasTMes = 0;
      let gasT = [];
      let gasTText = [];

      for (let obj of this.gastoAcumuladoInfo) {
        meses.push(obj['dataVencimento']);
        mesesVisualizacao.push(this.fixDateAxis(obj['dataVencimento']))
      }
            
      for (let mes of meses) {
        for (let objeto of this.gastoAcumuladoInfo) {
          if (objeto['dataVencimento'] === mes) {
            somaGasMes += objeto['gastos'];
            somaMaoDeObraMes += objeto['gastoMaoDeObra'];
            somaGasTMes += objeto['gastoTotal']
          }
        }
        gas.push(somaGasMes);
        gasText.push(`${this.fixDate(mes)} - ${this.fixCurrency(somaGasMes)}`);
        maoDeObra.push(somaMaoDeObraMes);
        maoDeObraText.push(`${this.fixDate(mes)} - ${this.fixCurrency(somaMaoDeObraMes)}`);
        gasT.push(somaGasTMes)
        gasTText.push(`${this.fixDate(mes)} - ${this.fixCurrency(somaGasTMes)}`);
      }

      const gastos = {
        x: mesesVisualizacao,
        y: gas,
        type: 'bar',
        name: 'Custo - Material/Serviço',
        hovertext: gasText,
        hoverinfo: 'name+text'
      };

      const MDO = {
        x: mesesVisualizacao,
        y: maoDeObra,
        type: 'bar',
        name: 'Pago - Mão de obra',
        hovertext: maoDeObraText,
        hoverinfo: 'name+text'
      };

      const total = {
        x: mesesVisualizacao,
        y: gasT,
        type: 'scatter',
        name: 'Gasto total',
        hovertext: gasTText,
        hoverinfo: 'name+text'
      }

      const data = [gastos, MDO, total];

      const layout = {
        barmode: 'group',
        legend: {
          orientation: 'h'
        }
        // showlegend: false
      };

      const config = {
        responsive: true,
        displayModeBar: false
      };

      Plotly.newPlot('gastoacumulado', data, layout, config);
    },
    genGastoMensalGraph() {
      const meses = [];
      const gas = [];
      const gasText = [];
      const maoDeObra = [];
      const maoDeObraText = [];
      const gasT = [];
      const gasTText = [];

      for (let obj of this.gastoAcumuladoInfo) {
        meses.push(this.fixDateAxis(obj['dataVencimento']));
        gas.push(obj['gastos']);
        gasText.push(`${this.fixDate(obj['dataVencimento'])} - ${this.fixCurrency(obj['gastos'])}`);
        maoDeObra.push(obj['gastoMaoDeObra']);
        maoDeObraText.push(`${this.fixDate(obj['dataVencimento'])} - ${this.fixCurrency(obj['gastoMaoDeObra'])}`);
        gasT.push(obj['gastoTotal']);
        gasTText.push(`${this.fixDate(obj['dataVencimento'])} - ${this.fixCurrency(obj['gastoTotal'])}`);
      }

      const gastos = {
        x: meses,
        y: gas,
        type: 'bar',
        name: 'Custo - Material/Serviço',
        hovertext: gasText,
        hoverinfo: 'name+text'
      };

      const MDO = {
        x: meses,
        y: maoDeObra,
        type: 'bar',
        name: 'Pago - Mão de obra',
        hovertext: maoDeObraText,
        hoverinfo: 'name+text'
      };

      const total = {
        x: meses,
        y: gasT,
        type: 'scatter',
        name: 'Gasto total',
        hovertext: gasTText,
        hoverinfo: 'name+text'
      }

      const data = [gastos, MDO, total];

      const layout = {
        barmode: 'group',
        legend: {
          orientation: 'h'
        }
        // showlegend: false
      };

      const config = {
        responsive: true,
        displayModeBar: false
      };

      Plotly.newPlot('gastomensal', data, layout, config);
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
    },
    fixDate (date) {
      const partes = date.split('-');

      switch (partes[1]) {
        case '01':
          return `Jan/${partes[0]}`;
        case '02':
          return `Fev/${partes[0]}`;
        case '03':
          return `Abr/${partes[0]}`;
        case '04':
          return `Mar/${partes[0]}`;
        case '05':
          return `Mai/${partes[0]}`;
        case '06':
          return `Jun/${partes[0]}`;
        case '07':
          return `Jul/${partes[0]}`;
        case '08':
          return `Ago/${partes[0]}`;
        case '09':
          return `Set/${partes[0]}`;
        case '10':
          return `Out/${partes[0]}`;
        case '11':
          return `Nov/${partes[0]}`;
        case '12':
          return `Dez/${partes[0]}`;
        }
    },
    fixDateAxis (date) {
      const partes = date.split('-');
      const year = partes[0].split('');

      switch (partes[1]) {
        case '01':
          return `Jan/${year[2]}${year[3]}`;
        case '02':
          return `Fev/${year[2]}${year[3]}`;
        case '03':
          return `Abr/${year[2]}${year[3]}`;
        case '04':
          return `Mar/${year[2]}${year[3]}`;
        case '05':
          return `Mai/${year[2]}${year[3]}`;
        case '06':
          return `Jun/${year[2]}${year[3]}`;
        case '07':
          return `Jul/${year[2]}${year[3]}`;
        case '08':
          return `Ago/${year[2]}${year[3]}`;
        case '09':
          return `Set/${year[2]}${year[3]}`;
        case '10':
          return `Out/${year[2]}${year[3]}`;
        case '11':
          return `Nov/${year[2]}${year[3]}`;
        case '12':
          return `Dez/${year[2]}${year[3]}`;
        }
    }
  },

  mounted () {
    this.getLocalStorageToken();
    this.validateLogin();
    this.fetchObrasInfoDB();
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
              Gasto acumulado
            </div>
            <div class="card-body">
              <div id="gastoacumulado"></div>
            </div>
          </div>
        </div>
        <div class="col-sm-6 pad-10">
          <div class="card">
            <div class="card-header title">
              Gasto mensal
            </div>
            <div class="card-body">
              <div id="gastomensal"></div>
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