<script>
import axios from 'axios';

export default {
  data () {
    return {
      info: [],
      codigo: '',
      nome: '',
      endereco: '',
      dataInicio: '',
      dataPrevistaFim: '',
      dataRealFim: '',
      custoPrevisto: ''
    };
  },

  methods: {
    cancel () {
      this.codigo = '';
      this.nome = '';
      this.endereco = '';
      this.dataInicio = '';
      this.dataPrevistaFim = '';
      this.dataRealFim = '';
      this.custoPrevisto = '';
    },
    fetchInfoDB () {
      axios.get("/api/obra").then(
        (res) => this.info = res.data.sort((s1, s2) => s1.codigo - s2.codigo))
    },
    createInfoDB () {
      axios.post("/api/obra",
      {
        nome: this.nome,
        endereco: this.endereco,
        dataInicio: this.dataInicio,
        dataPrevistaFim: this.dataPrevistaFim,
        dataRealFim: this.dataRealFim,
        custoPrevisto: this.custoPrevisto
      }).then(() => this.fetchInfoDB());
      this.cancel();
    },
    fillUpdateDeleteModal (codigo, nome, endereco, dataInicio, dataPrevistaFim, dataRealFim, custoPrevisto) {
      this.codigo = codigo;
      this.nome = nome;
      this.endereco = endereco;
      this.dataInicio = dataInicio;
      this.dataPrevistaFim = dataPrevistaFim;
      this.dataRealFim = dataRealFim;
      this.custoPrevisto = custoPrevisto;
    },
    updateInfoDB (cod, no, end, dataIni, dataPrFim, dataRFim, custoPrev) {
      axios.put("/api/obra",
      {
        codigo: cod,
        nome: no,
        endereco: end,
        dataInicio: dataIni,
        dataPrevistaFim: dataPrFim,
        dataRealFim: dataRFim,
        custoPrevisto: custoPrev
      }).then(() => this.fetchInfoDB());
      this.cancel();
    },
    removeFromDB (codigo) {
      axios.delete("/api/obra",
      {
        headers: {
          Authorization: ''
        },
        data: {
          codigo: Number(codigo)
        }
      }).then(() => this.fetchInfoDB())
      this.cancel();
    },
    brazilDate (data) {
      if (data === null) {
        return null
      }

      let partes = data.split("-");

      if (partes.length === 3) {
          return `${partes[2]}-${partes[1]}-${partes[0]}`;
      } else {
          return null;
      }
    },
    fixMoney (dinheiroInt) {
      if (dinheiroInt === null) {
        return null
      } else {
        const valorFormatado = dinheiroInt.toFixed(2);
  
        // Adiciona o símbolo da moeda e separadores de milhares e decimais
        const partes = valorFormatado.toString().split('.');
        const parteInteira = partes[0].replace(/\B(?=(\d{3})+(?!\d))/g, '.');
        const parteDecimal = partes[1];
  
        return `R$${parteInteira},${parteDecimal}`;
      }
    }
  },

  mounted () {
    this.fetchInfoDB();
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
    >+ Nova Obra</button>
  </header>

  <!-- DeleteModal -->
  <div class="modal" id="deleteModal" tabindex="-1" aria-labelledby="deleteModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-dialog-scrollable modal-sm">
      <div class="modal-content">
        <div class="modal-header">
          <h1 class="modal-title fs-5" id="deleteModalLabel">Realmente deseja excluir?</h1>
          <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
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
  <div class="modal" id="insertModal" tabindex="-1" aria-labelledby="insertModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-dialog-scrollable">
      <div class="modal-content">
        <div class="modal-header">
          <h1 class="modal-title fs-5" id="insertModalLabel">Nova Obra</h1>
          <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
        </div>

        <div class="modal-body">
          <form action="POST">

            <div class="mb-3">
              <label for="name-input" class="form-label bold">Nome:</label>
              <input
                type="text"
                class="form-control"
                id="name-input"
                placeholder="Projeto: "
                v-model="nome">
            </div>

            <div class="mb-3">
              <label for="address-input" class="form-label bold">Endereço:</label>
              <input
                type="text"
                class="form-control"
                id="address-input"
                placeholder="Rua da Obra, 1000"
                v-model="endereco">
            </div>

            <div class="mb-3">
              <label for="data-inicio-input" class="form-label bold">Data de início:</label>
              <input
                type="date"
                class="form-control"
                id="data-inicio-input"
                placeholder=""
                v-model="dataInicio">
            </div>

            <div class="mb-3">
              <label for="data-prevista-fim-input" class="form-label bold">Data prevista fim:</label>
              <input
                type="date"
                class="form-control"
                id="data-prevista-fim-input"
                placeholder=""
                v-model="dataPrevistaFim">
            </div>

            <div class="mb-3">
              <label for="data-real-fim-input" class="form-label bold">Data real fim:</label>
              <input
                type="date"
                class="form-control"
                id="data-real-fim-input"
                placeholder=""
                v-model="dataRealFim">
            </div>

            <div class="mb-3">
              <label for="custo-previsto-input" class="form-label bold">Custo Previsto:</label>
              <input
                type="text"
                class="form-control"
                id="custo-previsto-input"
                placeholder="R$... (inserir apenas números)"
                v-model="custoPrevisto">
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
            @click="createInfoDB"
          >Salvar</button>
        </div>
      </div>
    </div>
  </div>

  <!-- UpdateModal -->
  <div class="modal" id="updateModal" tabindex="-1" aria-labelledby="updateModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-dialog-scrollable">
      <div class="modal-content">
        <div class="modal-header">
          <h1 class="modal-title fs-5" id="updateModalLabel">Editar Obra</h1>
          <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
        </div>

        <div class="modal-body">
          <form action="PUT">

            <div class="mb-3">
              <label for="id-input" class="form-label bold">Código:</label>
              <input
                type="text"
                class="form-control"
                id="id-input"
                disabled
                v-model="codigo">
            </div>

            <div class="mb-3">
              <label for="name-input" class="form-label bold">Nome:</label>
              <input
                type="text"
                class="form-control"
                id="name-input"
                placeholder="Nome da Obra"
                v-model="nome">
            </div>

            <div class="mb-3">
              <label for="address-input" class="form-label bold">Endereço:</label>
              <input
                type="text"
                class="form-control"
                id="address-input"
                placeholder="Rua da Obra, 1000"
                v-model="endereco">
            </div>

            <div class="mb-3">
              <label for="data-inicio-input" class="form-label bold">Data de início:</label>
              <input
                type="date"
                class="form-control"
                id="data-inicio-input"
                v-model="dataInicio">
            </div>

            <div class="mb-3">
              <label for="data-prevista-fim-input" class="form-label bold">Data prevista fim:</label>
              <input
                type="date"
                class="form-control"
                id="data-prevista-fim-input"
                placeholder=""
                v-model="dataPrevistaFim">
            </div>

            <div class="mb-3">
              <label for="data-real-fim-input" class="form-label bold">Data real fim:</label>
              <input
                type="date"
                class="form-control"
                id="data-real-fim-input"
                placeholder=""
                v-model="dataRealFim">
            </div>

            <div class="mb-3">
              <label for="custo-previsto-input" class="form-label bold">Custo Previsto:</label>
              <input
                type="text"
                class="form-control"
                id="custo-previsto-input"
                placeholder="R$... (inserir apenas números)"
                v-model="custoPrevisto">
            </div>

          </form>
        </div>

        <div class="modal-footer">
          <button type="button" class="btn btn-secondary dark-grey" data-bs-dismiss="modal"
            @click="cancel"
          >Fechar</button>

          <button type="button" class="btn btn-success  light-green" data-bs-dismiss="modal"
            @click="updateInfoDB(this.codigo, this.nome, this.endereco, this.dataInicio, this.dataPrevistaFim, this.dataRealFim, this.custoPrevisto)"
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
          <th scope="col">Endereço</th>
          <th scope="col">Data de Início</th>
          <th scope="col">Data Prevista Fim</th>
          <th scope="col">Data Real Fim</th>
          <th scope="col">CustoPrevisto</th>
          <th></th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="(obra, i) in info" :key="i">
          <th scope="row">{{ obra.codigo }}</th>
          <td>{{ obra.nome }}</td>
          <td>{{ obra.endereco }}</td>
          <td>{{ brazilDate(obra.dataInicio) }}</td>
          <td>{{ brazilDate(obra.dataPrevistaFim) }}</td>
          <td>{{ brazilDate(obra.dataRealFim) }}</td>
          <td>{{ fixMoney(obra.custoPrevisto) }}</td>
          <td class="editar-excluir">
            <button
              type="button"
              class="btn btn-light btn-sm small"
              title="Editar"
              data-bs-toggle="modal"
              data-bs-target="#updateModal"
              @click="fillUpdateDeleteModal(obra.codigo, obra.nome, obra.endereco, obra.dataInicio, obra.dataPrevistaFim, obra.dataRealFim, obra.custoPrevisto)"
            ><img src="../assets/imagens/editar.png" alt="lata de lixo"></button>
            <button
              type="button"
              class="btn btn-light btn-sm small"
              title="Excluir"
              data-bs-toggle="modal"
              data-bs-target="#deleteModal"
              @click="fillUpdateDeleteModal(obra.codigo)"
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