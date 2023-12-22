<script>
// /api/jasper/produto/{categoria}/{marca}
import axios from 'axios';
import { useRouter } from 'vue-router';


export default {
  data () {
    return {
      // Variáveis auxiliares
      marcasInfo: [],
      // Variáveis de requisição
      localStorageToken: null,
      useRouter: useRouter(),
      categoria: 'todos',
      marca: 'todos',
      retorno: '',
      // Variaveis de comportamento
      area: '',
      mensagemConfirmacao: false,
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
    // Método para setar o 'this.httpStatus' com os casos de sucesso e erro.
    setHttpStatusCode (succesError) {
      this.httpStatus = succesError;
    },
    // Método para validar o login baseado no token.
    validateLogin () {
      !this.localStorageToken ? this.redirectToLogin() : null;
    },
    // Método para validar o StatusHttp da requisição. Casos de token expirado.
    validateHttpStatus (status) {
      this.setHttpStatusCode(status);
      this.httpStatus === 403 ? this.redirectToLogin(): null;
    },
    // Comportamentos da página
    setMensagemConfirmacao (bool) {
      this.mensagemConfirmacao = bool;
    },
    setCategoria (categ) {
      this.categoria = categ;
    },
    gerarPDF (callback) {
      axios.get(`/api/jasper/${this.area}/${this.categoria}/${this.marca}`,
      {
        headers: {
          Authorization: this.localStorageToken
        }
      }).then(res => {
        this.retorno = res;
        if (callback) callback();
      }).catch(error => {
        this.retorno = error.response;
        console.log(this.retorno);
        // if (callback) callback();
      });
    },
    limparButtonActions() {
      this.area = '';
    },
    gerarButtonActions () {
      this.setMensagemConfirmacao(false);
      this.gerarPDF(() => {
        this.setMensagemConfirmacao(true);
      });
    },
    // Busca de Dados
    fetchMarcasInfo(callback) {
      axios.get('/api/marca',
      {
        headers: {
          Authorization: this.localStorageToken
        }
      }).then(res => {
        this.marcasInfo = res.data.sort((s1, s2) => s1['nome'].localeCompare(s2['nome']));
        this.setHttpStatusCode(res.status);
        if (callback) callback();
      }).catch(error => {
        this.validateHttpStatus(error.response.status);
      });
    }
  },

  mounted () {
    this.getLocalStorageToken();
    this.fetchMarcasInfo();
  }
}


</script>


<template>
  <main class="main-container">
    <aside class="barra-lateral">
      <div>
        <h4 class="title">Filtros</h4>
        
        <ul class="nav flex-column">

          <!-- Áreas -->
        <li class="nav-item">
          <div class="form-floating">
            <select
              class="form-select"
              id="floatingSelect-area"
              aria-label="Floating label select example"
              v-model="area">
              <!-- <option selected value=""></option> -->
              <option value="produto">Produtos</option>
              <hr class="dropdown-divider">
              <option value="">S/ seleção</option>
            </select>
            <label for="floatingSelect-area">Área</label>
          </div>
        </li>

        <!-- Produto - Categoria -->
        <li v-if="area === 'produto'" class="nav-item">
          <div class="form-floating">
            <select
            class="form-select"
            id="floatingSelect-categoria"
              aria-label="Floating label select example"
              v-model="categoria">
              <option selected value="todos">Todas</option>
              <option value="Material">Material</option>
              <option value="Serviço">Serviço</option>
              <option value="TaxasImpostos">Taxas/Impostos</option>
            </select>
            <label for="floatingSelect-categoria">Categoria</label>
          </div>
        </li>

        <!-- Produto - Marca -->
        <li v-if="area === 'produto'" class="nav-item">
          <div class="form-floating">
            <select
              class="form-select"
              id="floatingSelect-marca"
              aria-label="Floating label select example"
              v-model="marca">
              <option selected value="todos">Todas</option>
              <option
              v-for="(marca, i) in marcasInfo" :key="i" :value="marca.nome"
              >{{ marca.nome }}</option>
            </select>
            <label for="floatingSelect-marca">Marca</label>
          </div>
        </li>
      </ul>
    </div>

      <div class="barra-lateral-div">
        <button
          type="button"
          class="btn btn-secondary"
          :disabled="area === '' ? true : false"
          @click="limparButtonActions()"
          >
          Limpar
        </button>
        <button
          type="button"
          class="btn btn-success botao"
          @click="gerarButtonActions()"
          :disabled="area === '' ? true : false"
          >
          Gerar
        </button>
      </div>
    </aside>

    <div v-if="mensagemConfirmacao" class="relatorio">
      <h2>Relátorio gerado na sua pasta de Downloads!</h2>
      <!-- <p>{{ this.retorno.data }}</p> -->
    </div>
  </main>


</template>


<style>

.main-container {
  display: flex;
}

.relatorio {
  /* border: 1px solid #2b3035; */
  width: 90%;
}

.relatorio h2 {
  display: flex;
  justify-content: center;
  align-items: center;
  color: green;
  height: 100%;
}

.barra-lateral {
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  height: 80vh;
  /* border: 5px solid #2b3035; */
  width: 12%;
  border-right: 3px solid #2b3035;
  border-bottom: 3px solid #2b3035;
  /* background-color: #BBBBBB; */
}

.title {
  display: flex;
  justify-content: center;
}

.barra-lateral-div {
  display: flex;
  justify-content: space-around;
  padding-bottom: 5px;
}

.barra-lateral button {
  display: flex;
  width: 35%;
  justify-content: center;
}

</style>