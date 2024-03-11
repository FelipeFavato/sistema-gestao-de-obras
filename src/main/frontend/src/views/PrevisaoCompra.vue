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
      obrasInfo: [],
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
      selectedObraID: '',
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
    // Validações de login: ------------------------------------------------------\
    redirectToLogin () {
      this.useRouter.push('/login');
    },
    setHttpStatusCode (succesError) {
      this.httpStatus = succesError;
    },
    validateHttpStatus (status) {
      this.setHttpStatusCode(status);
      this.httpStatus === 403 ? this.redirectToLogin(): 'Usuário autenticado.';
    },
    validateLogin () {
      !this.localStorageToken ? this.redirectToLogin() : 'Usuário autenticado.';
    },
    ///////////////////////////////////////////////////////////////////////////////

    // Métodos de busca - GET: ---------------------------------------------------\
    fetchInfoDB (callback) {
      axios.get('/api/previsao-compra',
      {
        headers: {
          Authorization: this.localStorageToken
        }
      }).then(res => {
        this.info = res.data.sort((s1, s2) => s1['codigo'] - s2['codigo']);
        this.setHttpStatusCode(res.status);
        if (callback) callback();
      }).catch(error => {
        this.validateHttpStatus(error.response.status);
      });
    },
    fetchObrasInfoDB (callback) {
      axios.get("/api/obra",
      {
        headers: {
          Authorization: this.localStorageToken
        }
      }).then(res => {
        this.obrasInfo = res.data.sort((s1, s2) => s2.codigo - s1.codigo)
        this.setHttpStatusCode(res.status);
        if (callback) callback();
      }).catch(error => {
        this.validateHttpStatus(error.response.status);
      });
    },
    ///////////////////////////////////////////////////////////////////////////////
    fillUpdateModal (cod, descr, qnt, valorU, valorT, compraPR) {
      this.codigo = cod;
      this.descricaoProdutoServico = descr;
      this.quantidade = qnt;
      this.valorUnitario = valorU;
      this.valorTotalPrevisto = valorT;
      this.compraPrevistaRealizada = compraPR;
    },
    updateCompraPrevistaRealizada (cod, descr, qnt, vu, vt, compraPR) {
      this.fillUpdateModal(cod, descr, qnt, vu, vt, compraPR);
      axios.put('/api/previsao-compra',
      {
        codigo: this.codigo,
        codigoObra: { codigo: 1 },
        descricaoProdutoServico: this.descricaoProdutoServico,
        quantidade : this.quantidade,
        valorUnitario: this.valorUnitario,
        valorTotalPrevisto: this.valorTotalPrevisto,
        compraPrevistaRealizada: this.compraPrevistaRealizada 
      },
      {
        headers: {
          Authorization: this.localStorageToken
        }
      }).then(res => {
        console.log(res);
      }).catch(error => {
        console.log(error)
      });
    }
  },

  mounted () {
    this.validateLogin();
    this.fetchInfoDB(() => {
      console.log(this.info)
    });
    this.fetchObrasInfoDB(() => {
      console.log(this.obrasInfo)
    })
  }
}


</script>

<template>
  <SkeletonTableAndHeader v-if="this.info == ''" />

  <main v-if="this.info != ''" class="middle-margin table-responsive">
    <table class="table table-hover">
      <thead>
        <tr>
          <th scope="col">Código</th>
          <th scope="col">Descrição</th>
          <th scope="col">Quantidade</th>
          <th scope="col">Valor unitário</th>
          <th scope="col">Valor total previsto</th>
          <th scope="col">Compra realizada</th>
          <th></th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="(previsaoCompra, i) in this.info" :key="i">
          <th scope="row">{{ previsaoCompra.codigo }}</th>
          <td>{{ previsaoCompra.descricaoProdutoServico }}</td>
          <td>{{ previsaoCompra.quantidade }}</td>
          <td>{{ previsaoCompra.valorUnitario }}</td>
          <td>{{ previsaoCompra.valorTotalPrevisto }}</td>
          <td>
            <input
              type="checkbox"
              :id="'compra-prevista-realizada-checkbox-input' + previsaoCompra.codigo"
              v-model="previsaoCompra.compraPrevistaRealizada"
              @change="updateCompraPrevistaRealizada(previsaoCompra.codigo, previsaoCompra.descricaoProdutoServico,
                previsaoCompra.quantidade, previsaoCompra.valorUnitario, previsaoCompra.valorTotalPrevisto, previsaoCompra.compraPrevistaRealizada)"
            />
            <label
              id=""
              :for="'compra-prevista-realizada-checkbox-input' + previsaoCompra.codigo"
            ></label>
          </td>
          <td class="editar-excluir">
            <button
              type="button"
              class="btn btn-light btn-sm small"
              title="Editar"
              data-bs-toggle="modal"
              data-bs-target="#updateModal"
            ><img src="../assets/imagens/editar.png" alt="editar" ></button>
            
            <button
              type="button"
              class="btn btn-light btn-sm small"
              title="Editar"
            ><img src="../assets/imagens/lata-de-lixo.png" alt="lata de lixo" ></button>
          </td>
        </tr>
      </tbody>
    </table>
  </main>
</template>

<style>
</style>