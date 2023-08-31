// createWebHistory => view router production htaxis
import { createRouter, createWebHashHistory } from 'vue-router';
import Home from '../views/Home.vue';
// import About from '../views/About.vue'
import Product from '../views/Product.vue';

// 2. É aqui que as rotas sao criadas
const routes = [
  { path: '/', component: Home },
  // Lazy Loading => O componente só é carregado quando clicar no link dele
  { path: '/about', name: 'about', component: () => import('../views/About.vue') },
  // Exemplo
  // { path: '/product/:id', name: 'product', component: Product }
]

// createRouter() => Cria o roteamento
const router = createRouter({
  // createWebHashHistory => Gera aquela hash na URL
  history: createWebHashHistory(),
  routes,
});

export default router;
