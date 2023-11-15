<script>

import axios from 'axios';
import Plotly from 'plotly.js-dist';
import { useRouter } from 'vue-router';

export default {
  data () {
    return {
      useRouter: useRouter(),
      httpStatus: '',
      localStorageToken: null,
      comprasInfo: [],
      processedCompraData: []
    }
  },
    
  methods: {
    // Método para redirecionar para a página de login.
    redirectToLogin () {
      this.useRouter.push('/login');
    },
    // Método para recuperar o token do localStorage e preencher 'this.localStorageToken'.
    getLocalStorageToken () {
      this.localStorageToken = localStorage.getItem('token');
    },
    // Método para setar o 'this.httpStatusCode' com os casos de sucesso e erro.
    setHttpStatusCode (succesError) {
      this.httpStatus = succesError;
    },
    // Método para validar o StatusHttp da requisição. Casos de token expirado.
    validateHttpStatus (status) {
      this.setHttpStatusCode(status);
      this.httpStatus === 403 ? this.redirectToLogin(): null;
    },
    // Método para validar o login baseado no token.
    validateLogin () {
      !this.localStorageToken ? this.redirectToLogin() : null;
    },
    // Método para buscar os dados das compras
    fetchComprasInfoDB (callback) {
      axios.get('/api/compra',
      {
        headers: {
          Authorization: this.localStorageToken
        }
      }).then(res => {
        this.comprasInfo = res.data;
        if (callback) callback();
      }).catch(error => {

      });
    },
    extractMonth (date) {
      return Number(date.split('-')[1]);
    },
    processComprasData () {
      // const processedData = [];
      const meses = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12];
      let somaMes = 0;
      for (let mes of meses) {
        for (let compra of this.comprasInfo) {
          if (this.extractMonth(compra.dataVencimento) === mes) {
            somaMes += compra.valorFinal;
          }
        }
        this.processedCompraData.push(somaMes);
      }
    },
    genGraph () {
      const meses = ['Jan', 'Fev', 'Mar', 'Abr', 'Maio', 'Jun', 'Jul', 'Ago', 'Set', 'Out', 'Nov', 'Dez'];
      // const gastoCompras = this.processComprasData();

      const compras = {
        x: meses,
        // y: gasto acumulado por mês, de acordo com a data de vencimento?
        y: this.processedCompraData,
        type: 'bar',
        marker: {
          color: '#292525'
        },
        name: 'Compras'
      };

      const maoDeObra = {
        x: meses,
        y: [0],
        type: 'bar',
        marker: {
          color: 'darkorange'
        },
        name: 'Mão de Obra'
      };

      const data = [maoDeObra, compras];

      const layout = {
        title: 'Gastos acumulados',
        font: { size: 15 },
        barmode: 'stack',
        showLegend: true,
        // legend: {
        //   x: 1,
        //   xanchor: 'left',
        //   y: 1
        // },
        // xaxis: { type: 'date' },
      };

      const config = {
        responsive: true,
        displayModeBar: false,
        // staticPlot: true
      };

      Plotly.newPlot("tester", data, layout, config);
    }
  },

  mounted () {
    this.getLocalStorageToken();
    this.validateLogin();
    this.fetchComprasInfoDB(() => {
      this.processComprasData();
      this.genGraph();
    });
  }
}

</script>

<template>
  <h2>Configurações </h2>

  <!-- <div>
    <p v-for="(compra, i) in comprasInfo" :key="i">
      {{ compra.dataVencimento }} - {{ compra.dataPagamento }} - {{ compra.valorFinal }}
    </p>
  </div> -->

  <div id="tester" style="width:800px;height:400px;"></div>

</template>

<style scope>
</style>