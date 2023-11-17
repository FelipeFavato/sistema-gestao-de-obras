<script>

import { useRouter } from 'vue-router';
import axios from 'axios';

export default {
  data () {
    return {
      usuarios: [],
      useRouter: useRouter(),
      httpStatus: '',
      localStorageToken: null
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

      })
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
        paper_bgcolor: "#f0f0f0",
        width: 600,
        height: 400,
        margin: {
          t: 40,
          l: 15,
          b: 15,
          r: 15
        },
        title: {
          text: "Custo por local",
          font: {
            color: 'black',
            size: 22
          },
          pad: {
            t: 10
          }
        },
      };

      const config = {
        displayModeBar: false
      };

      Plotly.newPlot('custoLocalUso', data, layout, config)
    }
  },

  mounted () {
    this.getLocalStorageToken();
    this.validateLogin();
    this.fetchCustoLocalUsoInfoDB(() => {
      this.genCustoLocalUsoGraph();
    });
  }
}


</script>

<template>

<main class="graficos">

  <div id="custoLocalUso" class="graph"></div>

  <div id="gastoAcumulado" class="graph"></div>

</main>

</template>

<style setup>

.graficos {
  display: flex;
  justify-content: space-evenly;
}

.graph {
  border: 1px solid rgba(153, 153, 153, 1);
  width: 602px;
  /* border-radius: 20px; */
  margin-top: 15px;
  margin-left: 15px;
}

</style>