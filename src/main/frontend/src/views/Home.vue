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

  props: {
    alturaMenu: Number,
  },

  methods: {
    // Validações de login: ------------------------------------------------------\
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
    ///////////////////////////////////////////////////////////////////////////////
  },

  mounted () {
  }
}


</script>

<template>

  <!-- Atualizações -->
  <main class="flex">

    <div class="card margin-10" style="width: 18rem;">
      <div class="card-body column">
        <div>
          <h5 class="card-title">Atualização 06.02.24</h5>
          <p class="card-text">Página de Arquivos</p>
          <p>1. Tela de ARQUIVOS quase 100% finalizada.</p>
          <p>2. Possível ADICIONAR, EDITAR, EXCLUIR FOTOS E PDF.</p>
          <p>3. Possível filtrar entre IMAGENS e PDFs.</p>
        </div>
        <a href="#arquivo" class="btn btn-success">Ver mudanças (Arquivos) -></a>
      </div>
    </div>
  
    <div class="card margin-10" style="width: 18rem;">
      <div class="card-body column">
        <div>
          <h5 class="card-title">Atualização 02.02.24</h5>
          <p class="card-text">Página de Arquivos</p>
          <p>1. Tamanho das fotos ajustadas.</p>
          <p>2. Bug de preenchimento corridigo</p>
          <p>3. Ajustado para celular</p>
        </div>
        <a href="#arquivo" class="btn btn-success">Ver mudanças (Arquivos) -></a>
      </div>
    </div>

    <div class="card margin-10" style="width: 18rem;">
      <div class="card-body column">
        <div>
          <h5 class="card-title">Atualização 01.02.24</h5>
          <p class="card-text">Página de Arquivos</p>
          <p>1. Póssivel clicar na foto e expandir a imagem, vendo DESCRIÇÃO e NOME.</p>
          <p>2. Bug de renderização corrigido</p>
        </div>
        <a href="#arquivo" class="btn btn-success">Ver mudanças (Arquivos) -></a>
      </div>
    </div>
  
    <div class="card margin-10" style="width: 18rem;">
      <div class="card-body column">
        <div>
          <h5 class="card-title">Atualização 31.01.24</h5>
          <p class="card-text">Página de Arquivos</p>
          <p>1. Tela de ARQUIVOS desenvolvida.</p>
          <p>2. Possível ADICIONAR, EDITAR, EXCLUIR apenas FOTOS por enquanto.</p>
          <p>3. Próxima feature: Fazer com que as fotos sejam clicáveis. Ao clicar na foto,
            expandir um ALBUM sendo possível ver NOME, DESCRIÇÃO e navegar entre as fotos.
            No mesmo molde de uma MODAL. 
          </p>
        </div>
        <a href="#arquivo" class="btn btn-success">Ver mudanças (Arquivos) -></a>
      </div>
    </div>



    <!-- <div class="card margin-10" style="width: 18rem;">
      <div class="card-body column">
        <div>
          <h5 class="card-title">Atualização 17.01.24</h5>
          <p class="card-text">Página de Custos</p>
          <p>1. Combos corrigidos para enviar apenas o código nas requisições.</p>
          <p></p>
          <p></p>
          <p></p>
        </div>
        <a disabled href="#" class="btn btn-secondary">Sem mudanças visíveis</a>
      </div>
    </div> -->
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

.flex {
  display: flex;
}

.column {
  display: flex;
  justify-content: space-between;
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