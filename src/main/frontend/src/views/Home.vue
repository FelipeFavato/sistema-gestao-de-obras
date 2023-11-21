<script>

import { useRouter } from 'vue-router';
import axios from 'axios';

export default {
  data () {
    return {
      // Arrays auxiliares
      usuarios: [],
      custoLocalUsoInfo: [],
      gastoAcumuladoInfo: [],
      gastoPorSocioInfo: [],
      gastoPorFornecedor: [],
      // Variaveis auxiliares
      useRouter: useRouter(),
      httpStatus: '',
      // Variaveis de requisição
      localStorageToken: null,
    }
  },

  methods: {
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
      axios.get('/api/dashboard/valorlocaluso',
      {
        headers: {
          Authorization: this.localStorageToken
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
      axios.get('/api/dashboard/gastosocio',
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
      axios.get('/api/dashboard/gastofornecedor',
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
        width: 600,
        height: 400,
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
        width: 600,
        height: 400,
        showlegend: false,
        margin: {
          "t": 20,
          "b": 10,
          "l": 20,
          "r": 20
        }
      };

      const config = {
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
        width: 600,
        height: 400,
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
        displayModeBar: false
      };

      Plotly.newPlot('gastoPorFornecedor', data, layout, config)
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
    this.fetchCustoLocalUsoInfoDB(() => {
      this.genCustoLocalUsoGraph();
    });
    this.fetchGastoPorSocioInfo(() => {
      this.genGastoPorSocioGraph();
    });
    this.fetchGastoPorFornecedorInfo(() => {
      this.genGastoPorFornecedorGraph();
    })
  }
}


</script>

<template>

  <main>
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
            Gasto por fornecedor
          </div>
          <div class="card-body">
            <div id="gastoPorFornecedor"></div>
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

</style>