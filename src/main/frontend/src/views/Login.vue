<script>
// Requisições de login => http://localhost:8080/api/auth/login
// {
//   "email": "matheusfavato@gmail.com",
//   "senha": "159753"
// }

// {
//   "email": "felipelopesfavato@gmail.com",
//   "senha": "123456"
// }
import axios from 'axios';
import { useRouter } from 'vue-router';

export default {
  data () {
    return {
      useRouter: useRouter(),
      email: '',
      senha: '',
      httpStatusCode: ''
    }
  },

  methods: {
    // Método para redirecionar para a 'HOME' em caso de sucesso.
    redirectToHome () {
      this.useRouter.push('/');
    },
    // Método para guardar o token gerado no LocalStorage.
    saveTokenLocalStorage (token) {
      localStorage.setItem('token', token);
    },
    // Método para setar o 'this.httpStatusCode' com os casos de sucesso e erro.
    setHttpStatusCode (succesError) {
      this.httpStatusCode = succesError;
    },
    // Método que recebe a requisição de login do usuário e trata o erro em caso de erro.
    login (callback) {
      axios.post('/api/auth/login',
      {
        email: this.email,
        senha: this.senha
      }).then((res) => {
        this.saveTokenLocalStorage(res['data']['token']);
        this.redirectToHome();
      }).catch((error) => {
        this.setHttpStatusCode(error.response.status);
      })
    },
  },

  mounted () {

  },

}

</script>

<template>

  <main class="darker-body">
    <!-- Main Container -->
    <div class="container d-flex justify-content-center align-items-center min-vh-100">
      <!-- Login Container -->
      <div class="row border rounded-5 p-3 bg-white shadow box-area">
        <!-- Left Box -->
        <div class="col-md-6 rounded-4 d-flex justify-content-center align-items-center flex-column left-box" style="background: #252925;">
          <div class="featured-image mb-3 mt-3">
            <img src="../assets/imagens/capacete.png" alt="capacete" class="img-fluid" style="width: 250px;">
          </div>
          <p class="text-white fs-2" style="font-family:'Courier New', Courier, monospace; font-weight: 700;">HUB Construções</p>
          <!-- <small class="text-white text-warp text-center" style="width: 17rem; font-family: 'Courier New', Courier, monospace;"></small> -->
        </div>
        <!-- Right Box -->
        <div class="col-md-6 rounded-4 d-flex flex-column right-box">
          <div class="ajuste-tamanho">
              <form action="POST" class="ajuste-tamanho">

                <div>
                  <!-- Email -->
                  <div class="mb-3">
                    <label for="email-input" class="form-label bold">Email:</label>
                      <input
                        type="text"
                        class="form-control"
                        id="email-input"
                        placeholder="usuario@email.com"
                        v-model="email"
                        >
                  </div>

                <!-- Senha -->
                <div class="mb-3">
                  <label for="senha-input" class="form-label bold">Senha:</label>
                    <input
                      type="password"
                      class="form-control"
                      id="senha-input"
                      placeholder="******"
                      v-model="senha"
                    >
                  </div>
                </div>
                <!-- Lembrar de mim? -->

                <!-- Resposta requisição com falha. -->
                <div>
                  <p
                    class="red-letter"
                    v-if="httpStatusCode === 403"
                    >Usuário e/ou senha inválidos.
                  </p>
                </div>

                <!-- Entrar -->
                <div class="mb-3">
                  <button
                    type="button"
                    class="btn btn-success  light-green w-100 fs-6"
                    @click="login"
                  >Login</button>
                </div>

              </form>
          </div>

        </div>
      </div>
    </div>
  </main>

</template>

<style setup>

.darker-body{
  background-color: rgb(171, 180, 178);
}

.red-letter {
  color: red;
}

.company-color {
  background-color: #252925;
}

.box-area{
  width: 930px;
}

.border {
  border-radius: 1px solid #000;
}

.ajuste-tamanho {
  width: 100%;
  height: 100%;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
}

</style>
