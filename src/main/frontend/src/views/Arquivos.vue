<script>
import { useRouter } from 'vue-router';
import axios from 'axios';

export default {
  data () {
    return {
      // Validações de login ----------\
      useRouter: useRouter(),
      localStorageToken: null,
      httpStatus: '',
      //////////////////////////////////

      // Variáveis de comportamento: --\
      info: [], // Informação crua
      arquivos: [], // Informação tratada pronta para renderizar imagens
      arquivoSelecionado: false,
      //////////////////////////////////

      // Variáveis de requisição: -----\
      conteudoArquivo: '',
      descricao: 'teste',
      nomeArquivo: 'teste',
      idObra: 1,

      selectedFile: null,
      imageDataUrl: ''
      //////////////////////////////////

    }
  },

  props: {
    alturaMenu: Number,
  },
    
  methods: {
    // Validações de Login:--------------------------------------------------------------\
    // Redireciona para a página de login.
    redirectToLogin () {
      this.useRouter.push('/login');
    },
    // Recupera o token do localStorage e preencher 'this.localStorageToken'.
    getLocalStorageToken () {
      this.localStorageToken = localStorage.getItem('token');
    },
    // Seta o 'this.httpStatus' com os casos de sucesso e erro.
    setHttpStatusCode (succesError) {
      this.httpStatus = succesError;
    },
    // Valida o login baseado no token.
    validateLogin () {
      !this.localStorageToken ? this.redirectToLogin() : null;
    },
    // Valida o StatusHttp da requisição. Casos de token expirado.
    validateHttpStatus (status) {
      this.setHttpStatusCode(status);
      this.httpStatus === 403 ? this.redirectToLogin(): null;
    },
    //////////////////////////////////////////////////////////////////////////////////////

    // Métodos de busca de dados - GET: -------------------------------------------------\
    fetchInfoDB (callback) {
      axios.get('/api/obra-arquivos',
      {
        headers: {
          Authorization: `Bearer ${this.localStorageToken}`
        },
      })
      .then(response => {
        this.info = response.data;
        this.arquivos = this.generateArquivos(response.data);
        if (callback) callback();
      }).catch(error => {
        console.error('Error fetching image data:', error);
      });
    },
    //////////////////////////////////////////////////////////////////////////////////////

    // Métodos de INSERT de dados - POST: -----------------------------------------------\
    createArquivoInfoDB (formD) {
      axios.post('/api/obra-arquivos', formD,
      {
        headers: {
          Authorization: `Bearer ${this.localStorageToken}`,
          'Content-Type': 'multipart/form-data'
        },
      }).then(res => {
        console.log(res);
      }).catch(error => {
        console.log(error);
      });
    },

    createArquivo () {
      const formData = new FormData();
      formData.append("file", this.conteudoArquivo);
      formData.append("descricao", this.descricao);
      formData.append("nomeArquivo", this.nomeArquivo);
      formData.append("idObra", this.idObra);
      this.createArquivoInfoDB(formData)
    },
    //////////////////////////////////////////////////////////////////////////////////////

    // Métodos de comportamento: --------------------------------------------------------\
    // Gera a lista de arquivos assim que a página carrega.
    generateArquivos (data) {
      const fotografias = [];
      for (let arquivo of data) {
        arquivo.conteudoArquivo = 'data:image/jpeg;base64,' + arquivo.conteudoArquivo;
        fotografias.push(arquivo);
      }

      return fotografias;
    },
    // Quando o usuário selecionar um arquivo, deixa de mostrar o input e mostra a imagem.
    switchInputArchive () {
      this.arquivoSelecionado = !this.arquivoSelecionado;
    },
    handleFileChange(event) {
      this.switchInputArchive(); 
      const file = event.target.files[0]; // Pega o primeiro arquivo selecionado
      this.conteudoArquivo = file;
      if (file) {
        // Cria um objeto URL temporário para a imagem selecionada
        const reader = new FileReader();
        reader.readAsDataURL(file);
        reader.onload = () => {
          // Define a variável selectedFile para o arquivo selecionado e a imageDataUrl para a URL da imagem
          this.selectedFile = file;
          this.imageDataUrl = reader.result;
        };
      }
    }
    //////////////////////////////////////////////////////////////////////////////////////
  },

  mounted () {
    this.getLocalStorageToken();
    this.fetchInfoDB();
  }
}

</script>

<template>
  <main class="margin-left-top-10">
    <div class="row row-cols-1 row-cols-md-3 g-4">
      <div class="col"  style="height: 350px; width: 245px;">
        <!-- <div class="card h-100"> -->
          <div class="card" style="height: 100%; width: 100%;">

            <input v-if="!this.arquivoSelecionado" id="file-input" type="file" @change="handleFileChange"/>
            <label v-if="!this.arquivoSelecionado" for="file-input" class="label-subir-foto" style="height: 100%; width: 100%;">
              +
            </label>

            <img  v-if="imageDataUrl" class="card-img" :src="imageDataUrl" alt="Imagem selecionada" style="height: 100%; width: 100%;">

            <div class="card-img-overlay" v-if="this.arquivoSelecionado" style="height: 100%; width: 100%;">
              <form style="height: 100%; width: 100%;">
                <div class="card-body margin-top-160">
                  <div class="mb-3">
                    <input
                      type="text"
                      class="form-control"
                      id="name-input"
                      placeholder="Nome"
                      v-model="nomeArquivo"
                    >
                  </div>
                  <div class="mb-3">
                    <input
                      type="text"
                      class="form-control"
                      id="descricao-input"
                      placeholder="Descrição"
                      v-model="descricao"
                    >
                  </div>
                  <div class="space-around">
                    <button type="button" class="btn btn-secondary" @click="cancel">Limpar</button>
                    <button type="button" class="btn btn-success" @click="createArquivo">Subir</button>
                  </div>
                </div>
              </form>
            </div>
          </div>

        <!-- </div> -->
      </div>

      <div v-for="(foto, i) in arquivos" :key="i" class="col"  style="height: 350px; width: 245px;">
        <div class="card h-100">
          <img :src="foto.conteudoArquivo" class="card-img-top" :alt="i"  style="height: 80%; width: 100%;">
          <div class="card-body">
            <h5 class="card-title texto-centralizado">{{ foto.nomeArquivo }}</h5>
          </div>
        </div>
      </div>
    </div>
  </main>


  <!-- Arquivos -->
  <!-- <div>
      <img v-if="imageData" :src="this.imageData"  style="height: 200px; width: 200px;" alt="Image" />
      <div v-else>Loading...</div>
  </div>
  <img :src="imageDataUrl" v-if="imageDataUrl" alt="Imagem selecionada"> -->

</template>

<style scope>

.margin-left-top-10 {
  margin-left: 10px;
  margin-top: 10px;
}

.grey-background {
  background-color: black;
}

.margin-top-160 {
  margin-top: 160px;
}

.border {
  border: 3px solid black;
}

.texto-centralizado {
  display: flex;
  justify-content: center;
}

.space-around {
  display: flex;
  justify-content: space-around;
}

.flex-end {
  display: flex;
}

input[type="file"] {
  display: none;
}

.label-subir-foto {
  display: inline-block;
  text-transform: uppercase;
  color: #fff;
  background-color: #3D8B37;
  text-align: center;
  padding: 15px 40px;
  font-size: 180px;
  letter-spacing: 1.5px;
  user-select: none;
  cursor: pointer;
  box-shadow: 5px 15px 25px rgba(0, 0, 0, 0.35);
  border-radius: 3px;
}

.label-subir-foto:active {
  transform: scale(0.9);
}

</style>