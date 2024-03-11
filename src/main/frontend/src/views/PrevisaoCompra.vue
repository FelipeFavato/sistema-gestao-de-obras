<script>
import axios from 'axios';
import { useRouter } from 'vue-router';
import { getLocalStorageToken } from '../utils/userLoginValidations';
import SkeletonTableAndHeader from '../components/skeletonLoading/SkeletonTableAndHeader.vue';

// {
//   "codigoObra": {
//     "codigo": 1
//   },
//   "descricaoProdutoServico": "Porta X",
//   "quantidade": 5,
//   "valorUnitario": 10,
//   "valorTotalPrevisto": 50,
//   "compraPrevistaRealizada": false
// }

export default {
  data () {
    return {
      // Variáveis de autenticação/autorização: --\
      useRouter: useRouter(),
      localStorageToken: getLocalStorageToken(),
      httpStatus: '',
      /////////////////////////////////////////////
      // Variáveis de requisição/auxiliares: -----\
      info: [],
      codigo: '',
      codigoObra: '',
      descricaoProdutoServico: '',
      quantidade: '',
      valorUnitario: '',
      valorTotalPrevisto: '',
      compraPrevistaRealizada: false,
      /////////////////////////////////////////////
      // Variáveis de comportamento: -------------\
      /////////////////////////////////////////////
    }
  },

  props: {
    alturaMenu: Number,
  },

  components: {
    SkeletonTableAndHeader,
  },

  methods: {

  },
}


</script>

<template>
<p>Previsao</p>
</template>

<style>
</style>