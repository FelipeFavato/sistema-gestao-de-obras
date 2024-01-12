<script>
import { useRouter } from 'vue-router';
import axios from 'axios';

export default {
  data () {
    return {
      // Arrays auxiliares
      info: [],
      marcasInfo: [],
      // Variaveis auxiliares
      useRouter: useRouter(),
      httpStatus: '',
      selectedMarcaNome: '',
      // Variaveis para requisição
      localStorageToken: null,
      codigo: '',
      nome: '',
      tipoProduto: '',
      marca: {}
    };
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
    // Método para esvaziar os dados quando enviada/cancelada a requisição.
    cancel() {
      this.nome = '';
      this.tipoProduto = '';
      this.marca = {};
      this.selectedMarcaNome = '';
    },
    // Método para buscar os dados no Banco.
    fetchInfoDB () {
      axios.get("/api/produto",
      {
        headers: {
          Authorization: this.localStorageToken
        }
      }).then(res => {
        this.info = res.data.sort((s1, s2) => s1['nome'].localeCompare(s2['nome']));
        this.setHttpStatusCode(res.status);
      }).catch(error => {
        this.validateHttpStatus(error.response.status);
      });
    },
    // Método para criar os dados no Banco.
    createProduto () {
      axios.post("/api/produto",
        {
          nome: this.nome,
          tipoProduto: this.tipoProduto,
          marca: this.marca
        },
        {
          headers: {
            Authorization: `Bearer ${this.localStorageToken}`
          }
        }).then((res) => {
          this.fetchInfoDB();
          this.setHttpStatusCode(res.status);
        }).catch(error => {
          this.validateHttpStatus(error.response.status);
        });
      this.cancel();
    },
    createProdutoInfoDB () {
      this.fillMarcaForRequest();
      this.createProduto();
    },
    // Método para preencher as informações da Modal.
    fillUpdateDeleteModal (codigo, nome, tipo, marca) {
      this.codigo = codigo;
      this.nome = nome;
      this.tipoProduto = tipo;
      this.marca = marca;
      this.selectedMarcaNome = marca.nome;
    },
    // Método para atualizar um Produto no Banco.
    updateInfoDB (codigo, nome, tipo) {
      this.fillMarcaForRequest();
      axios.put("/api/produto",
        {
          codigo: Number(codigo),
          nome: nome,
          tipoProduto: tipo,
          marca: this.marca
        },
        {
          headers: {
            Authorization: `Bearer ${this.localStorageToken}`
          }
        }).then((res) => {
          this.fetchInfoDB();
          this.setHttpStatusCode(res.status);
        }).catch(error => {
          this.validateHttpStatus(error.response.status);
        });
      this.cancel();
    },
    // Método para remover um produto do Banco.
    removeFromDB (codigo) {
      axios.delete("/api/produto",
        {
          headers: {
            Authorization: this.localStorageToken
          },
          data: {
            codigo: Number(codigo)
          }
        }).then((res) => {
          this.fetchInfoDB();
          this.setHttpStatusCode(res.status);
        }).catch(error => {
          this.validateHttpStatus(error.response.status);
        });
      this.cancel();
    },
    // Método para ajustar a visualização da categoria.
    fixTaxasImpostos(tipo) {
      return tipo === "TaxasImpostos" ? "Taxas/Impostos" : tipo;
    },
    // Método GET - Marcas
    fetchMarcasInfoDB () {
      axios.get('/api/marca',
      {
        headers: {
          Authorization: this.localStorageToken
        }
      }).then(res => {
        this.marcasInfo = res.data.sort((s1, s2) => s1['nome'].localeCompare(s2['nome']));
        this.setHttpStatusCode(res.status);
      }).catch(error => {
        this.validateHttpStatus(error.response.status);
      });
    },
    fillMarcaForRequest () {
      for (let chosenMarca of this.marcasInfo) {
        if (chosenMarca.nome === this.selectedMarcaNome) {
          this.marca = chosenMarca;
        }
      }
    }
  },

  // {
  //   codigo: Number(codigo),
  //   nome: nome,
  //   tipoProduto: tipo,
  //   marca: { codigoMarca: 1 } 
  //  }
  // Trocar na modal tambem

  mounted () {
    this.getLocalStorageToken();
    this.validateLogin();
    this.fetchInfoDB();
    this.fetchMarcasInfoDB();
  }
}
</script>

<template>

  <!-- Header com o botão de +Novo -->
  <header class="header middle-margin">
    <button
      type="button"
      class="btn btn-success light-green"
      data-bs-toggle="modal"
      data-bs-target="#insertModal"
    >+ Novo Produto</button>
  </header>

  <!-- DeleteModal -->
  <div class="modal fade" id="deleteModal" data-bs-backdrop="static" data-bs-keyboard="false" tabindex="-1" aria-labelledby="deleteModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-dialog-scrollable modal-sm">
      <div class="modal-content">
        <div class="modal-header">
          <h1 class="modal-title fs-5" id="deleteModalLabel">Realmente deseja excluir?</h1>
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
            type="button"
            class="btn btn-success light-green"
            data-bs-dismiss="modal"
            @click="removeFromDB(this.codigo)"
          >Sim</button>
        </div>
      </div>
    </div>
  </div>

  <!-- InsertModal -->
  <div class="modal fade" id="insertModal" data-bs-backdrop="static" data-bs-keyboard="false" tabindex="-1" aria-labelledby="insertModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-dialog-scrollable">
      <div class="modal-content">
        <div class="modal-header">
          <h1 class="modal-title fs-5" id="insertModalLabel">Novo Produto</h1>
          <button @click="cancel" type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
        </div>

        <div class="modal-body">

          <form action="POST">

            <!-- Nome -->
            <div class="mb-3">
              <label for="name-input" class="form-label bold">Nome:</label>
              <input
                type="text"
                class="form-control"
                id="name-input"
                placeholder="Produto, mão de obra, etc..."
                v-model="nome"
                maxlength="100"
                >
            </div>

            <!-- Marca -->
            <div class="mb-3">
              <label for="marca-empresa-select" class="bold">Marca/Empresa:</label>
              <select
                class="form-select"
                id="marca-empresa-select"
                v-model="selectedMarcaNome">
                <option
                  v-for="(marca, i) in marcasInfo" :key="i" :value="marca.nome"
                  >{{ marca.nome }}</option>
              </select>
            </div>

            <!-- Categoria -->
            <div class="mb-3">
              <label for="tipoProduto-select" class="bold">Categoria:</label>
              <select
                class="form-select"
                id="tipoProduto-select"
                v-model="tipoProduto">
                <option value="Material">Material</option>
                <option value="Serviço">Serviço</option>
                <option value="TaxasImpostos">Taxas/Impostos</option>
              </select>
            </div>

          </form>
        </div>

        <div class="modal-footer">
          <button
            type="button"
            class="btn btn-secondary dark-grey"
            data-bs-dismiss="modal"
            @click="cancel"
          >Fechar</button>
          <button
            type="button"
            class="btn btn-success  light-green"
            data-bs-dismiss="modal"
            @click="createProdutoInfoDB"
          >Salvar</button>
        </div>
      </div>
    </div>
  </div>

  <!-- UpdateModal -->
  <div class="modal fade" id="updateModal" data-bs-backdrop="static" data-bs-keyboard="false" tabindex="-1" aria-labelledby="updateModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-dialog-scrollable">
      <div class="modal-content">
        <div class="modal-header">
          <h1 class="modal-title fs-5" id="updateModalLabel">Editar Produto</h1>
          <button @click="cancel" type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
        </div>

        <div class="modal-body">

          <form action="PUT">

            <!-- Codigo -->
            <div class="mb-3">
              <label for="id-input" class="form-label bold">Código:</label>
              <input
                type="text"
                class="form-control"
                id="id-input"
                disabled
                v-model="codigo">
            </div>

            <!-- nome -->
            <div class="mb-3">
              <label for="nome-input" class="form-label bold">Nome:</label>
              <input
                type="text"
                class="form-control"
                id="nome-input"
                placeholder="Produto, mão de obra, etc..."
                v-model="nome"
                disabled>
            </div>

            <!-- Marca -->
            <div class="mb-3">
              <label for="marca-empresa-select" class="bold">Marca/Empresa:</label>
              <select
                class="form-select"
                id="marca-empresa-select"
                v-model="selectedMarcaNome">
                <option
                  v-for="(marca, i) in marcasInfo" :key="i" :value="marca.nome"
                  >{{ marca.nome }}</option>
              </select>
            </div>

            <!-- Categoria -->
            <div class="mb-3">
              <label for="tipoProduto-select" class="bold">Categoria:</label>
              <select
                class="form-select"
                id="tipoProduto-select"
                v-model="tipoProduto">
                <option value="Material">Material</option>
                <option value="Serviço">Serviço</option>
                <option value="TaxasImpostos">Taxas/Impostos</option>
              </select>
            </div>

          </form>

        </div>

        <div class="modal-footer">
          <button type="button" class="btn btn-secondary dark-grey" data-bs-dismiss="modal"
            @click="cancel"
          >Fechar</button>

          <button type="button" class="btn btn-success  light-green" data-bs-dismiss="modal"
            @click="updateInfoDB(this.codigo, this.nome, this.tipoProduto)"
          >Salvar</button>
        </div>
      </div>
    </div>
  </div>

  <!-- Tabela -->
  <main class="middle-margin table-responsive">
    <table class="table table-hover">
      <thead>
        <tr>
          <th scope="col">Código</th>
          <th scope="col">Nome</th>
          <th scope="col">Categoria</th>
          <th scope="col">Marca/Empresa</th>
          <th></th>
          <th></th>
          <th></th>
          <th></th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="(produto, i) in info" :key="i">
          <th scope="row">{{ produto.codigo }}</th>
          <td>{{ produto.nome }}</td>
          <td>{{ fixTaxasImpostos(produto.tipoProduto) }}</td>
          <td>{{ produto.marca ? produto.marca.nome : null }}</td>
          <td></td>
          <td></td>
          <td></td>
          <td class="editar-excluir">
            <button
              type="button"
              class="btn btn-light btn-sm small"
              title="Editar"
              data-bs-toggle="modal"
              data-bs-target="#updateModal"
              @click="fillUpdateDeleteModal(produto.codigo, produto.nome,
              produto.tipoProduto, produto.marca)"
            ><img src="../assets/imagens/editar.png" alt="lata de lixo"></button>
            <button
              type="button"
              class="btn btn-light btn-sm small"
              title="Excluir"
              data-bs-toggle="modal"
              data-bs-target="#deleteModal"
              @click="fillUpdateDeleteModal(produto.codigo)"
            ><img src="../assets/imagens/lata-de-lixo.png" alt="lata de lixo"></button>
          </td>
        </tr>
      </tbody>
    </table>
  </main>

</template>

<style scope>

.header {
  display: flex;
  justify-content: space-between;
  padding-bottom: 5px;
  /* border-bottom: solid #212529 2px; */
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
</style>