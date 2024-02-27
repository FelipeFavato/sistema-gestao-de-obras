<script>
import { useRouter } from 'vue-router';
import axios from 'axios';
import { insertSuccessToast, updateSuccessToast, deleteSuccessToast,
  deleteErrorToast, insertErrorToast, updateErrorToast } from '../utils/toasts/index';
import SkeletonCards from '../components/skeletonLoading/SkeletonCards.vue';

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
      fotoModal: '',
      modalMaxWidth: 0,
      modalMaxHeight: 0,
      isPortrait: false,
      retratoOuPaisagem: '',
      retratoOuPaisagemImagem: '',
      retratoOuPaisagemDescricao: '',
      selectedFiltro: '',
      customToastNotification: 'Arquivo',
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

  components: {
    SkeletonCards
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
          Authorization: this.localStorageToken
          // 'Content-Type': 'application/pdf',
        },
        // responseType: 'blob'
      }).then(response => {
        this.info = response.data;
        this.arquivos = this.generateArquivos(this.info);
        if (callback) callback();
      }).catch(error => {
        console.error('Error fetching image data:', error);
      });
    },
    fetchObrasInfoDB (callback) {
      axios.get('/api/obra',
      {
        headers: {
          Authorization: this.localStorageToken
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
          Authorization: this.localStorageToken,
          'Content-Type': 'multipart/form-data'
        },
      }).then(res => {
        if (callback) callback();
        insertSuccessToast(this.customToastNotification);
      }).catch(error => {
        insertErrorToast(this.customToastNotification);
        this.limparButtonActions();
      });
      
    },

    createArquivo () {
      const self = this;
      const formData = new FormData();
      formData.append("file", this.conteudoArquivo);
      formData.append("descricao", this.descricao ? this.descricao : ' ');
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
          Authorization: this.localStorageToken
        }
      }).then(res => {
        if (callback) callback();
        updateSuccessToast(this.customToastNotification);
      }).catch(error => {
        console.log(error);
        updateErrorToast(this.customToastNotification);
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
        deleteSuccessToast(this.customToastNotification);
      }).catch(error => {
        console.log(error);
        deleteErrorToast('');
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
    closeVisualizacao () {
      this.conteudoArquivo = '';
      this.nomeArquivo = '';
      this.descricao = '';
      this.fotoModal = '';
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
      // Separa oque é PDF do que é imagem.
      for (let a of data) {
        if (`${a.conteudoArquivo[0]}${a.conteudoArquivo[1]}${a.conteudoArquivo[2]}${a.conteudoArquivo[3]}${a.conteudoArquivo[4]}` === "JVBER") {
          fotografias.push(a);
        } else {
          a.conteudoArquivo = 'data:image/jpeg;base64,' + a.conteudoArquivo;
          fotografias.push(a);
        }
      }

      return fotografias.sort((s1, s2) => s1['nomeArquivo'].localeCompare(s2['nomeArquivo']));
    },
    exibirPDF(pdfBytes) {
      // String Base64 do PDF.
      let pdfBase64String = pdfBytes; // String 'JVBER...' completa

      // Decodificar a string Base64 para um array de bytes.
      let pdfData = atob(pdfBase64String);

      // Criar um array de bytes
      let dataArray = new Uint8Array(pdfData.length);
      for (let i = 0; i < pdfData.length; i++) {
        dataArray[i] = pdfData.charCodeAt(i);
      }

      // Criar um blob a partir do array de bytes
      let blob = new Blob([dataArray], { type: 'application/pdf' });

      // Criar uma URL para o blob
      let url = URL.createObjectURL(blob);

      // Usar a URL no src de um elemento iframe
      let iframe = document.getElementById('iframe');
      iframe.src = url;
      iframe.style.width = '80%';
      iframe.style.height = '100%';
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
    filtroDropDownActions(selectedFiltro) {
      this.selectedFiltro = selectedFiltro;

      if (this.selectedFiltro === 'Imagens') {
        this.selectArquivosByObra(Number(this.idObra));
        this.selectedArquivosByObra = this.selectedArquivosByObra.filter(arq => arq.conteudoArquivo[0] !== 'J');
      } else if (this.selectedFiltro === 'PDF') {
        this.selectArquivosByObra(Number(this.idObra));
        this.selectedArquivosByObra = this.selectedArquivosByObra.filter(arq => arq.conteudoArquivo[0] !== 'd');
      } else if (this.selectedFiltro === 'Todos') {
        this.selectArquivosByObra(Number(this.idObra));
      }
    },
    fillVisualizaArquivoModal (conteudoArquivo, nomeArquivo, descricao) {
      this.fotoModal = conteudoArquivo;
      this.nomeArquivo = nomeArquivo;
      this.descricao = descricao;

      if (this.fotoModal[0] === 'J') {
        this.checkRetratoPaisagemModal(false);
        setTimeout(() => {
          this.exibirPDF(conteudoArquivo);
        }, 1500);
      }
    },
    adjustModalSize() {
      const img = this.$refs.modalImage;
      this.modalMaxWidth = img.naturalWidth;
      this.modalMaxHeight = img.naturalHeight;
      
      // Verifica se a imagem está no modo retrato ou paisagem.
      // Seta para true caso seja Retrato (de pé).
      this.isPortrait = img.naturalHeight > img.naturalWidth;

      this.checkRetratoPaisagemModal(this.isPortrait);
    },
    checkRetratoPaisagemModal (retrato) {
      if (retrato) {
        this.retratoOuPaisagem = 'retrato'
        this.retratoOuPaisagemImagem = 'retrato-imagem';
        this.retratoOuPaisagemDescricao = 'retrato-descricao';
      } else {
        this.retratoOuPaisagem = 'paisagem'
        this.retratoOuPaisagemImagem = 'paisagem-imagem';
        this.retratoOuPaisagemDescricao = 'paisagem-descricao';
      }
    },
    //////////////////////////////////////////////////////////////////////////////////////

    // Métodos de refinamento: ----------------------------------------------------------\
    // HGPK = Handle Global Press Key: Lida com os cliques de ENTER na página.
    HGPKEnter (event) {
      const e = event;
      const ENTER = e.key === 'Enter';

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
    },
    addHGPKEnter () {
      window.addEventListener('keydown', this.HGPKEnter);
    },
    removeHGPKEnter () {
      window.removeEventListener('keydown', this.HGPKEnter);
    }
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
    this.addHGPKEnter();
  },

  unmounted () {
    this.removeHGPKEnter();
  }
}

</script>

<template>

  <!-- Cards Esqueleto - Carregamento -->
  <SkeletonCards v-if="this.info == ''" />

  <!-- Header com o DropDown 'Obras' -->
  <header v-if="this.info != ''" class="header middle-margin">
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

    <!-- Filtro tipo de ARQUIVO -->
    <div class="dropdown">
      <button class="btn btn-secondary dropdown-toggle" type="button" data-bs-toggle="dropdown" aria-expanded="false">
        {{ this.selectedFiltro ? this.selectedFiltro : 'Todos' }}
      </button>
      <ul class="dropdown-menu">
        <!-- Imagens -->
        <li>
          <button
            class="dropdown-item"
            type="button"
            @click="filtroDropDownActions('Imagens')"
            >Imagens</button>
        </li>
        <!-- PDF -->
        <li>
          <button
            class="dropdown-item"
            type="button"
            @click="filtroDropDownActions('PDF')"
            >PDF</button>
        </li>
        <li><hr class="dropdown-divider"></li>
        <li>
          <button
            class="dropdown-item"
            type="button"
            @click="filtroDropDownActions('Todos')"
          >Todos</button>
        </li>
      </ul>
    </div>
  </header>

  <!-- VisualizaArquivoModal -->
  <div class="modal fade" id="visualizaArquivoModal" data-bs-backdrop="static" data-bs-keyboard="false"
    tabindex="-1" aria-labelledby="visualizaArquivoModalLabel" aria-hidden="true">
    <div :class="this.retratoOuPaisagem" class="modal-dialog modal-dialog-centered" style="height: 100%; width: 100%;">
      <div class="modal-content" style="height: 100%; width: 100%;">

        <!-- FOTO -->
        <div v-if="this.fotoModal[0] === 'd'" class="modal-body" style="height: 100%; width: 100%;">
          <div class="card mb-3" style="height: 100%; width: 100%;">
            <div class="row g-0" style="height: 100%; width: 100%;">
              <div class="col-md-4 texto-centralizado black-background" :class="this.retratoOuPaisagemImagem">
                <img
                  :src="this.fotoModal"
                  ref="modalImage"
                  class="img-fluid rounded-start modal-image"
                  alt="Foto Modal"
                  @load="adjustModalSize"
                >
              </div>
              <div class="col-md-8" :class="this.retratoOuPaisagemDescricao">
                <div class="card-header texto-centralizado space-between">
                  <h3>{{ nomeArquivo }}</h3>
                  <button @click="closeVisualizacao" type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="card-body">
                  <p class="card-text">{{ descricao }}</p>
                </div>
              </div>
            </div>
          </div>
        </div>

        <!-- PDF -->
        <div v-if="this.fotoModal[0] === 'J'" class="modal-body" style="height: 100%; width: 100%;">
          <div class="card mb-3" style="height: 100%; width: 100%;">
            <div class="row g-0 space-between" style="height: 100%; width: 100%;">
              <div class="col-md-4 texto-centralizado black-background" :class="this.retratoOuPaisagemImagem">
                <iframe
                id="iframe"
                height="100%"
                width="80%"
                ></iframe>
              </div>
              <div class="col-md-8" :class="this.retratoOuPaisagemDescricao" style="height: 100%; width: 20%;">
                <div class="card-header texto-centralizado space-between">
                  <h3>{{ nomeArquivo }}</h3>
                  <button @click="closeVisualizacao" type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="card-body">
                  <p class="card-text">{{ descricao }}</p>
                </div>
              </div>
            </div>
          </div>
        </div>

      </div>
    </div>
  </div>

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
  <main v-if="this.info != ''" class="margin-12">
    <div class="row row-cols-1 row-cols-md-3 g-4">

      <!-- Card de INSERT -->
      <div v-if="this.selectedObraNome" class="col col-6 col-md-4 col-lg-2 tamanho-cards">
          <div class="card h-100" style="height: 100%; width: 100%;">

            <input v-if="!this.arquivoSelecionado" id="file-input" type="file" @change="handleFileChange"/>
            <label v-if="!this.arquivoSelecionado" for="file-input" id="novaFotoButton" class="label-subir-foto tamanho-cards">
              +
            </label>

            <img v-if="imageDataUrl" class="card-img" :src="imageDataUrl" alt="Imagem selecionada" style="height: 100%; width: 100%;">

            <div class="card-img-overlay" v-if="this.arquivoSelecionado" style="height: 100%; width: 100%;">
              <form  class="flex-display" style="height: 100%; width: 100%;">
                <div class="card-bodyflex" >
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
                    <button type="button" class="btn btn-secondary tamanho-font-14" @click="limparButtonActions">Limpar</button>
                    <button type="button" class="btn btn-success tamanho-font-14" id="salvaNovaFoto" @click="createArquivo">Salvar</button>
                  </div>
                </div>
              </form>
            </div>

          </div>
      </div>

      <!-- Lista de cards de Fotos. -->
      <div v-for="(foto, i) in selectedArquivosByObra" :key="i" class="col col-6 col-md-4 col-lg-2 tamanho-cards">
        <div class="card h-100 align-center" style="height: 100%; width: 100%;">
          <!-- Caso seja imagem - Renderizar IMAGEM -->
          <img v-if="foto.conteudoArquivo[0] === 'd'"
            :src="foto.conteudoArquivo"
            class="card-img img-thumbnail img-fluid image-in-cards"
            :alt="foto.nomeArquivo, i"
            style="height: 100%; width: 100%;"
            ref="cardImage"
          >
          <!-- Caso seja PDF - Renderizar PDF -->
          <img v-if="foto.conteudoArquivo[0] === 'J'"
            src="../assets/imagens/ficheiro-pdf-2.png"
            class="card-img img-thumbnail img-fluid no-border"
            :alt="foto.nomeArquivo, i"
            style="height: 65%; width: 70%;"
          >

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
            <div class="texto-centralizado">
              <button
                @click="fillVisualizaArquivoModal(foto.conteudoArquivo, foto.nomeArquivo, foto.descricao)"
                class="expandir-button no-background-color"
                data-bs-toggle="modal"
                data-bs-target="#visualizaArquivoModal"
                ><img src="../assets/imagens/expandir.png" class="expandir-button rounded mx-auto d-block">
              </button>
            </div>

            <!-- Nome -->
            <p class="card-text texto-centralizado grey-background border-radius-5">{{ foto.nomeArquivo }}</p>
          </div>
        </div>
      </div>

    </div>
  </main>

</template>

<style scope>

/* Estilos para dispositivos com largura de tela de até 768 pixels (tablets e dispositivos móveis) */
@media (max-width: 768px) {
  .tamanho-cards {
    min-height: 200px;
    max-height: 200px;
  }
}

/* Estilos para dispositivos com largura de tela acima de 768 pixels (desktop) */
@media (min-width: 769px) {
  .tamanho-cards {
    min-height: 280px;
    max-height: 280px;
  }

  .tamanho-maximo-da-modal {
    max-height: 90vh;
    max-width: 180vh;
  }

  .retrato {
    max-width: 90vh;
    max-height: 90vh;
  }

  .retrato-imagem {
    height: 100%;
    width: 65%;
  }

  .retrato-descricao {
    height: 100%;
    width: 35%;
  }

  .paisagem {
    max-height: 90vh;
    max-width: 180vh;
  }

  .paisagem-imagem {
    height: 100%;
    width: 80%;
  }

  .paisagem-descricao {
    height: 100%;
    width: 20%;
  }
}

.vh-heigth {
  width: 150vh;
  height: 100vh;
}

.margin-12 {
  margin: 12px;
}

.border-radius-5 {
  border-radius: 10px;
}

.font {
  font-family: Arial, Helvetica, sans-serif;
}

.tamanho-font-14 {
  font-size: 14px;
}

.hover-white-background:hover {
  background-color: rgba(255, 255, 255, 0.5);
}

.expandir-button {
  opacity: 0; /* Torna o botão invisível por padrão */
  transition: opacity 0.3s ease; /* Adiciona uma transição suave */
  height: 100%;
  width: 80%;
  border: none;
}

.expandir-button:hover {
  opacity: 0.8; /* Torna o botão visível quando o mouse passa por cima */
}

.no-background-color {
  background-color: rgba(255, 255, 255, 0);
}

.black-background {
  background-color: #808080;
}

.grey-background {
  /* background-color: rgba(136, 136, 136, 0.5); */
  background-color: rgba(255, 255, 255, 0.8);
}

.margin-left-10 {
  margin-left: 10px;
}

.margin-top-160 {
  margin-top: 160px;
}

.red-border {
  border: 1px solid red;
}

.texto-centralizado {
  display: flex;
  justify-content: center;
}

.no-border {
  border: none;
}

.align-center {
  display: flex;
  justify-content: center;
  align-items: center;
}

.space-between {
  display: flex;
  justify-content: space-between;
}

.flex-display {
  display: flex;
  flex-direction: column;
  justify-content: flex-end;
}

.modal-image {
  object-fit: contain;
}

.image-in-cards {
  object-fit: cover;
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
  display: flex;
  justify-content: center;
  align-items: center;
  color: #fff;
  background-color: #3D8B37;
  font-size: 100px;
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