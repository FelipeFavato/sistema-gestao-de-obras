// createWebHistory => view router production htaxis
import { createRouter, createWebHashHistory } from 'vue-router';
import Home from '../views/Home.vue';

const routes = [
  { path: '/', component: Home },
  { path: '/obra', name: 'obra', component: () => import('../views/Obra.vue') },
  { path: '/obracusto', component: () => import('../views/ObraCusto.vue') },
  { path: '/localuso', component: () => import('../views/LocalUso.vue') },
  { path: '/fornecedor', component: () => import('../views/Fornecedores.vue') },
  { path: '/produtoservico', component: () => import('../views/ProdutoServico.vue') },
  { path: '/perfil', component: () => import('../views/Perfil.vue') },
]

const router = createRouter({
  history: createWebHashHistory(),
  routes,
});

export default router;
