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
      formato: 'pdf',
      mensagemConfirmacao: false,
    }
  },

  props: {
    alturaMenu: Number,
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
      axios.get(`/api/relatorio/${this.area}/pdf/${this.categoria}/${this.marca}`,
      {
        headers: {
          Authorization: this.localStorageToken,
          'Content-Type': 'application/pdf',
        },
        responseType: 'blob'
      }).then(res => {
        this.exibirPDF(res.data);
        if (callback) callback();
      }).catch(error => {
        this.retorno = error.response;
      });
    },
    limparButtonActions() {
      this.area = '';
      this.formato = 'pdf';
    },
    gerarButtonActions () {
      this.setMensagemConfirmacao(false);
      this.gerarPDF(() => {
        this.setMensagemConfirmacao(true);
      });
    },
    exibirPDF(pdfBytes) {
      // Crie um Blob a partir dos bytes do PDF
      const blob = new Blob([pdfBytes], { type: 'application/pdf' });

      // Crie uma URL temporária para o Blob
      const url = URL.createObjectURL(blob);

      // Recupera um iframe
      const iframe = document.getElementById('iframe');

      // Defina a fonte do iframe como a URL temporária do Blob
      iframe.src = url;
    },
    baixarCSV() {
      axios.get(`/api/relatorio/${this.area}/csv/${this.categoria}/${this.marca}`,
      {
        headers: {
          Authorization: this.localStorageToken,
          'Content-Type': 'application/json'
        },
        // responseType: 'blob'
      })
      .then(response => {
        // Cria um Blob a partir dos dados da resposta
        var blob = new Blob([response.data]);

        // Cria um URL de objeto para o Blob
        var url = window.URL.createObjectURL(blob);
        
        // Cria um link temporário e configura o URL
        var link = document.createElement('a');
        link.href = url;

        // Define o atributo de download para o nome do arquivo desejado
        link.download = `${this.area}.csv`;
        
        // Simula um clique no link para iniciar o download
        link.click();

        // Libera recursos do URL de objeto
        window.URL.revokeObjectURL(url);
      })
      .catch(error => console.error('Erro na solicitação:', error));
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
  <main class="main-container" :style="{ height: `calc(99vh - ${alturaMenu}px)` }">
    <aside class="barra-lateral">
      <!-- Filtros -->
      <div>        
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
                <option value="">Selecionar</option>
              </select>
              <label for="floatingSelect-area">Relatórios</label>
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

          <!-- Formato -->
          <li v-if="area != ''" class="nav-item">
            <div class="form-floating">
              <select
                class="form-select"
                id="floatingSelect-formato"
                aria-label="Floating label select example"
                v-model="formato">
                <option value="pdf">PDF</option>
                <option value="csv">Excel (CSV)</option>
              </select>
              <label for="floatingSelect-formato">Formato</label>
            </div>
          </li>

        </ul>
      </div>

      <!-- Botões -->
      <div class="barra-lateral-div">

        <!-- Botão Limpar -->
        <button
          type="button"
          class="btn btn-secondary"
          :disabled="area === '' ? true : false"
          @click="limparButtonActions()"
          >
          Limpar
        </button>
    
        <!-- Botão Gerar -->
        <button
          v-if="formato === 'pdf'"
          type="button"
          class="btn btn-success botao"
          @click="gerarButtonActions()"
          :disabled="area === '' ? true : false"
        >
          Gerar
        </button>
    
        <!-- Botão Baixar -->
        <button
          v-if="formato === 'csv'"
          @click="baixarCSV()"
          type="button"
          class="btn btn-success"
          :disabled="area === '' ? true : false"
        >
          Baixar
        </button>

      </div>
    </aside>

    <div class="relatorio">
      <iframe id="iframe" width="100%" height="100%"></iframe>
      
    </div>
  </main>


</template>


<style>

.main-container {
  display: flex;
}

.relatorio {
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
  width: 12%;
  border-right: 3px solid #2b3035;
  height: 100%;
}

.title {
  display: flex;
  justify-content: center;
}

.barra-lateral-div {
  display: flex;
  justify-content: space-around;
  padding-bottom: 10px;
}

.barra-lateral button {
  display: flex;
  width: 35%;
  justify-content: center;
}

.font-16 {
  font-size: 16px;
}

</style>