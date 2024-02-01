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
      obrasInfo: [],
      selectedArquivosByObra: [],
      arquivoSelecionado: false,
      selectedObraNome: '',
      selectedFile: null,
      imageDataUrl: '',
      //////////////////////////////////

      // Variáveis de requisição: -----\
      codigo: '',
      conteudoArquivo: '',
      descricao: '',
      nomeArquivo: '',
      idObra: '',
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
      }).then(response => {
        this.info = response.data;
        this.arquivos = this.generateArquivos(response.data);
        if (callback) callback();
      }).catch(error => {
        console.error('Error fetching image data:', error);
      });
    },
    fetchObrasInfoDB (callback) {
      axios.get("/api/obra",
      {
        headers: {
          Authorization: `Bearer ${this.localStorageToken}`
        }
      }).then(res => {
        this.obrasInfo = res.data.sort((s1, s2) => s1.codigo - s2.codigo)
        // this.setHttpStatusCode(res.status);
        if (callback) callback();
      }).catch(error => {
        this.validateHttpStatus(error.response.status);
      });
    },
    //////////////////////////////////////////////////////////////////////////////////////

    // Métodos de INSERT de dados - POST: -----------------------------------------------\
    createArquivoInfoDB (formD, callback) {
      axios.post('/api/obra-arquivos', formD,
      {
        headers: {
          Authorization: `Bearer ${this.localStorageToken}`,
          'Content-Type': 'multipart/form-data'
        },
      }).then(res => {
        if (callback) callback();
      }).catch(error => {
        if (error.response.status === 500) {
          alert("Foto já cadastrada no sistema.");
          this.limparButtonActions();
        }
      });
    },

    createArquivo () {
      const self = this;
      const formData = new FormData();
      formData.append("file", this.conteudoArquivo);
      formData.append("descricao", this.descricao);
      formData.append("nomeArquivo", this.nomeArquivo);
      formData.append("idObra", this.idObra);
      // Cria o arquivo novo e renderiza a lista.
      this.createArquivoInfoDB(formData, () => {
        self.fetchInfoDB(() => {
          self.selectArquivosByObra(Number(self.idObra));
        });
        this.limparButtonActions();
      });
    },
    //////////////////////////////////////////////////////////////////////////////////////

    // Métodos de UPDATE de dados - PUT: ------------------------------------------------\
    fillUpdateArquivoModal(cod, nome, descricao) {
      this.codigo = cod;
      this.nomeArquivo = nome;
      this.descricao = descricao;
    },
    updateArquivoInfoDB(callback) {
      axios.put('/api/obra-arquivos',
      {
        codigo: Number(this.codigo),
        nomeArquivo: this.nomeArquivo,
        descricao: this.descricao
      },
      {
        headers: {
          Authorization: `Bearer ${this.localStorageToken}`
        }
      }).then(res => {
        if (callback) callback();
      }).catch(error => {
        console.log(error);
      })
    },
    updateArquivo() {
      const self = this;
      this.updateArquivoInfoDB(() => {
        self.fetchInfoDB(() => {
          self.selectArquivosByObra(Number(self.idObra))
        });
        self.cancel();
      });
    },
    //////////////////////////////////////////////////////////////////////////////////////

    // Métodos de EXCLUSÃO de dados - DELETE: -------------------------------------------\
    fillDeleteArquivoModal (cod) {
      this.codigo = cod;
    },
    removeArquivoFromDB (codigo, callback) {
      axios.delete('/api/obra-arquivos',
      {
        headers: {
          Authorization: this.localStorageToken
        },
        data: {
          codigo: Number(codigo)
        }
      }).then(res => {
        if (callback) callback();
      }).catch(error => {
        console.log(error);
      });
    },
    removeArquivo (codigo) {
      const self = this;
      this.removeArquivoFromDB(codigo, () => {
        self.fetchInfoDB(() => {
          self.selectArquivosByObra(Number(self.idObra))
        });
        self.cancel();
      });
    },
    //////////////////////////////////////////////////////////////////////////////////////

    // Métodos de comportamento: --------------------------------------------------------\
    // Limpa o combo de requisição.
    cancel() {
      this.codigo = '';
      this.descricao = '';
      this.conteudoArquivo = '';
      this.nomeArquivo = '';
    },
    limparButtonActions () {
      // Limpa o combo.
      this.cancel();
      // Retira a imagem que está sendo gerada.
      this.imageDataUrl = '';
      // Volta a LABEL de novo Upload.
      this.switchInputArchive();
    },
    // Gera a lista de arquivos assim que a página carrega.
    generateArquivos (data) {
      const fotografias = [];
      for (let arquivo of data) {
        arquivo.conteudoArquivo = 'data:image/jpeg;base64,' + arquivo.conteudoArquivo;
        fotografias.push(arquivo);
      }

      return fotografias.sort((s1, s2) => s1['nomeArquivo'].localeCompare(s2['nomeArquivo']));
    },
    // Quando o usuário selecionar um arquivo, deixa de mostrar o input e mostra a imagem.
    switchInputArchive () {
      this.arquivoSelecionado = !this.arquivoSelecionado;
    },
    // Preciso estudar essa função.
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
    },
    selectObra (obraNome, obraCod) {
      this.selectedObraNome = obraNome;
      this.idObra = obraCod;
    },
    emptyArquivosByObra() {
      this.selectedArquivosByObra = [];
    },
    selectArquivosByObra(obraCod) {
      const arquivosDestaObra = [];
      for (let arquivo of this.arquivos) {
        if (arquivo.idObra.codigo === obraCod) {
          arquivosDestaObra.push(arquivo)
        }
      }

      this.selectedArquivosByObra = arquivosDestaObra;
    },
    obrasDropDownActions(obraNome, obraCod) {
      this.selectObra(obraNome, obraCod);
      this.emptyArquivosByObra();
      this.selectArquivosByObra(Number(obraCod));
    },
    expandirFoto (conteudoArquivo) {
      alert('foto expandida!')
    },
    //////////////////////////////////////////////////////////////////////////////////////

    // Métodos de refinamento: ----------------------------------------------------------\
    // HGPK = Handle Global Press Key: Lida com os cliques de ENTER na página.
    HGPKEnter () {
      window.addEventListener('keydown', (event) => {
        // Confere se o botão apertado foi o 'ENTER' 
        const e = event;
        const ENTER = e.keyCode === 13;

        // Recupera botões e elementos da página.
        let body = document.getElementsByTagName('body');
        let novaFotoButton = document.getElementById('novaFotoButton');

        let salvaNovaFoto = document.getElementById('salvaNovaFoto');
        let atualizaFoto = document.getElementById('atualizaFoto');
        let deletaFoto = document.getElementById('deletaFoto');

        // Modais e comparações se elas estão ativas ou não.
        let deleteArquivoModal = document.getElementById('deleteArquivoModal');
        let updateArquivoModal = document.getElementById('updateModalArquivo');

        const noModalOpen = body[0].classList.value === '';

        const isDeleteArquivoModal = deleteArquivoModal.classList.value === 'modal fade show';
        const isUpdateArquivoModal = updateArquivoModal.classList.value === 'modal fade show';

        // Ativa o comportamento desejado baseado no momento que o usuário está na página:
        if (noModalOpen && !this.arquivoSelecionado && ENTER) {
          e.preventDefault();
          novaFotoButton.click();
        } else if (noModalOpen && this.arquivoSelecionado && ENTER) {
          e.preventDefault();
          salvaNovaFoto.click();
        } else if (!noModalOpen && isUpdateArquivoModal && ENTER) {
          e.preventDefault();
          atualizaFoto.click();
        } else if (!noModalOpen && isDeleteArquivoModal && ENTER) {
          e.preventDefault();
          deletaFoto.click();
        }
      });
    },
    //////////////////////////////////////////////////////////////////////////////////////

  },

  mounted () {
    this.getLocalStorageToken();
    this.fetchInfoDB(() => {
      this.obrasDropDownActions(
        localStorage.getItem('nomeObra') ? localStorage.getItem('nomeObra') : '',
        localStorage.getItem('codigoObra') ? localStorage.getItem('codigoObra') : 1
      );
    });
    this.fetchObrasInfoDB()
    this.HGPKEnter();
  }
}

</script>

<template>

  <!-- Header com o DropDown 'Obras' -->
  <header class="header middle-margin">
    <!-- DropDown 'Obras' -->
    <div class="dropdown">
      <button class="btn btn-secondary dropdown-toggle" type="button" data-bs-toggle="dropdown" aria-expanded="false">
        {{ selectedObraNome ? selectedObraNome : 'Obras' }}
      </button>
      <ul class="dropdown-menu">
        <li v-for="(obra, i) in obrasInfo" :key="i">
          <button
            class="dropdown-item"
            type="button"
            @click="obrasDropDownActions(obra.nome, obra.codigo)"
            >{{ obra.nome }}</button>
        </li>
        <li><hr class="dropdown-divider"></li>
        <li>
          <button
            class="dropdown-item"
            type="button"
            @click="obrasDropDownActions('', '')"
          >Limpar</button>
        </li>
      </ul>
    </div>
  </header>

  <!-- DeleteModalArquivo -->
  <div class="modal fade" id="deleteArquivoModal" data-bs-backdrop="static" data-bs-keyboard="false"
    tabindex="-1" aria-labelledby="deleteArquivoModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-dialog-scrollable modal-sm">
      <div class="modal-content">
        <div class="modal-header">
          <h1 class="modal-title fs-5" id="deleteArquivoModalLabel">Realmente deseja excluir?</h1>
          <button @click="cancel" type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
        </div>

        <div class="modal-footer header">
          <button
            type="button"
            class="btn btn-secondary dark-grey"
            data-bs-dismiss="modal"
            @click="cancel"
          >Não</button>

          <button
            id="deletaFoto"
            type="button"
            class="btn btn-success light-green"
            data-bs-dismiss="modal"
            @click="removeArquivo(this.codigo)"
          >Sim</button>
        </div>
      </div>
    </div>
  </div>

  <!-- UpdateModalArquivo -->
  <div class="modal fade" id="updateModalArquivo" data-bs-backdrop="static" data-bs-keyboard="false"
    tabindex="-1" aria-labelledby="updateModalArquivoLabel" aria-hidden="true">
    <div class="modal-dialog modal-dialog-scrollable">
      <div class="modal-content">
        <div class="modal-header">
          <h1 class="modal-title fs-5" id="updateModalArquivoLabel">Editar Arquivo</h1>
          <button @click="cancel" type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
        </div>

        <div class="modal-body">
          <form action="PUT">

            <!-- Código -->
            <div class="mb-3">
              <label for="id-input" class="form-label bold">Código:</label>
              <input
                type="text"
                class="form-control"
                id="id-input"
                disabled
                v-model="codigo">
            </div>

            <!-- NomeArquivo -->
            <div class="mb-3">
              <label for="nome-arquivo-input" class="form-label bold">Nome:</label>
              <input
                type="text"
                class="form-control"
                id="nome-arquivo-input"
                v-model="nomeArquivo">
            </div>

            <!-- Descrição -->
            <div class="mb-3">
              <label for="descricao-input" class="form-label bold">Descrição:</label>
              <input
                type="text"
                class="form-control"
                id="descricao-input"
                v-model="descricao">
            </div>

          </form>
        </div>

        <div class="modal-footer">
          <button type="button" class="btn btn-secondary dark-grey" data-bs-dismiss="modal"
            @click="cancel"
          >Fechar</button>

          <button id="atualizaFoto" type="button" class="btn btn-success  light-green" data-bs-dismiss="modal"
            @click="updateArquivo()"
          >Salvar</button>
        </div>
      </div>
    </div>
  </div>

  <!-- CARDS -->
  <main class="margin-left-top-10">
    <div class="row row-cols-1 row-cols-md-3 g-4">

      <!-- Card de INSERT -->
      <div  v-if="this.selectedObraNome" class="col"  style="height: 350px; width: 245px;">
          <div class="card" style="height: 100%; width: 100%;">

            <input v-if="!this.arquivoSelecionado" id="file-input" type="file" @change="handleFileChange"/>
            <label v-if="!this.arquivoSelecionado" for="file-input" id="novaFotoButton" class="label-subir-foto" style="height: 100%; width: 100%;">
              +
            </label>

            <img  v-if="imageDataUrl" class="card-img" :src="imageDataUrl" alt="Imagem selecionada" style="height: 100%; width: 100%;">

            <div class="card-img-overlay" v-if="this.arquivoSelecionado" style="height: 100%; width: 100%;">
              <form style="height: 100%; width: 100%;">
                <div class="card-body margin-top-160">
                  <!-- Nome -->
                  <div class="mb-3">
                    <input
                      type="text"
                      class="form-control"
                      id="name-input"
                      placeholder="Nome"
                      v-model="nomeArquivo"
                    >
                  </div>
                  <!-- Descrição -->
                  <div class="mb-3">
                    <input
                      type="text"
                      class="form-control"
                      id="descricao-input"
                      placeholder="Descrição"
                      v-model="descricao"
                    >
                  </div>
                  <!-- Botões de Limpar e Subir arquivo -->
                  <div class="space-between">
                    <button type="button" class="btn btn-secondary" @click="limparButtonActions">Limpar</button>
                    <button type="button" class="btn btn-success" id="salvaNovaFoto" @click="createArquivo">Subir</button>
                  </div>
                </div>
              </form>
            </div>
          </div>
      </div>

      <!-- Lista de cards de Fotos. -->
      <div v-for="(foto, i) in selectedArquivosByObra" :key="i" class="col"  style="height: 350px; width: 245px;">
        <div class="card h-100" style="height: 100%; width: 100%;">
          <img :src="foto.conteudoArquivo" class="card-img img-thumbnail img-fluid" :alt="foto.nomeArquivo, i" style="height: 100%; width: 100%;">
          <div class="card-img-overlay space-between-column hover-white-background">

            <!-- Botões Editar/Excluir -->
            <div class="space-between">
              <!-- Editar -->
              <button
                type="button"
                class="btn btn-light btn-sm small grey-background border-radius-5"
                title="Editar"
                data-bs-toggle="modal"
                data-bs-target="#updateModalArquivo"
                @click="fillUpdateArquivoModal(foto.codigo, foto.nomeArquivo, foto.descricao)"
              ><img src="../assets/imagens/editar.png" alt="lata de lixo"></button>
              <!-- Excluir -->
              <button
                type="button"
                class="btn btn-light btn-sm small grey-background border-radius-5"
                title="Excluir"
                data-bs-toggle="modal"
                data-bs-target="#deleteArquivoModal"
                @click="fillDeleteArquivoModal(foto.codigo)"
              ><img src="../assets/imagens/lata-de-lixo.png" alt="lata de lixo"></button>
            </div>

            <!-- Botão Expandir -->
            <div class="texto-centralizado" style="height: 245px; width: 185px;">
              <button @click="expandirFoto(foto.conteudoArquivo)" class="expandir-button no-background-color" style="height: 245px; width: 185px;">
                <img src="../assets/imagens/expandir.png" class="expandir-button rounded mx-auto d-block">
              </button>
            </div>

            <!-- Nome -->
            <!-- <h5 class="card-title">Card title</h5> -->
            <p class="card-text texto-centralizado grey-background border-radius-5">{{ foto.nomeArquivo }}</p>
          </div>
        </div>
      </div>

    </div>
  </main>

</template>

<style scope>

.margin-left-top-10 {
  margin-left: 10px;
  margin-top: 10px;
}

.border-radius-5 {
  border-radius: 10px;
}

.font {
  font-family: Arial, Helvetica, sans-serif;
}

.hover-white-background:hover {
  background-color: rgba(255, 255, 255, 0.5);
}

.expandir-button {
  opacity: 0; /* Torna o botão invisível por padrão */
  transition: opacity 0.3s ease; /* Adiciona uma transição suave */
  height: 70px;
  width: 70px;
  border: none;
}

.expandir-button:hover {
  opacity: 0.8; /* Torna o botão visível quando o mouse passa por cima */
}

.no-background-color {
  background-color: rgba(255, 255, 255, 0);
}

.grey-background {
  /* background-color: rgba(136, 136, 136, 0.5); */
  background-color: rgba(255, 255, 255, 0.8);
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

.space-between {
  display: flex;
  justify-content: space-between;
}

.space-between-column {
  display: flex;
  justify-content: space-between;
  flex-direction: column;
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
  transform: scale(0.95);
}

.label-subir-foto:hover {
  background-color: #2C6D28;
}

</style>