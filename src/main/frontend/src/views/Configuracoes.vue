<script>

  // const TESTER = document.getElementById('tester');

  // Plotly.newPlot( 'tester', [{
  // x: [1, 2, 3, 4, 5],
  // y: [1, 2, 4, 8, 16] }], {
  // margin: { t: 0 } } );

  import Plotly from 'plotly.js-dist';
  // import { Plotly } from 'vue-plotly';

  export default {
    data () {
      return {
      }
    },
    
    methods: {

    },

    mounted () {
      Plotly.newPlot("tester", [ {
        x: [1, 2, 3, 4],
        y: [10, 15, 13, 17],
        type: 'scatter'
      }, {
        x: [1, 2, 3, 4],
        y: [16, 5, 11, 9],
        type: 'scatter'
      }]);
    }
  }

</script>

<template>
  <h2>Configurações </h2>

  <div id="tester" style="width:600px;height:250px;"></div>

</template>

<style scope>
</style>